package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.FileUpload;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.WxServerConfig;
import com.resto.brand.web.service.WxServerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by KONATA on 2016/12/3.
 */
@Controller
@RequestMapping("/wxServer")
public class WxServerController extends GenericController {

    @Autowired
    private WxServerConfigService wxServerConfigService;


    @RequestMapping("/list")
    public void list(){
    }


    @RequestMapping("/list_all")
    @ResponseBody
    public List<WxServerConfig> listData(){
        List<WxServerConfig> lists = wxServerConfigService.getConfigList();
        return lists;
    }


    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(Long id){
        WxServerConfig wxServerConfig = wxServerConfigService.selectById(id);
        return getSuccessResult(wxServerConfig);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid WxServerConfig wxServerConfig){
        wxServerConfigService.insert(wxServerConfig);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid WxServerConfig wxServerConfig){
        wxServerConfigService.update(wxServerConfig);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Long id){
        wxServerConfigService.delete(id);
        return Result.getSuccess();
    }

    @RequestMapping("uploadFile")
    @ResponseBody
    public String uploadFile(MultipartFile file,HttpServletRequest request){
        String filePath = "/root/uploads/payChartFiles/";
        File finalFile= FileUpload.fileUp(file, filePath, UUID.randomUUID().toString(), null);
        log.info("路径为："+finalFile.getAbsolutePath());
        return finalFile.getAbsolutePath();
    }
}
