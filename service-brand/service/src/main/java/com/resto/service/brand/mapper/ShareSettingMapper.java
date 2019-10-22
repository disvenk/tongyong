package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.ShareSetting;

import java.util.List;

public interface ShareSettingMapper  extends BaseDao<ShareSetting,String> {
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
