package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.enums.ModuleSign;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dao.ShareSettingMapper;
import com.resto.brand.web.model.ShareSetting;
import com.resto.brand.web.service.ModuleListService;
import com.resto.brand.web.service.ShareSettingService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;



/**
 *
 */
@Component
@Service
public class ShareSettingServiceImpl extends GenericServiceImpl<ShareSetting, String> implements ShareSettingService {

    @Resource
    private ShareSettingMapper sharesettingMapper;
    
    @Resource
    ModuleListService moduleListService;

    @Override
    public GenericDao<ShareSetting, String> getDao() {
        return sharesettingMapper;
    }

	@Override
	public List<ShareSetting> selectBrandShareSetting() {
		return sharesettingMapper.selectBrandShareSetting();
	}


	@Override
	public ShareSetting selectByBrandId(String currentBrandId) {
		ShareSetting shareSetting = sharesettingMapper.selectByBrandId(currentBrandId);
		return shareSetting;
	}

	@Override
	public void updateByBrandId(ShareSetting setting) {
		ShareSetting old = selectByBrandId(setting.getBrandId());
		if(old==null){
			setting.setId(ApplicationUtils.randomUUID());
			insert(setting);
		}else{
			setting.setId(old.getId());
			update(setting);
		}
	}

	@Override
	public ShareSetting selectValidSettingByBrandId(String brandId) {
		boolean hasModule = moduleListService.hasModule(ModuleSign.SHARE, brandId);
		if(hasModule){
			ShareSetting setting = selectByBrandId(brandId);
			if(setting!=null&&setting.getIsActivity()){
				return setting;
			}
		}
		return null;
	} 

}
