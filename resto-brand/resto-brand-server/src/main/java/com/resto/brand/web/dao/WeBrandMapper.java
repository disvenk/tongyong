package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.WeBrand;

import java.util.Date;
import java.util.List;

public interface WeBrandMapper  extends GenericDao<WeBrand,Integer> {
    int deleteByPrimaryKey(Long id);

    int insert(WeBrand record);

    int insertSelective(WeBrand record);

    WeBrand selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeBrand record);

    int updateByPrimaryKey(WeBrand record);

    //获取日期下的品牌分数
    List<WeBrand> selectWeBrandList(Date createTime);
}
