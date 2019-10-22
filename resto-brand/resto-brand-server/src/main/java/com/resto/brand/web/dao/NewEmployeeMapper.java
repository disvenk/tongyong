package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.NewEmployee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewEmployeeMapper extends GenericDao<NewEmployee,String> {
    int deleteByPrimaryKey(String id);

    int insert(NewEmployee record);

    int insertSelective(NewEmployee record);

    NewEmployee selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NewEmployee record);

    int updateByPrimaryKey(NewEmployee record);

    List<NewEmployee> selectByIds(List<String> ids);

    List<NewEmployee> selectByShopId(@Param("shopId") String shopId);

    List<NewEmployee> selectByBrandId(@Param("brandId") String brandId);

    NewEmployee selectByTelephone(@Param("telephone") String telePhone);
}
