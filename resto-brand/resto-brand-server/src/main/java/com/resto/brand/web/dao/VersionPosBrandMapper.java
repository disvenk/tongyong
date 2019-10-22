package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.VersionPosBrand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VersionPosBrandMapper extends GenericDao<VersionPosBrand,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(VersionPosBrand record);

    int insertSelective(VersionPosBrand record);

    VersionPosBrand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VersionPosBrand record);

    int updateByPrimaryKey(VersionPosBrand record);

    VersionPosBrand selectByBrandId(@Param("brandId") String brandId);

    int updateVersionNOAndDownloadAddressByVersionId(@Param("versionNo") String versionNo, @Param("downloadAddress") String downloadAddress, @Param("substituteMode") Integer substituteMode, @Param("voluntarily") Integer voluntarily, @Param("versionId") Integer versionId);

    void deleteByVersionId(@Param("versionId") Integer versionId);

    List<Brand> selectNotExistVersionPosBrand();

    List<VersionPosBrand> selectByVersionId(@Param("versionId") Integer versionId);
}