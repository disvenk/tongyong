package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.AccountNotice;

import java.util.List;

public interface AccountNoticeMapper  extends BaseDao<AccountNotice,Integer> {

    int insert(AccountNotice record);

    int updateByPrimaryKey(AccountNotice record);

	void deleteByAccountId(Integer accountId);

	List<AccountNotice> selectByAccountId(Integer accountId);
}
