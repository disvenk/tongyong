package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.WechatConfig;

public interface WechatConfigMapper  extends BaseDao<WechatConfig,String> {
    int deleteByPrimaryKey(String id);

    int insert(WechatConfig record);

    int insertSelective(WechatConfig record);

    WechatConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WechatConfig record);

    int updateByPrimaryKey(WechatConfig record);

	WechatConfig selectByBrandId(String brandId);
}
