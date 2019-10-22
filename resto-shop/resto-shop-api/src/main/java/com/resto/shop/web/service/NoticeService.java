package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.Notice;

public interface NoticeService extends GenericService<Notice, String> {
	
	/**
	 * 根据店铺ID查询信息
	 * @param noticeType 
	 * @param customerId 
	 * @return
	 */
	List<Notice> selectListByShopId(String shopId, String customerId, Integer noticeType);
	
	/**
	 * 添加通知
	 * @param notice
	 */
    Notice create(Notice notice);

	void addNoticeHistory(String customerId, String noticeId);

	List<Notice> selectListAllByShopId(String currentShopId);

	void bindSupportTime(Notice notice);

	Integer[] getSupportTime(String noticeId);
}
