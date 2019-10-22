package com.resto.brand.web.dao;

import com.resto.brand.web.model.VersionBrandPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VersionBrandPackageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VersionBrandPackage record);

    int insertSelective(VersionBrandPackage record);

    VersionBrandPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VersionBrandPackage record);

    int updateByPrimaryKey(VersionBrandPackage record);

    int deleteByBrandId(@Param("brandId") String brandId);

    List<VersionBrandPackage> selectBrandPackage(@Param("packageId")Integer packageId);

    int deleteByPackageId(@Param("packageId") Integer packageId);
}