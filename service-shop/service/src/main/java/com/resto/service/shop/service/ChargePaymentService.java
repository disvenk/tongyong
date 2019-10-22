package com.resto.service.shop.service;

import com.resto.api.brand.dto.RechargeLogDto;
import com.resto.conf.util.DateUtil;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ChargePayment;
import com.resto.service.shop.mapper.ChargePaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChargePaymentService extends BaseService<ChargePayment, String> {

    @Autowired
    private ChargePaymentMapper chargepaymentMapper;

    @Override
    public BaseDao<ChargePayment, String> getDao() {
        return chargepaymentMapper;
    }

    public List<ChargePayment> selectPayList(String beginDate, String endDate) {
        //获取开始时间
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return chargepaymentMapper.selectPayList(begin,end);
    }

	public RechargeLogDto selectRechargeLog(String beginDate, String endDate, String brandId) {
		// TODO Auto-generated method stub
		Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return chargepaymentMapper.selectRechargeLog(begin,end,brandId);
	}

	public RechargeLogDto selectShopRechargeLog(String beginDate, String endDate, String shopId) {
		// TODO Auto-generated method stub
		Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return chargepaymentMapper.selectShopRechargeLog(begin,end,shopId);
	}

    public ChargePayment selectPayData(String shopId) {
        return chargepaymentMapper.selectPayData(shopId);
    }

    public ChargePayment selectByChargeOrderId(String chargeOrderId) {
        return chargepaymentMapper.selectByChargeOrderId(chargeOrderId);
    }
}
