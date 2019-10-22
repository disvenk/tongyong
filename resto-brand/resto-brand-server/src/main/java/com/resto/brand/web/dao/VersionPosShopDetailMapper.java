package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.VersionPosShopDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VersionPosShopDetailMapper extends GenericDao<VersionPosShopDetail,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(VersionPosShopDetail record);

    int insertSelective(VersionPosShopDetail record);

    VersionPosShopDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VersionPosShopDetail record);

    int updateByPrimaryKey(VersionPosShopDetail record);

    List<VersionPosShopDetail> selectByBrandIdAndShopDetailId(@Param("brandId") String brandId, @Param("shopDetailId") String shopDetailId);

    List<VersionPosShopDetail> selectByBrandId(@Param("brandId") String brandId);

    int deleteByBrandId(String brandId);

    int updateVersionNOAndDownloadAddressByVersionId(@Param("versionNo") String versionNo, @Param("downloadAddress") String downloadAddress, @Param("substituteMode") Integer substituteMode, @Param("voluntarily") Integer voluntarily, @Param("versionId") Integer versionId);

    void deleteByVersionId(@Param("versionId") Integer versionId);

    List<VersionPosShopDetail> selectByVersionId(@Param("versionId") Integer versionId);

    int updatePosVersion(@Param("brandId")String brandId,@Param("shopdetailId")String shopdetailId,@Param("shopVersion")String shopVersion);
}