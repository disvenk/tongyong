package com.resto.brand.web.dao;

import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.core.generic.GenericDao;

public interface WechatConfigMapper  extends GenericDao<WechatConfig,String> {
    int deleteByPrimaryKey(String id);

    int insert(WechatConfig record);

    int insertSelective(WechatConfig record);

    WechatConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WechatConfig record);

    int updateByPrimaryKey(WechatConfig record);

	WechatConfig selectByBrandId(String brandId);
}
