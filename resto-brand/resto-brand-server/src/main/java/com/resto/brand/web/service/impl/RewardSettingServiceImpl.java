package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.enums.ModuleSign;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dao.RewardSettingMapper;
import com.resto.brand.web.model.RewardSetting;
import com.resto.brand.web.service.ModuleListService;
import com.resto.brand.web.service.RewardSettingService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;



/**
 *
 */
@Component
@Service
public class RewardSettingServiceImpl extends GenericServiceImpl<RewardSetting, String> implements RewardSettingService {

    @Resource
    private RewardSettingMapper rewardsettingMapper;
    
    @Resource
    ModuleListService moduleListService;

    @Override
    public GenericDao<RewardSetting, String> getDao() {
        return rewardsettingMapper;
    }

	@Override
	public void updateSetting(RewardSetting setting) {
		RewardSetting old = selectByBrandId(setting.getBrandId());
		if(old==null){
			setting.setId(ApplicationUtils.randomUUID());
			insert(setting);
		}else{
			setting.setId(old.getId());
			update(setting);
		}
		
	}

	@Override
	public RewardSetting selectByBrandId(String brandId) {
		
		return rewardsettingMapper.selectByBrandId(brandId);
	}

	@Override
	public RewardSetting selectValidSettingByBrandId(String brandId) {
		boolean hasModule = moduleListService.hasModule(ModuleSign.REWARD, brandId);
		if(hasModule){
			RewardSetting setting = selectByBrandId(brandId);
			if(setting!=null&&setting.getIsActivty()){
				return setting;
			}
		}
		return null;
	} 

}
