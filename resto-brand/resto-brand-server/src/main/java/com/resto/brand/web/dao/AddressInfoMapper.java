package com.resto.brand.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.AddressInfo;

public interface AddressInfoMapper  extends GenericDao<AddressInfo,String> {
	
	List<AddressInfo> selectByBrandId(@Param("brandId")String brandId);
	
}
