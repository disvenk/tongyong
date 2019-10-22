package com.resto.shop.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.Notice;

public interface NoticeMapper  extends GenericDao<Notice,String> {
    int deleteByPrimaryKey(String id);

    int insert(Notice record);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);
    
    /**
     * 根据店铺ID查询信息
     * @param noticeType 
     * @param customerId 
     * @return
     */
    List<Notice> selectListByShopId(@Param(value = "shopId") String currentShopId,@Param(value = "noticeType")  Integer noticeType,@Param("customerId") String customerId);
    
    void addNoticeHistory(String customerId,String noticeId);

	List<Notice> selectListAllByShopId(String currentShopId);

	void clearSupportTime(String noticeId);

	void insertSupportTime(@Param("noticeId") String noticeId,@Param("time") Integer time);

	List<Integer> getSupportTime(String noticeId);
}
