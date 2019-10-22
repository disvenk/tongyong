package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.RedisService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.RecommendCategory;
import com.resto.shop.web.service.RecommendCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by xielc on 2017/6/29.
 */
@RequestMapping("recommendCategory")
@Controller
public class RecommendedCategoryController extends GenericController {

    @Autowired
    private RecommendCategoryService recommendCategoryService;

    @Autowired
    private BrandSettingService brandSettingService;

    @Autowired
    RedisService redisService;
    
    @RequestMapping("/list")
    public ModelAndView index(){
        BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if(setting != null && setting.getRecommendCategory().equals(new Integer(1))){ //开启推荐餐包功能
            return new ModelAndView("recommendCategory/list");
        }else{
            return new ModelAndView("recommendCategory/none");
        }



    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<RecommendCategory> getList(){
        List<RecommendCategory> result =  recommendCategoryService.selectListByShopId(getCurrentShopId());
        return result;
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid RecommendCategory recommendCategory,String[] articleId,String[] articleName,String[] recommendSort) {
        recommendCategory.setShopDetailId(getCurrentShopId());
        try {
            recommendCategoryService.create(recommendCategory,articleId,articleName,recommendSort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new Result(true);
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid RecommendCategory recommendCategory,String[] articleId,String[] articleName,String[] recommendSort) {
        recommendCategoryService.updateInfo(recommendCategory,articleId,articleName,recommendSort);
        if (redisService.get(getCurrentShopId() + "articleAttr") != null) {
            redisService.remove(getCurrentShopId() + "articleAttr");
        }
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id) {
        recommendCategoryService.deleteInfo(id);
        return Result.getSuccess();
    }

}
