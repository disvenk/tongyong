package com.resto.shop.web.dao;

import com.resto.shop.web.model.StoreOperationsLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StoreOperationsLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(StoreOperationsLog record);

    int insertSelective(StoreOperationsLog record);

    StoreOperationsLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StoreOperationsLog record);

    int updateByPrimaryKey(StoreOperationsLog record);

    List<StoreOperationsLog> selectListByShopIdAndTime(@Param("shopId") String shopId,@Param("logType") Integer logType, @Param("staTime")Date staTime,@Param("endTime") Date endTime);

//    List<StoreOperationsLog> selectStoreOperationsLogIdByTime(@Param("staTime")Date staTime,@Param("endTime") Date endTime,@Param("dailyLogId") String dailyLogId);
}