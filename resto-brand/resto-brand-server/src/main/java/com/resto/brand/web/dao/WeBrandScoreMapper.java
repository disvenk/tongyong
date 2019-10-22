package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.WeBrandScore;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface WeBrandScoreMapper  extends GenericDao<WeBrandScore,Integer> {
    int deleteByPrimaryKey(Long id);

    int insert(WeBrandScore record);

    int insertSelective(WeBrandScore record);

    WeBrandScore selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeBrandScore record);

    int updateByPrimaryKey(WeBrandScore record);

    WeBrandScore selectByBrandIdAndCreateTime(@Param("brandId") String brandId, @Param("createTime") Date createTime);
}
