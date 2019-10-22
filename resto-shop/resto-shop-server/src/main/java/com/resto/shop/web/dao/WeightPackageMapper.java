package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.WeightPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeightPackageMapper extends GenericDao<WeightPackage,Long> {

    int deleteByPrimaryKey(Long id);

    int insert(WeightPackage record);

    int insertSelective(WeightPackage record);

    WeightPackage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeightPackage record);

    int updateByPrimaryKey(WeightPackage record);

    List<WeightPackage> getAllWeightPackages(String shopId);

    WeightPackage selectByDateShopId(@Param("name") String name, @Param("shopId") String shopId);

    WeightPackage getWeightPackageById(Long id);

    List<WeightPackage> selectWeightPackageByShopId(String shopId);

    List<WeightPackage> getWeightPackage();

    List<WeightPackage> getAllBrandWeightPackages(@Param("shopId") String shopId);
}
