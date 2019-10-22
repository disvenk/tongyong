package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.Appraise;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AppraiseMapper extends BaseDao<Appraise,String> {

    int insert(Appraise record);

    int updateByPrimaryKey(Appraise record);
    
    List<Appraise> listAppraise(@Param(value = "currentShopId") String currentShopId, @Param(value = "currentPage") Integer currentPage, @Param(value = "showCount") Integer showCount, @Param(value = "maxLevel") Integer maxLevel, @Param(value = "minLevel") Integer minLevel);

    Map<String, Object> appraiseCount(@Param(value = "currentShopId") String currentShopId);

    List<Map<String, Object>> appraiseMonthCount(String currentShopId);

	Appraise selectDetailedById(String appraiseId);

    List<Appraise> selectDeatilByOrderId(String orderId);

    Appraise selectAppraiseByCustomerId(@Param("customerId") String customerId, @Param("shopId") String shopId);

    List<Appraise> selectCustomerAllAppraise(@Param(value = "customerId") String customerId, @Param(value = "currentPage") Integer currentPage, @Param(value = "showCount") Integer showCount);

    int selectByCustomerCount(String customerId);

    List<Appraise> selectByGoodAppraise();

    Map<String, Object> selectCustomerAppraiseAvg(@Param("customerId") String customerId);

    List<Appraise> selectByTimeAndShopId(@Param("shopId") String shopId, @Param("beginDate") Date begin, @Param("endDate") Date end);

	List<Appraise> selectByTimeAndBrandId(@Param("beginDate") Date begin, @Param("endDate") Date end);
}
