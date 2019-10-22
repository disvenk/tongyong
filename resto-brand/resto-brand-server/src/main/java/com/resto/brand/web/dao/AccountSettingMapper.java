package com.resto.brand.web.dao;

import com.resto.brand.web.model.AccountSetting;
import com.resto.brand.core.generic.GenericDao;

public interface AccountSettingMapper  extends GenericDao<AccountSetting,Long> {
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
