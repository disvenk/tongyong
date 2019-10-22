package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.AccountAddressInfo;

import java.util.List;

public interface AccountAddressInfoService extends GenericService<AccountAddressInfo, String> {

	List<AccountAddressInfo> selectByBrandId(String brandId);
    
}
