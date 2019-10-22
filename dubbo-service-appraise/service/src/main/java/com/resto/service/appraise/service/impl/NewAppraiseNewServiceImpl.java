package com.resto.service.appraise.service.impl;

import com.resto.api.appraise.dto.NewAppraiseCustomerDto;
import com.resto.api.appraise.service.NewAppraiseNewService;
import com.resto.api.appraise.entity.AppraiseNew;
import com.resto.service.appraise.service.impl.service.AppraiseNewService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
public class NewAppraiseNewServiceImpl implements NewAppraiseNewService {
    
    @Resource
    private AppraiseNewService appraiseNewService;

    @Override
    public int dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseNew appraiseNew) {
        return appraiseNewService.dbSave(appraiseNew);
    }

    @Override
    public int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody List<AppraiseNew> list) {
        return appraiseNewService.dbInsertList(list);
    }

    @Override
    public int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseNew appraiseNew) {
        return appraiseNewService.dbDelete(appraiseNew);
    }

    @Override
    public int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object var) {
        return appraiseNewService.dbDeleteByPrimaryKey(new Integer(var.toString()));
    }

    @Override
    public int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String var) {
        return appraiseNewService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseNew appraiseNew) {
        return appraiseNewService.dbUpdate(appraiseNew);
    }

    @Override
    public List<AppraiseNew> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseNew t) {
        return appraiseNewService.dbSelect(t);
    }

    @Override
    public List<AppraiseNew> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseNew t, Integer pageNum, Integer pageSize) {
        return appraiseNewService.dbSelectPage(t,pageNum,pageSize);
    }

    @Override
    public AppraiseNew dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Object key) {
        return appraiseNewService.dbSelectByPrimaryKey(key);
    }

    @Override
    public AppraiseNew dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseNew record) {
        return appraiseNewService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseNew record) {
        return appraiseNewService.dbSelectCount(record);
    }

    @Override
    public List<AppraiseNew> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String ids) {
        return appraiseNewService.dbSelectByIds(ids);
    }

    @Override
    public AppraiseNew selectByOrderIdCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String orderId, String customerId) {
        return appraiseNewService.selectByOrderIdCustomerId(orderId,customerId);
    }

    @Override
    public List<NewAppraiseCustomerDto> ListAppraiseCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Integer currentPage, Integer showCount, Integer level, String shopId) {
        List<NewAppraiseCustomerDto> list=appraiseNewService.ListAppraiseCustomer(currentPage, showCount,level,shopId);
        return list;
    }

    @Override
    public List<NewAppraiseCustomerDto> ListAppraiseCustomerId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, Integer currentPage, Integer showCount, String customerId, String shopId) {
        List<NewAppraiseCustomerDto> list=appraiseNewService.ListAppraiseCustomerId(currentPage, showCount,customerId,shopId);
        return list;
    }

    @Override
    public NewAppraiseCustomerDto selectByAppraiseId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String appraiseId) {
        NewAppraiseCustomerDto newAppraiseCustomerDto= appraiseNewService.selectByAppraiseId(appraiseId);
        return newAppraiseCustomerDto;
    }

    @Override
    public int selectByCustomerCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String customerId) {
        return appraiseNewService.selectByCustomerCount(customerId);
    }

    @Override
    public Map<String, Object> appraiseCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String currentShopId) {
        return appraiseNewService.appraiseCount(currentShopId);
    }

    @Override
    public List<Map<String, Object>> appraiseMonthCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String currentShopId) {
        return appraiseNewService.appraiseMonthCount(currentShopId);
    }

    @Override
    public List<AppraiseNew> selectByTimeAndShopId(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, String shopId, String begin, String end) {
        return appraiseNewService.selectByTimeAndShopId(shopId,begin,end);
    }

    @Override
    public AppraiseNew add(@ApiParam(value = "品牌id", required = true) @RequestParam(value = "brandId") String brandId, @RequestBody AppraiseNew appraiseNew) {
        appraiseNewService.dbSave(appraiseNew);
        return appraiseNew;
    }
}
