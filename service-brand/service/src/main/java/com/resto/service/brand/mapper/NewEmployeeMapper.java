package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.NewEmployee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewEmployeeMapper extends BaseDao<NewEmployee,String> {

    int insert(NewEmployee record);

    int updateByPrimaryKey(NewEmployee record);

    List<NewEmployee> selectByIds(List<String> ids);

    List<NewEmployee> selectByShopId(@Param("shopId") String shopId);

    List<NewEmployee> selectByBrandId(@Param("brandId") String brandId);

    NewEmployee selectByTelephone(@Param("telephone") String telePhone);
}
