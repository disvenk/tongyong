package com.resto.brand.web.dao;

import com.resto.brand.web.model.AccountNotice;
import com.resto.brand.core.generic.GenericDao;

import java.util.List;

public interface AccountNoticeMapper  extends GenericDao<AccountNotice,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountNotice record);

    int insertSelective(AccountNotice record);

    AccountNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountNotice record);

    int updateByPrimaryKey(AccountNotice record);

	void deleteByAccountId(Integer accountId);

	List<AccountNotice> selectByAccountId(Integer accountId);
}
