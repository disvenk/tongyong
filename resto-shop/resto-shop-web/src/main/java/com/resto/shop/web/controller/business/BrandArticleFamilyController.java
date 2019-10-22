package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.AccountChargeOrder;
import com.resto.brand.web.model.DistributionMode;
import com.resto.brand.web.service.DistributionModeService;
import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.ArticleFamily;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.ArticleFamilyService;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-11-05 14:33
 */

@Controller
@RequestMapping("brandArticleFamily")
public class BrandArticleFamilyController extends GenericController {

    @Autowired
    ArticleFamilyService articleFamilyService;

    @Resource
    DistributionModeService distributionModeService;

    @RequestMapping("/list")
    public String list(){
        return "brandarticlefamily/list";
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<ArticleFamily> listData(){
        return articleFamilyService.selectBrandArticleFamilyList();
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid ArticleFamily articleFamily, HttpServletRequest request){
        articleFamily.setId(ApplicationUtils.randomUUID());
        articleFamily.setShopDetailId("-1");
        articleFamily.setOpenArticleLibrary(true);
        articleFamilyService.insert(articleFamily);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid ArticleFamily articleFamily){
        articleFamily.setShopDetailId(getCurrentBrandId());
        articleFamilyService.update(articleFamily);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id) {
        articleFamilyService.delete(id);
        return Result.getSuccess();
    }

    @RequestMapping("querydistributionMode")
    @ResponseBody
    public Result querydistributionMode(){
        List<DistributionMode> distributions =  distributionModeService.selectList();
        return getSuccessResult(distributions);
    }
}
