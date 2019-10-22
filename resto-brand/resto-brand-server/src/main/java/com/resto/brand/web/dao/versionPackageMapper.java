package com.resto.brand.web.dao;

import com.resto.brand.web.model.VersionPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface versionPackageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VersionPackage record);

    int insertSelective(VersionPackage record);

    VersionPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VersionPackage record);

    int updateByPrimaryKey(VersionPackage record);

    List<VersionPackage> selectversionPackage();

    int deleteAll();

    VersionPackage selectPackageUrlByBrandId(@Param("brandId") String brandId);
}