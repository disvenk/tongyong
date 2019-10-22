package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.WeightPackageDetail;

import java.util.List;

public interface WeightPackageDetailMapper extends GenericDao<WeightPackageDetail,Long> {

    int deleteByPrimaryKey(Long id);

    int insert(WeightPackageDetail record);

    int insertSelective(WeightPackageDetail record);

    WeightPackageDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeightPackageDetail record);

    int updateByPrimaryKey(WeightPackageDetail record);

    void deleteDetails(Long weightPackageId);

    List<WeightPackageDetail> selectWeightPackageDetailByShopId(String shopId);
}
