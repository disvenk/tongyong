package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.Platform;
import com.resto.brand.web.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by KONATA on 2016/11/1.
 * 第三方平台控制器
 */
@RequestMapping("platform")
@Controller
public class PlatformController extends GenericController {

    @Autowired
    private PlatformService platformService;

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<Platform> listData(){
        return platformService.selectAll();
    }


    @RequestMapping("/list_data")
    @ResponseBody
    public Result listDataAll(String brandId){
        List<Platform> lists =  platformService.selectPlatformByBrandId(brandId);
        return getSuccessResult(lists);
    }



    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(Long id){
        Platform platform = platformService.selectById(id);
        return getSuccessResult(platform);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid Platform platform){
        platformService.insert(platform);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid Platform platform){
        platformService.update(platform);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Long id){
        platformService.delete(id);
        return Result.getSuccess();
    }
}
