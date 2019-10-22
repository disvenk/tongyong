package com.resto.service.appraise.service.impl.service;

import com.resto.api.appraise.dto.NewAppraiseCustomerDto;
import com.resto.api.appraise.entity.AppraiseNew;
import com.resto.conf.mybatis.base.BaseService;
import com.resto.service.appraise.mapper.AppraiseNewMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class AppraiseNewService extends BaseService<AppraiseNew, AppraiseNewMapper>{

    @Resource
    private AppraiseNewMapper appraisenewMapper;
    
    public AppraiseNew selectByOrderIdCustomerId(String orderId, String customerId) {
        return appraisenewMapper.selectByOrderIdCustomerId(orderId,customerId);
    }
    
    public List<NewAppraiseCustomerDto> ListAppraiseCustomer(Integer currentPage, Integer showCount, Integer level, String shopId) {
        List<NewAppraiseCustomerDto> list=appraisenewMapper.ListAppraiseCustomer(currentPage, showCount,level,shopId);
        return list;
    }
    
    public List<NewAppraiseCustomerDto> ListAppraiseCustomerId(Integer currentPage, Integer showCount, String customerId, String shopId) {
        List<NewAppraiseCustomerDto> list=appraisenewMapper.ListAppraiseCustomerId(currentPage, showCount,customerId,shopId);
        return list;
    }
    
    public NewAppraiseCustomerDto selectByAppraiseId(String appraiseId) {
        NewAppraiseCustomerDto newAppraiseCustomerDto= appraisenewMapper.selectByAppraiseId(appraiseId);
        return newAppraiseCustomerDto;
    }
    
    public int selectByCustomerCount(String customerId) {
        return appraisenewMapper.selectByCustomerCount(customerId);
    }
    
    public Map<String, Object> appraiseCount(String currentShopId) {
        return appraisenewMapper.appraiseCount(currentShopId);
    }
    
    public List<Map<String, Object>> appraiseMonthCount(String currentShopId) {
        return appraisenewMapper.appraiseMonthCount(currentShopId);
    }
    
    public List<AppraiseNew> selectByTimeAndShopId(String shopId, String begin, String end) {
        return appraisenewMapper.selectByTimeAndShopId(shopId,begin,end);
    }
    
    public AppraiseNew add(AppraiseNew appraiseNew) {
        dbSave(appraiseNew);
        return appraiseNew;
    }
}
