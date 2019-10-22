package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.AccountNoticeMapper;
import com.resto.brand.web.model.AccountNotice;
import com.resto.brand.web.service.AccountNoticeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class AccountNoticeServiceImpl extends GenericServiceImpl<AccountNotice, Integer> implements AccountNoticeService {

    @Resource
    private AccountNoticeMapper accountnoticeMapper;

    @Override
    public GenericDao<AccountNotice, Integer> getDao() {
        return accountnoticeMapper;
    }

	@Override
	public void deleteByAccountId(Integer accountId) {
		accountnoticeMapper.deleteByAccountId(accountId);
	}

	@Override
	public List<AccountNotice> selectByAccountId(Integer accountId) {
		return accountnoticeMapper.selectByAccountId(accountId);
	}
}
