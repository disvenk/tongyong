package com.resto.brand.web.controller.business;

import com.google.zxing.WriterException;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.Encrypter;
import com.resto.brand.core.util.FileToZip;
import com.resto.brand.core.util.QRCodeUtil;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.TableQrcode;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.TableQrcodeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by carl on 2016/12/16.
 */
@Controller
@RequestMapping("tableQrcode")
public class TableQrcodeController extends GenericController {

    private static SimpleDateFormat myDate=new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private TableQrcodeService tableQrcodeService;
    @Resource
    BrandService brandService;
    @Resource
    ShopDetailService shopDetailService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<TableQrcode> listData(){
        return tableQrcodeService.selectList();
    }

    @RequestMapping("/brandList")
    @ResponseBody
    public Result queryBrands() {
        List<Brand> brands = brandService.selectList();
        return getSuccessResult(brands);
    }

    @RequestMapping("/shopList")
    @ResponseBody
    public Result shopList(String brandId){
        List<ShopDetail> shops = shopDetailService.selectByBrandId(brandId);
        return getSuccessResult(shops);
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(Long id){
        TableQrcode tableQrcode = tableQrcodeService.selectById(id);
        return getSuccessResult(tableQrcode);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(String brandId, String shopId, Integer beginTableNumber, Integer endTableNumber, String ignoreNumber ){
        List<TableQrcode> tableQrcodes = tableQrcodeService.selectList();
        int a = 0;
        for (int i = beginTableNumber; i <= endTableNumber; i++) {//循环生成二维码
            if (ignoreNumber(i,ignoreNumber) && isTableNumber(i,tableQrcodes,shopId,brandId)){
                TableQrcode tableQrcode = new TableQrcode();
                tableQrcode.setBrandId(brandId);
                tableQrcode.setShopDetailId(shopId);
                tableQrcode.setTableNumber(i);
                tableQrcode.setCreateTime(new Date());
                tableQrcodeService.insert(tableQrcode);
                a++;
            }
        }
        if(a != 0){
            return Result.getSuccess();
        }else{
            Result result = new Result(false);
            return result;
        }
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid TableQrcode tableQrcode){
        tableQrcode.setUpdateTime(new Date());
        tableQrcodeService.update(tableQrcode);
        return Result.getSuccess();
    }

    @RequestMapping("/run")
    @ResponseBody
    public Result run(String brandId,String shopId, Integer beginTableNumber, Integer endTableNumber, String ignoreNumber , HttpServletRequest request)
            throws IOException, InterruptedException, WriterException {
        ShopDetail shopDetail = shopDetailService.selectById(shopId);
        Brand brand = brandService.selectById(brandId);
        String brandSign = brand.getBrandSign();
        String fileSavePath = getFilePath(request,null);
        deleteFile(new File(fileSavePath));//删除历史生成的文件
        String filepath = getFilePath(request,shopDetail.getName());
        for (int i = beginTableNumber; i <= endTableNumber; i++) {//循环生成二维码
            if (ignoreNumber(i,ignoreNumber)) {
                String fileName = shopDetail.getName() + "-" + i + ".jpg" ;
                TableQrcode tableQrcode = tableQrcodeService.selectByTableNumberShopId(shopDetail.getId(), i);
                String contents = getShopUrlRun(brandSign, tableQrcode.getId());
                QRCodeUtil.createQRCode(contents, filepath, fileName);
            }
        }
        Date data = new Date();
        String now = myDate.format(data);
        //打包
        FileToZip.fileToZip(filepath, fileSavePath, ("桌号二维码" + now));
        Result result = new Result(true);
        result.setMessage("桌号二维码" + now +".zip");
        System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
        return result;
    }

    @RequestMapping("/download")
    @ResponseBody
    public Result run(String ids, HttpServletRequest request , HttpServletResponse response)
            throws IOException, InterruptedException, WriterException {
        int count=0;
        for(int i=0;i<ids.length();i++){
            if(ids.charAt(i)==','){
                count++;
            }
        }
        String[] idArr = null;
        if(count > 0){
            idArr = ids.substring(1,ids.length()-1).split(",");
        }else{
            idArr = ids.split(",");
        }
        TableQrcode tableQrcodeFrist = tableQrcodeService.selectById(Long.parseLong(idArr[0]));
        Brand brand = brandService.selectById(tableQrcodeFrist.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectById(tableQrcodeFrist.getShopDetailId());
        String brandSign = brand.getBrandSign();
        String fileSavePath = getFilePath(request,null);
        deleteFile(new File(fileSavePath));//删除历史生成的文件
        String filepath = getFilePath(request,shopDetail.getName());

        for (int i = 0; i < idArr.length; i++) {//循环生成二维码
            TableQrcode tableQrcode = tableQrcodeService.selectById(Long.parseLong(idArr[i]));
            ShopDetail shop = shopDetailService.selectById(tableQrcode.getShopDetailId());
            String fileName = shop.getName() + "-" + tableQrcode.getTableNumber() + ".jpg" ;
            String contents = getShopUrl(brandSign, Long.parseLong(idArr[i]));
            QRCodeUtil.createQRCode(contents, filepath, fileName);
        }
        Date data = new Date();
        String now = myDate.format(data);
        //打包
        FileToZip.fileToZip(filepath, fileSavePath, ("桌号二维码" + now));
        Result result = new Result(true);
        result.setMessage("桌号二维码" + now +".zip");
        System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
        return result;
    }

    @RequestMapping("/downloadFile")
    public String donloadFile(String fileName ,HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        String downLoadPath = getFilePath(request,fileName);
        try {
            long fileLength = new File(downLoadPath).length();
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        return null;
    }

    /**
     * 判断是否包含 要忽略的值
     * @param index
     * @param ignoreNumber
     * @return
     */
    public boolean ignoreNumber(int index,String ignoreNumber){
        boolean flag = true;
        if(ignoreNumber!=null && !("").equals(ignoreNumber)){
            ignoreNumber = ignoreNumber.replaceAll("，", ",");
            String[] ignoreNumbers = ignoreNumber.split(",");//保存当前要忽略的值
            for(String number : ignoreNumbers){
                if((index + "").indexOf(number) != -1){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 判断店铺是否存在这个桌号
     * @param index
     * @param tableQrcodes
     * @return
     */
    public boolean isTableNumber(int index, List<TableQrcode> tableQrcodes, String shopId, String brandId){
        boolean flag = true;
        if(tableQrcodes != null){
            for(int j = 0; j < tableQrcodes.size(); j++){
                if(index == tableQrcodes.get(j).getTableNumber() && tableQrcodes.get(j).getShopDetailId().equals(shopId) && tableQrcodes.get(j).getBrandId().equals(brandId)){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 得到 店铺专属的链接
     * @param brandSign	品牌标识
     * @param id	店铺ID
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getShopUrl(String brandSign, Long id) throws UnsupportedEncodingException{
        String v = Encrypter.encrypt(id.toString());
        String url = "http://"+brandSign+".restoplus.cn/wechat/index?vv=" + v;
        return url;
    }

    public String getShopUrlRun(String brandSign, Long id) throws UnsupportedEncodingException{
        String v = Encrypter.encrypt(id.toString());
        String url = "http://"+brandSign+".restoplus.cn/wechat/index?vv=" + v;
        return url;
    }

    /**
     * 得到要 保存 二维码文件夹的路径
     * @param request
     * @param fileName
     * @return
     */
    public String getFilePath(HttpServletRequest request, String fileName){
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0,lastR)+"/";
        String filePath = "qrCodeFiles/";
        if(fileName!=null){
            filePath += fileName;
        }
        return systemPath+filePath;
    }

    /**
     * 删除上次生成的二维码文件
     */
    public static void deleteFile(File file){
        System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
        if(file.isFile() && file.exists()){//表示该文件不是文件夹
            file.delete();
        }else if(file.isDirectory()){
            //首先得到当前的路径
            String[] childFilePaths = file.list();
            for(String childFilePath : childFilePaths){
                File childFile=new File(file.getAbsolutePath()+File.separator+childFilePath);
                deleteFile(childFile);
            }
            file.delete();
        }
    }

}
