package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.VersionPosShopDetail;

import java.util.List;

public interface VersionPosShopDetailService extends GenericService<VersionPosShopDetail,Integer> {

    int deleteByBrandId(String brandId);

    List<VersionPosShopDetail> selectByBrandId(String brandId);

    int updatePosVersion(String brandId,String shopdetailId,String shopVersion);

}
