package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.VersionPos;
import com.resto.brand.web.model.VersionPosBrand;
import com.resto.brand.web.model.VersionPosShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.VersionPosBrandService;
import com.resto.brand.web.service.VersionPosShopDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("versionBrand")
public class VersionBrandController {

    @Autowired
    VersionPosBrandService versionPosBrandService;

    @Autowired
    VersionPosShopDetailService versionPosShopDetailService;

    @Autowired
    BrandService brandService;

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<VersionPosBrand> listData() {
        List<VersionPosBrand> versionPosBrands = versionPosBrandService.selectList();
        return versionPosBrands;
    }

    @RequestMapping("/list_ShopDetail")
    @ResponseBody
    public List<VersionPosShopDetail> listVersionShopDetailData(String brandId) {
        List<VersionPosShopDetail> versionPosShopDetails = versionPosShopDetailService.selectByBrandId(brandId);
        return versionPosShopDetails;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Result create(@RequestBody VersionPosBrand versionPosBrand) {
        versionPosBrandService.insert(versionPosBrand);
        return Result.getSuccess();
    }

    @RequestMapping(value = "modify", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Result modify(@RequestBody VersionPosBrand versionPosBrand) {
        versionPosBrandService.update(versionPosBrand);

        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Integer id) {
        VersionPosBrand versionPosBrand = versionPosBrandService.selectById(id);
        versionPosBrandService.delete(id);
        versionPosShopDetailService.deleteByBrandId(versionPosBrand.getBrandId());
        return Result.getSuccess();
    }

    @RequestMapping("/selectBrandList")
    @ResponseBody
    public Result selectBrandList() {
        List<Brand> brands = versionPosBrandService.selectNotExistVersionPosBrand();
        if (brands != null) {
            return new JSONResult(brands);
        }
        return Result.getSuccess();
    }

    @RequestMapping("/selectBrandListByVersionId")
    @ResponseBody
    public Result selectBrandListByVersionId(Integer versionId) {
        List<VersionPosBrand> versionPosBrands = versionPosBrandService.selectByVersionId(versionId);
        return new JSONResult(versionPosBrands);
    }


    @RequestMapping("/selectTotalversion")
    @ResponseBody
    public Result selectTotalversion() {
        VersionPos versionPos = versionPosBrandService.selectTotalversion();
        return new JSONResult(versionPos);
    }

}
