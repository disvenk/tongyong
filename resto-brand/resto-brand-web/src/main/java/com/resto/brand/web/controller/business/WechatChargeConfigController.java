package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.WechatChargeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by KONATA on 2016/12/26.
 * 微信充值账户配置
 */
@Controller
@RequestMapping("/wechatChargeConfig")
public class WechatChargeConfigController extends GenericController {

    @Autowired
    private BrandSettingService brandSettingService;

    @Autowired
    private WechatChargeConfigService wechatChargeConfigService;

    @RequestMapping("/create")
    @ResponseBody
    public Result create(@Valid BrandSetting brandSetting) {
        String id = wechatChargeConfigService.createWechatChargeConfig(brandSetting.getWechatChargeConfig());
        brandSettingService.updateWechatChargeConfig(brandSetting.getId(), id);
        return Result.getSuccess();
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Result modify(@Valid BrandSetting brandSetting) {
        wechatChargeConfigService.update(brandSetting.getWechatChargeConfig());
        return Result.getSuccess();
    }
}
