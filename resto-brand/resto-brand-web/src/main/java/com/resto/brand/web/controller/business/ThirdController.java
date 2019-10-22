package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.ThirdParam;
import com.resto.brand.web.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by KONATA on 2017/2/8.
 * 第三方对接接口
 */
@RequestMapping("/third")
@Controller
public class ThirdController extends GenericController {

    @Autowired
    private PlatformService thirdService;

    @RequestMapping("/list")
    public void list() {
    }


    @RequestMapping("/list_all")
    @ResponseBody
    public List<ThirdParam> listData() {
        return thirdService.selectParamList();
    }


    @RequestMapping("/create")
    @ResponseBody
    public Result create(@Valid ThirdParam thirdParam){
        thirdService.insertParam(thirdParam);
        return Result.getSuccess();
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(Long id){
        thirdService.deleteParam(id);
        return Result.getSuccess();
    }
}
