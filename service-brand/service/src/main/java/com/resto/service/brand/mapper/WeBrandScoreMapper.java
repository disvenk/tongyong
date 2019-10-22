package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.WeBrandScore;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface WeBrandScoreMapper  extends BaseDao<WeBrandScore,Integer> {
    int deleteByPrimaryKey(Long id);

    int insert(WeBrandScore record);

    int insertSelective(WeBrandScore record);

    WeBrandScore selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeBrandScore record);

    int updateByPrimaryKey(WeBrandScore record);

    WeBrandScore selectByBrandIdAndCreateTime(@Param("brandId") String brandId, @Param("createTime") Date createTime);
}
