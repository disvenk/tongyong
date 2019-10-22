package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.PictureResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.BucketNameType;
import com.resto.brand.core.util.FileUpload;
import com.resto.brand.core.util.OssUtil;
import com.resto.brand.core.util.UploadFilesUtil;
import com.resto.brand.core.util.jindutiao.UploadFilesUtilJindu;
import com.resto.brand.web.model.Version;
import com.resto.brand.web.model.VersionPackage;
import com.resto.brand.web.service.VersionPosBrandService;
import com.resto.brand.web.service.VersionPosShopDetailService;
import com.resto.brand.web.service.VersionService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("version")
@Log4j
public class VersionController {

    @Autowired
    VersionPosBrandService versionPosBrandService;

    @Autowired
    VersionPosShopDetailService versionPosShopDetailService;

    @Autowired
    VersionService versionService;
    @RequestMapping("/list")
    public void list(){
    }
    @RequestMapping("/brandlist")
    public String versionBrandList(){
        return "versionBrand/list";
    }

    @RequestMapping("/versionList")
    public void versionList(){

    }

    @RequestMapping("/versionPackage")
    public void versionPackage(){

    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<Version> listData(){
        return versionService.selectVersionAsc();
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(Version version){
        versionService.insert(version);
        return Result.getSuccess();
    }

    @RequestMapping(value="modify")
    @ResponseBody
    public Result modify(Version version){
        versionService.update(version);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Integer id){
        versionService.delete(id);
        return Result.getSuccess();
    }
//    @RequestMapping("upload")
//    public Result upload(HttpServletRequest request){
//
//        //文件上传的请求
//        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
//        //获取请求的参数
//        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
//        Iterator<Map.Entry<String, MultipartFile>> iterator = fileMap.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<String, MultipartFile> next = iterator.next();
//            MultipartFile file = next.getValue();
//            if(!file.isEmpty()){
//                try {
//                    InputStream in = file.getInputStream();
//                    String name = file.getName();
//                    String originalFilename = file.getOriginalFilename();
//                    String key = OssUtil.uploadFileOSS(in, name);
//                    String url = OssUtil.getUrl(key);
//                    return new JSONResult(url,"success");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//           }
//        }
//        return new JSONResult("失败");
//    }

    @RequestMapping("upload")
    @ResponseBody
    public Result uploadFile(MultipartFile file,HttpServletRequest request){
        String type = request.getParameter("type");
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0, lastR) + "/";
        String filePath = "upload/files/" + DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        File finalFile = FileUpload.fileUp(file, systemPath + filePath, UUID.randomUUID().toString(), type);
        PictureResult picResult = UploadFilesUtilJindu.uploadPic(request,finalFile);
        if(picResult.getError() == 0){
            return new Result(picResult.getUrl(),true);
        }else{
            return new Result(false);
        }
    }

    /**
     * 获取实时长传进度
     * @param request
     * @return
     */
    @RequestMapping ("item/percent")
    @ResponseBody
    public int getUploadPercent(HttpServletRequest request){
        HttpSession session = request.getSession();
        int percent = session.getAttribute("upload_percent") == null ? 0: (int) session.getAttribute("upload_percent");
        return percent;
    }

    /**
     * 重置上传进度
     * @param request
     * @return
     */
    @RequestMapping ("/percent/reset")
    @ResponseBody
    public Result resetPercent(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("upload_percent",0);
        return Result.getSuccess();
    }

    @RequestMapping("uploadPackage")
    @ResponseBody
    public String uploadPackage(MultipartFile file,HttpServletRequest request){

        try {
            //上传文件到阿里服务器
            String url = OssUtil.uploadFile(file, BucketNameType.NEW_POS);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/list_package")
    @ResponseBody
    public List<VersionPackage> listpackageData(){

        return versionService.selectversionPackage();
    }

    @RequestMapping("createPackage")
    @ResponseBody
    public Result createPackage( String version){
        versionService.createPackage(version);
        return Result.getSuccess();
    }

    @RequestMapping("updatePackage")
    @ResponseBody
    public Result updatePackage( String version){
        versionService.updatePackage(version);
        return Result.getSuccess();
    }
}
