package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.OssUtil;
import com.resto.brand.web.model.Version;
import com.resto.brand.web.model.VersionPos;
import com.resto.brand.web.service.VersionPosService;
import com.resto.brand.web.service.VersionPosShopDetailService;
import com.resto.brand.web.service.VersionService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("versionPos")
@Log4j
public class VersionPosController {

    @Autowired
    VersionPosService versionPosService;

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

    @RequestMapping("/list_all")
    @ResponseBody
    public List<VersionPos> listData(){
        return versionPosService.selectList();
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(VersionPos versionPos){
        Version version = versionService.selectById(versionPos.getVersionId());
        versionPos.setVersionNo(version.getVersionNo());
        versionPos.setDownloadAddress(version.getDownloadAddress());
        versionPos.setSubstituteMode(version.getSubstituteMode());
        versionPos.setVoluntarily(version.getVoluntarily());
        versionPos.setMessage(version.getMessage());
        versionPosService.insert(versionPos);
        return Result.getSuccess();
    }

    @RequestMapping(value="modify")
    @ResponseBody
    public Result modify(VersionPos versionPos){
        Version version = versionService.selectById(versionPos.getVersionId());
        versionPos.setVersionNo(version.getVersionNo());
        versionPos.setDownloadAddress(version.getDownloadAddress());
        versionPos.setSubstituteMode(version.getSubstituteMode());
        versionPos.setVoluntarily(version.getVoluntarily());
        versionPos.setMessage(version.getMessage());
        versionPosService.update(versionPos);
        return Result.getSuccess();
    }
}
