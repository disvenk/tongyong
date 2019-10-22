package com.resto.brand.web.dao;

import com.resto.brand.web.model.ShareSetting;

import java.util.List;

import com.resto.brand.core.generic.GenericDao;

public interface ShareSettingMapper  extends GenericDao<ShareSetting,String> {
    int deleteByPrimaryKey(String id);

    int insert(ShareSetting record);

    int insertSelective(ShareSetting record);

    ShareSetting selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShareSetting record);

    int updateByPrimaryKeyWithBLOBs(ShareSetting record);

    int updateByPrimaryKey(ShareSetting record);

	List<ShareSetting> selectBrandShareSetting();

	void changeOpen(String id, boolean is_open);

	ShareSetting selectByBrandId(String brand_id);

	void changeCloseTime(String id);

	void changeOpenTime(String id);
}
