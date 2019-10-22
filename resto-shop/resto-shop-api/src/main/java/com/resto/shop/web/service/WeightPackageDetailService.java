package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WeightPackageDetail;

import java.util.List;

public interface WeightPackageDetailService extends GenericService<WeightPackageDetail, Long> {

    int insertDetail(Long weightPackageId, WeightPackageDetail weightPackageDetail);

    void deleteDetails(Long weightPackageId);

    /**
     * 根据 店铺ID 查询店铺下的所有  WeightPackageDetail  数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<WeightPackageDetail> selectWeightPackageDetailByShopId(String shopId);
}
