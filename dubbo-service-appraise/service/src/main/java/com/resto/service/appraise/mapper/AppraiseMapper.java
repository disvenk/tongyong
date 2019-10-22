package com.resto.service.appraise.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.api.appraise.entity.Appraise;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AppraiseMapper extends MyMapper<Appraise> {

    Appraise selectDetailedById(String appraiseId);
    
    List<Appraise> listAppraise(@Param(value = "currentShopId") String currentShopId, @Param(value = "currentPage") Integer currentPage, @Param(value = "showCount") Integer showCount, @Param(value = "maxLevel") Integer maxLevel, @Param(value = "minLevel") Integer minLevel);
    
    Map<String, Object> appraiseCount(@Param(value = "currentShopId") String currentShopId);
    
    List<Map<String, Object>> appraiseMonthCount(String currentShopId);

    List<Appraise> selectDeatilByOrderId(@Param("orderId") String orderId, @Param("customerId") String customerId);

    Appraise selectAppraiseByCustomerId(@Param("customerId") String customerId, @Param("shopId") String shopId);

    List<Appraise> selectCustomerAllAppraise(@Param(value = "customerId") String customerId, @Param(value = "currentPage") Integer currentPage, @Param(value = "showCount") Integer showCount);

    int selectByCustomerCount(String customerId);

    List<Appraise> selectByGoodAppraise();
    
    Map<String, Object> selectCustomerAppraiseAvg(@Param("customerId") String customerId);

    List<Appraise> selectByTimeAndShopId(@Param("shopId") String shopId, @Param("beginDate") String begin, @Param("endDate") String end);

	List<Appraise> selectByTimeAndBrandId(@Param("beginDate") Date begin, @Param("endDate") Date end);

    Appraise selectByOrderIdCustomerId(@Param("orderId") String orderId, @Param("customerId") String customerId);

    List<Appraise> selectAllAppraiseByShopIdAndCustomerId(@Param("shopId") String shopId, @Param("customerId") String customerId);
}
