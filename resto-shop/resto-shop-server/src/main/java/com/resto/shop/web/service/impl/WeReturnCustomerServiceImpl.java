package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.shop.web.dao.WeReturnCustomerMapper;
import com.resto.shop.web.model.WeReturnCustomer;
import com.resto.shop.web.service.WeReturnCustomerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class WeReturnCustomerServiceImpl extends GenericServiceImpl<WeReturnCustomer, Long> implements WeReturnCustomerService {

    @Resource
    private WeReturnCustomerMapper wereturncustomerMapper;

    @Override
    public GenericDao<WeReturnCustomer, Long> getDao() {
        return wereturncustomerMapper;
    }

    @Override
    public List<WeReturnCustomer> selectByShopIdAndTime(String zuoriDay, String shopId) {
        Date beginDate = DateUtil.getformatBeginDate(zuoriDay);
        Date endDate = DateUtil.getformatEndDate(zuoriDay);
        return wereturncustomerMapper.selectByShopIdAndTime(shopId,beginDate,endDate);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        wereturncustomerMapper.deleteByIds(ids);
    }
}
