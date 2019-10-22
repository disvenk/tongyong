package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.BrandSetting;

import java.util.List;

public interface BrandSettingService extends GenericService<BrandSetting, String> {

	void insertSelective(BrandSetting brandSetting);

	BrandSetting selectByBrandId(String brandId);

	void updateWechatChargeConfig(String settingId,String configId);

    //通过第三方接口的appid
    BrandSetting selectByAppid(String appid);

    //查询所有正常使用的品牌的所有第三方接口
    List<BrandSetting>  selectListByState();

    /**
     * 更新品牌设置
     * @param bs
     */
    void updateBrandSetting(BrandSetting bs);

    /**
     * 根据 brandId 查询 品牌设置
     * @param brandId
     * @return
     */
    BrandSetting posSelectByBrandId(String brandId);

}
