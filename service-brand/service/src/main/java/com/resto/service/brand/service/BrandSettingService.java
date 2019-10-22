package com.resto.service.brand.service;

import com.resto.conf.util.ApplicationUtils;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.Brand;
import com.resto.service.brand.entity.BrandSetting;
import com.resto.service.brand.mapper.BrandSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandSettingService extends BaseService<BrandSetting, String> {

	@Autowired
	private BrandService brandService;

    @Autowired
    private BrandSettingMapper brandsettingMapper;

    public BaseDao<BrandSetting, String> getDao() {
        return brandsettingMapper;
    }

	public BrandSetting selectByBrandId(String brandId) {
		BrandSetting setting = brandsettingMapper.selectByBrandId(brandId);
		if(setting==null){
			setting = new BrandSetting();
			setting.setId(ApplicationUtils.randomUUID());
			this.insert(setting);
			Brand brand = brandService.selectById(brandId);
			brand.setBrandSettingId(setting.getId());
			brandService.update(brand);
		}
		return setting;
	}
}
