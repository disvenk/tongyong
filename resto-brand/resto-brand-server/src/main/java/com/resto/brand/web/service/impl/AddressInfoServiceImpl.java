package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.AddressInfoMapper;
import com.resto.brand.web.model.AddressInfo;
import com.resto.brand.web.service.AddressInfoService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;



/**
 *
 */
@Component
@Service
public class AddressInfoServiceImpl extends GenericServiceImpl<AddressInfo, String> implements AddressInfoService {

	@Resource
	private AddressInfoMapper addressInfoMapper;

    @Override
    public GenericDao<AddressInfo, String> getDao() {
        return addressInfoMapper;
    }

	@Override
	public List<AddressInfo> selectByBrandId(String brandId) {
		return addressInfoMapper.selectByBrandId(brandId);
	}

    
}
