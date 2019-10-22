package com.resto.service.appraise.service.impl;

import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.api.appraise.entity.Appraise;
import com.resto.service.appraise.service.impl.service.AppraiseService;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
public class NewAppraiseServiceImpl implements NewAppraiseService {

	Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private AppraiseService appraiseService;

	@Override
	public Appraise dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody Appraise appraise) {
		appraiseService.dbSave(appraise);
		return appraise;
	}

	@Override
	public int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody List<Appraise> list) {
		return appraiseService.dbInsertList(list);
	}

	@Override
	public int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody Appraise appraise) {
		return appraiseService.dbDelete(appraise);
	}

	@Override
	public int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object var) {
		return appraiseService.dbDeleteByPrimaryKey(var.toString());
	}

	@Override
	public int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String var) {
		return appraiseService.dbDeleteByIds(var);
	}

	@Override
	public int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody Appraise appraise) {
		return appraiseService.dbUpdate(appraise);
	}

	@Override
	public List<Appraise> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody Appraise t) {
		return appraiseService.dbSelect(t);
	}

	@Override
	public List<Appraise> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody Appraise t, Integer pageNum, Integer pageSize) {
		return appraiseService.dbSelectPage(t,pageNum,pageSize);
	}

	@Override
	public Appraise dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object key) {
		return appraiseService.dbSelectByPrimaryKey(key);
	}

	@Override
	public Appraise dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody Appraise record) {
		return appraiseService.dbSelectOne(record);
	}

	@Override
	public int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody Appraise record) {
		return appraiseService.dbSelectCount(record);
	}

	@Override
	public List<Appraise> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String ids) {
		return appraiseService.dbSelectByIds(ids);
	}

	@Override
	public Map<String, Object> appraiseCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String currentShopId) {
		return appraiseService.appraiseCount(currentShopId);
	}

	@Override
	public List<Map<String, Object>> appraiseMonthCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String currentShopId) {
		return appraiseService.appraiseMonthCount(currentShopId);
	}

	@Override
	public Appraise selectDetailedById(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String appraiseId) {
		Appraise appraise = appraiseService.selectDetailedById(appraiseId);
		return appraise;
	}

	@Override
	public Appraise selectDeatilByOrderId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String orderId, String customerId) {
		return appraiseService.selectDeatilByOrderId(orderId, customerId);
	}

	@Override
	public Appraise selectAppraiseByCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String customerId, String shopId) {
		return appraiseService.selectAppraiseByCustomerId(customerId,shopId);
	}

	@Override
	public List<Appraise> selectCustomerAllAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String customerId, Integer currentPage, Integer showCount) {
		return appraiseService.selectCustomerAllAppraise(customerId, currentPage, showCount);
	}

	@Override
	public int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String customerId) {
		return appraiseService.selectByCustomerCount(customerId);
	}

	@Override
	public List<Appraise> selectByGoodAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId) {
		return appraiseService.selectByGoodAppraise();
	}

	@Override
	public Map<String, Object> selectCustomerAppraiseAvg(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String customerId) {
		return appraiseService.selectCustomerAppraiseAvg(customerId);
	}

	@Override
	public List<Appraise> selectByTimeAndShopId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String shopId, String begin, String end) {
		return appraiseService.selectByTimeAndShopId(shopId, begin, end);
	}

	@Override
	public List<Appraise> selectByTimeAndBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Date begin, Date end) {
		return appraiseService.selectByTimeAndBrandId(begin,end);
	}

	@Override
	public Appraise selectByOrderIdCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String orderId, String customerId) {
		return appraiseService.selectByOrderIdCustomerId(orderId, customerId);
	}

	@Override
	public List<Appraise> selectAllAppraiseByShopIdAndCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String shopId, String customerId) {
		return appraiseService.selectAllAppraiseByShopIdAndCustomerId(shopId,customerId);
	}

	@Override
	public List<Appraise> listAppraise(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String currentShopId, Integer currentPage, Integer showCount, Integer maxLevel, Integer minLevel) {
		return appraiseService.listAppraise(currentShopId, currentPage, showCount, maxLevel, minLevel);
	}

}
