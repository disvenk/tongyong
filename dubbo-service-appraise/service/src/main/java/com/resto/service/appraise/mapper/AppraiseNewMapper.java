package com.resto.service.appraise.mapper;

import com.resto.api.appraise.dto.NewAppraiseCustomerDto;
import com.resto.conf.mybatis.util.MyMapper;
import com.resto.api.appraise.entity.AppraiseNew;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppraiseNewMapper  extends MyMapper<AppraiseNew> {

    List<NewAppraiseCustomerDto> ListAppraiseCustomer(@Param("currentPage") Integer currentPage, @Param("showCount") Integer showCount, @Param("level") Integer level, @Param("shopId") String shopId);

    List<NewAppraiseCustomerDto> ListAppraiseCustomerId(@Param("currentPage") Integer currentPage, @Param("showCount") Integer showCount, @Param("customerId") String customerId, @Param("shopId") String shopId);

    AppraiseNew selectByOrderIdCustomerId(@Param("orderId") String orderId, @Param("customerId") String customerId);

    int selectByCustomerCount(@Param("customerId") String customerId);

    NewAppraiseCustomerDto selectByAppraiseId(@Param("appraiseId") String appraiseId);

    Map<String, Object> appraiseCount(@Param(value = "currentShopId") String currentShopId);

    List<Map<String, Object>> appraiseMonthCount(@Param(value = "currentShopId") String currentShopId);

    List<AppraiseNew> selectByTimeAndShopId(@Param("shopId") String shopId, @Param("beginDate") String begin, @Param("endDate") String end);
}
