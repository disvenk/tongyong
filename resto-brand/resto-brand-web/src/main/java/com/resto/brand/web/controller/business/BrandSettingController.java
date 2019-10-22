package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("brandsetting")
public class BrandSettingController extends GenericController {

    @Resource
    BrandSettingService brandsettingService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private WechatConfigService wechatConfigService;

    @Autowired
    RedisService redisService;


    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<BrandSetting> listData(HttpServletRequest request) {
        List<BrandSetting> bs=brandsettingService.selectList();
        return bs;
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(String id) {
        BrandSetting brandsetting = brandsettingService.selectById(id);
        return getSuccessResult(brandsetting);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid BrandSetting brand) {
        brandsettingService.insert(brand);
        return Result.getSuccess();
    }

    @RequestMapping("cleanBrandSetting")
    @ResponseBody
    public Result cleanBrandSetting(String id) {
//        MemcachedUtils.flus();
        Brand brand = brandService.selectBrandBySetting(id);
//        MemcachedUtils.clean(brand.getId(), brand.getBrandSign());
//        RedisUtil.clean(brand.getId(), brand.getBrandSign());
        redisService.clean(brand.getId(), brand.getBrandSign());
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid BrandSetting bs) {
        BrandSetting brandSetting = brandsettingService.selectById(bs.getId());
        Brand brand = brandService.selectBrandBySetting(brandSetting.getId());
        if (brandSetting.getIsAllowExtraPrice() == 0) {
            bs.setIsUseServicePrice(0);
        }
        brandsettingService.updateBrandSetting(bs);
        if(redisService.get(brand.getId()+"setting") != null){
            redisService.remove(brand.getId()+"setting");
        }
//        MemcachedUtils.flus();
//        redisService.removePattern("*");
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id) {
        brandsettingService.delete(id);
        return Result.getSuccess();
    }

    @RequestMapping("savePlatform")
    @ResponseBody
    public Result create(String[] platform, String id) {
        Brand brand = brandService.selectBrandBySetting(id);
        if(brand == null){
            return new Result(false);
        }
        platformService.deleteConfig(brand.getId());
        for(String plat : platform){
            platformService.insertConfig(brand.getId(),plat);
        }
        return new Result(true);

    }
}
