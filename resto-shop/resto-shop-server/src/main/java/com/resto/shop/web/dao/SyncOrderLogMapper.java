package com.resto.shop.web.dao;


import com.resto.shop.web.model.SyncOrderLog;

public interface SyncOrderLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SyncOrderLog record);

    int insertSelective(SyncOrderLog record);

    SyncOrderLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SyncOrderLog record);

}