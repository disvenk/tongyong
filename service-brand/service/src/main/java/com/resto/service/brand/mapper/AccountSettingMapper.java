package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.AccountSetting;

public interface AccountSettingMapper  extends BaseDao<AccountSetting,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(AccountSetting record);

    int insertSelective(AccountSetting record);

    AccountSetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountSetting record);

    int updateByPrimaryKey(AccountSetting record);

    /**
     * 通过品牌设置id查询 品牌账户设置
     * @param brandSettingId
     * @return
     */
    AccountSetting selectByBrandSettingId(String brandSettingId);
}
