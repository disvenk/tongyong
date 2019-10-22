package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper extends BaseDao<Notice,String> {

    int insert(Notice record);

    int updateByPrimaryKey(Notice record);

    List<Notice> selectListByShopId(@Param(value = "shopId") String currentShopId, @Param(value = "noticeType") Integer noticeType, @Param("customerId") String customerId);

    void addNoticeHistory(String customerId, String noticeId);

	List<Notice> selectListAllByShopId(String currentShopId);

	void clearSupportTime(String noticeId);

	void insertSupportTime(@Param("noticeId") String noticeId, @Param("time") Integer time);

	List<Integer> getSupportTime(String noticeId);
}
