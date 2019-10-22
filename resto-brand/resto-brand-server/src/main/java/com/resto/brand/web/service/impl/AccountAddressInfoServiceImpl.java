package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.AccountAddressInfoMapper;
import com.resto.brand.web.model.AccountAddressInfo;
import com.resto.brand.web.service.AccountAddressInfoService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class AccountAddressInfoServiceImpl extends GenericServiceImpl<AccountAddressInfo, String> implements AccountAddressInfoService {

    @Resource
    private AccountAddressInfoMapper accountaddressinfoMapper;

    @Override
    public GenericDao<AccountAddressInfo, String> getDao() {
        return accountaddressinfoMapper;
    }

	@Override
	public List<AccountAddressInfo> selectByBrandId(String brandId) {
		return accountaddressinfoMapper.selectByBrandId(brandId);
	}
}
