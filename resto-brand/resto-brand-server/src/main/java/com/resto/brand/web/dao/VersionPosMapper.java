package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.VersionPos;
import org.apache.ibatis.annotations.Param;

public interface VersionPosMapper extends GenericDao<VersionPos,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(VersionPos record);

    int insertSelective(VersionPos record);

    VersionPos selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VersionPos record);

    int updateByPrimaryKey(VersionPos record);

    int updateVersionNOAndDownloadAddressByVersionId(@Param("versionNo") String versionNo, @Param("downloadAddress") String downloadAddress, @Param("substituteMode") Integer substituteMode, @Param("voluntarily") Integer voluntarily, @Param("versionId") Integer versionId);

     VersionPos selectVersionMessage();


    VersionPos selectTotalversion();
}