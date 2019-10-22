package com.resto.service.appraise.service.impl.service;

import com.resto.api.appraise.entity.Appraise;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.appraise.mapper.AppraiseMapper;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class AppraiseService extends BaseServiceResto<Appraise, AppraiseMapper> {
	
    @Resource
    private AppraiseMapper appraiseMapper;
	
	public Map<String, Object> appraiseCount( String currentShopId) {
		return appraiseMapper.appraiseCount(currentShopId);
	}
	
	public List<Map<String, Object>> appraiseMonthCount( String currentShopId) {
		return appraiseMapper.appraiseMonthCount(currentShopId);
	}
	
	public Appraise selectDetailedById( String appraiseId) {
		Appraise appraise = appraiseMapper.selectDetailedById(appraiseId);
		return appraise;
	}
	
	public Appraise selectDeatilByOrderId( String orderId, String customerId) {
		List<Appraise> apprises = appraiseMapper.selectDeatilByOrderId(orderId, customerId);
		if(apprises.size() > 0){
			return apprises.get(0);
		}else{
			return null;
		}
	}
	
	public Appraise selectAppraiseByCustomerId( String customerId, String shopId) {
		return appraiseMapper.selectAppraiseByCustomerId(customerId,shopId);
	}
	
	public List<Appraise> selectCustomerAllAppraise( String customerId, Integer currentPage, Integer showCount) {
		return appraiseMapper.selectCustomerAllAppraise(customerId, currentPage, showCount);
	}
	
	public int selectByCustomerCount( String customerId) {
		return appraiseMapper.selectByCustomerCount(customerId);
	}
	
	public List<Appraise> selectByGoodAppraise() {
		return appraiseMapper.selectByGoodAppraise();
	}
	
	public Map<String, Object> selectCustomerAppraiseAvg( String customerId) {
		return appraiseMapper.selectCustomerAppraiseAvg(customerId);
	}
	
	public List<Appraise> selectByTimeAndShopId( String shopId, String begin, String end) {
		return appraiseMapper.selectByTimeAndShopId(shopId, begin, end);
	}
	
	public List<Appraise> selectByTimeAndBrandId( Date begin, Date end) {
		return appraiseMapper.selectByTimeAndBrandId(begin,end);
	}
	
	public Appraise selectByOrderIdCustomerId( String orderId, String customerId) {
		return appraiseMapper.selectByOrderIdCustomerId(orderId, customerId);
	}
	
	public List<Appraise> selectAllAppraiseByShopIdAndCustomerId( String shopId, String customerId) {
		return appraiseMapper.selectAllAppraiseByShopIdAndCustomerId(shopId,customerId);
	}
	
	public List<Appraise> listAppraise( String currentShopId, Integer currentPage, Integer showCount, Integer maxLevel, Integer minLevel) {
		return appraiseMapper.listAppraise(currentShopId, currentPage, showCount, maxLevel, minLevel);
	}

}
