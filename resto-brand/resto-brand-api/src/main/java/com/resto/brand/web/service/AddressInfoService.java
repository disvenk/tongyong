package com.resto.brand.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.AddressInfo;

public interface AddressInfoService extends GenericService<AddressInfo, String> {
	
	List<AddressInfo> selectByBrandId(String brandId);
}
