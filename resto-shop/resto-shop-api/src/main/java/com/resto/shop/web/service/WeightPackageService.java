package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WeightPackage;

import java.util.List;

public interface WeightPackageService extends GenericService<WeightPackage, Long> {

    List<WeightPackage> getAllWeightPackages(String shopId);

    WeightPackage insertDetail(WeightPackage weightPackage);

    void initWeightPackageDetail(WeightPackage weightPackage);

    WeightPackage selectByDateShopId(String name, String shopId);

    WeightPackage getWeightPackageById(Long id);

    /**
     * 根据 店铺ID 查询店铺下的所有  WeightPackage  数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<WeightPackage> selectWeightPackageByShopId(String shopId);

    List<WeightPackage> getWeightPackage();

    List<WeightPackage> getAllBrandWeightPackages(String currentShopId);
}
