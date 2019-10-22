package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ElemeToken;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ElemeTokenService;
import com.resto.brand.web.service.ShopDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by carl on 2017/9/18.
 */

@Controller
@RequestMapping("elemetoken")
public class ElemeSetUpController extends GenericController {

    @Resource
    ElemeTokenService elemeTokenService;
    @Resource
    BrandService brandService;
    @Resource
    ShopDetailService shopDetailService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<ElemeToken> listData(){
        return elemeTokenService.selectList();
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(Long id){
        ElemeToken elemeToken = elemeTokenService.selectById(id);
        return getSuccessResult(elemeToken);
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


    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid ElemeToken elemeToken){
        ElemeToken token = elemeTokenService.getTokenByShopId(elemeToken.getShopId());
        if(token != null){
            return new Result("该店铺已经创建了饿了么token！", false);
        }
        elemeToken.setUpdateTime(new Date());
        elemeToken.setTokenType("Bearer");
        elemeToken.setExpiresIn((long) 2592000);
        elemeToken.setScope("all");
        elemeTokenService.insert(elemeToken);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid ElemeToken elemeToken){
        elemeTokenService.update(elemeToken);
        return Result.getSuccess();
    }
}
