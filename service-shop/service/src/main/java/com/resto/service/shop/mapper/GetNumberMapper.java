package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.GetNumber;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GetNumberMapper extends BaseDao<GetNumber,String> {

    int insert(GetNumber getNumber);

    int updateByPrimaryKey(GetNumber getNumber);

    GetNumber getWaitInfoByCustomerId(@Param("customerId") String customerId, @Param("shopId") String shopId, @Param("timeOut") Integer timeOut);

}
