package com.resto.shop.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.SmsLog;

public interface SmsLogMapper  extends GenericDao<SmsLog,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(SmsLog record);

    int insertSelective(SmsLog record);

    SmsLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SmsLog record);

    int updateByPrimaryKey(SmsLog record);
    
    /**
     * 根据店铺ID查询短信记录
     * @param shopId
     * @return
     */
    List<SmsLog> selectListByShopId(@Param("shopId") String shopId);

	List<SmsLog> selectListByShopIdAndDate(@Param("shopId") String shopId,@Param("begin") Date begin);

	List<SmsLog> selectListByWhere(@Param("begin")Date beginDate,@Param("end")Date endDate,@Param("ids")String[] ids);
	
	
	/**
	 * 根据品牌id查询
	 * @param brandId
	 * @return
	 */
	List<SmsLog> selectListByBrandId(String brandId);

    SmsLog selectByMap(Map<String, Object> selectMap);
}
