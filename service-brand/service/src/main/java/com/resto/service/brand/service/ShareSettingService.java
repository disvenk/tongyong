package com.resto.service.brand.service;


import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.ShareSetting;
import com.resto.service.brand.mapper.ShareSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ShareSettingService extends BaseService<ShareSetting, String> {

    @Autowired
    private ShareSettingMapper sharesettingMapper;

    public BaseDao<ShareSetting, String> getDao() {
        return sharesettingMapper;
    }

	public ShareSetting selectByBrandId(String currentBrandId) {
		ShareSetting shareSetting = sharesettingMapper.selectByBrandId(currentBrandId);
		return shareSetting;
	}
}
