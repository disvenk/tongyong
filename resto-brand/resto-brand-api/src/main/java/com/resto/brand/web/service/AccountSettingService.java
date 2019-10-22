package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.AccountSetting;

public interface AccountSettingService extends GenericService<AccountSetting, Long> {

    AccountSetting selectByBrandSettingId(String brandSettingId);

    void insertAccountSetting(AccountSetting brand);

	void updateAccountSetting(AccountSetting brand);


}
