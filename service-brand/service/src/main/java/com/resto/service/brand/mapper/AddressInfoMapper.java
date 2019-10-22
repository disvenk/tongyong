package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.AddressInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressInfoMapper  extends BaseDao<AddressInfo,String> {
	
	List<AddressInfo> selectByBrandId(@Param("brandId") String brandId);
	
}
