package com.resto.shop.web.dao;

import com.resto.shop.web.model.Experience;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExperienceMapper  extends GenericDao<Experience,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(Experience record);

    List<Experience> selectListByShopId(String shopId);

    int insertSelective(Experience record);

    Experience selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Experience record);

    int updateByPrimaryKey(Experience record);

    int deleteByTitle(@Param("title") String title, @Param("shopId") String shopId);
}
