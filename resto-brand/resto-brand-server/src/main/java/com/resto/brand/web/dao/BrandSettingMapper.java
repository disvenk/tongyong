package com.resto.brand.web.dao;

import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BrandSettingMapper  extends GenericDao<BrandSetting,String> {
    int deleteByPrimaryKey(String id);

    int insert(BrandSetting record);

    int insertSelective(BrandSetting record);

    BrandSetting selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BrandSetting record);

    int updateByPrimaryKeyWithBLOBs(BrandSetting record);

    int updateByPrimaryKey(BrandSetting record);

	BrandSetting selectByBrandId(String brandId);

    void updateWechatChargeConfig(@Param("settingId") String settingId,@Param("configId") String configId);

    BrandSetting selectByAppid(String appid);

    /**
     * 查询所有的开通第三方接口的品牌
     * @return
     */
    List<BrandSetting> selectListByState();

    BrandSetting posSelectByBrandId(@Param("brandId") String brandId);
}
