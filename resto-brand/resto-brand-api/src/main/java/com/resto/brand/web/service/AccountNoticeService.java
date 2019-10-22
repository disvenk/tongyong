package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.AccountNotice;

import java.util.List;

public interface AccountNoticeService extends GenericService<AccountNotice, Integer> {

	/**
	 * 删除品牌账户 短信提醒设置
	 * @param accountId
	 */
	void deleteByAccountId(Integer accountId);

	List<AccountNotice> selectByAccountId(Integer accountId);
}
