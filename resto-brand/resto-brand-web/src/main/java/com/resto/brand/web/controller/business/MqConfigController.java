package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.MqConfig;
import com.resto.brand.web.service.MqConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by KONATA on 2017/7/12.
 */
@Controller
@RequestMapping("mqConfig")
public class MqConfigController extends GenericController {

    @Autowired
    MqConfigService mqConfigService;

    @RequestMapping("/list_all")
    @ResponseBody
    public Result list(){
        List<MqConfig> mqConfigs = mqConfigService.selectList();
        return getSuccessResult(mqConfigs);
    }
}
