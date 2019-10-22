package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.dto.RechargeLogDto;
import com.resto.shop.web.dao.ChargePaymentMapper;
import com.resto.shop.web.model.ChargePayment;
import com.resto.shop.web.report.ChargePaymentMapperReport;
import com.resto.shop.web.service.ChargePaymentService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class ChargePaymentServiceImpl extends GenericServiceImpl<ChargePayment, String> implements ChargePaymentService {

    @Resource
    private ChargePaymentMapper chargepaymentMapper;

    @Resource
    private ChargePaymentMapperReport chargePaymentMapperReport;

    @Override
    public GenericDao<ChargePayment, String> getDao() {
        return chargepaymentMapper;
    }

    @Override
    public List<ChargePayment> selectPayList(String beginDate, String endDate) {
        //获取开始时间
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return chargePaymentMapperReport.selectPayList(begin,end);
    }

	@Override
	public RechargeLogDto selectRechargeLog(String beginDate, String endDate, String brandId) {
		// TODO Auto-generated method stub
		Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return chargepaymentMapper.selectRechargeLog(begin,end,brandId);
	}

	@Override
	public RechargeLogDto selectShopRechargeLog(String beginDate, String endDate, String shopId) {
		// TODO Auto-generated method stub
		Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return chargepaymentMapper.selectShopRechargeLog(begin,end,shopId);
	}

    @Override
    public ChargePayment selectPayData(String shopId) {
        return chargepaymentMapper.selectPayData(shopId);
    }

    @Override
    public ChargePayment selectByChargeOrderId(String chargeOrderId) {
        return chargepaymentMapper.selectByChargeOrderId(chargeOrderId);
    }
}
