package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.Experience;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExperienceMapper extends BaseDao<Experience,Integer> {

    int deleteByPrimaryKey(Integer id);

    int insert(Experience record);

    List<Experience> selectListByShopId(String shopId);

    int insertSelective(Experience record);

    Experience selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Experience record);

    int updateByPrimaryKey(Experience record);

    int deleteByTitle(@Param("title") String title, @Param("shopId") String shopId);
}
