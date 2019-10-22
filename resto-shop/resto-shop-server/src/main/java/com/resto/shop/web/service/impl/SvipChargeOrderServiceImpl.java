package com.resto.shop.web.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.shop.web.dao.SvipActivityMapper;
import com.resto.shop.web.dao.SvipChargeItemMapper;
import com.resto.shop.web.dao.SvipChargeOrderMapper;
import com.resto.shop.web.dao.SvipMapper;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.SvipChargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
@Service
@Component
public class SvipChargeOrderServiceImpl extends GenericServiceImpl<SvipChargeOrder, String> implements SvipChargeOrderService {

    @Resource
    private SvipChargeOrderMapper svipchargeorderMapper;

    @Autowired
    SvipActivityMapper svipActivityMapper;

    @Autowired
    SvipChargeItemMapper svipChargeItemMapper;

    @Autowired
    SvipMapper svipMapper;

    @Override
    public GenericDao<SvipChargeOrder, String> getDao() {
        return svipchargeorderMapper;
    }

    @Override
    public SvipChargeOrder createChargeOrder(String activityId, String customerId, String shopId, String brandId) {
        SvipActivity svipActivity = svipActivityMapper.selectByPrimaryKey(activityId);
        SvipChargeOrder svipChargeOrder = new SvipChargeOrder();
        svipChargeOrder.setId(ApplicationUtils.randomUUID());
        svipChargeOrder.setChargeMoney(svipActivity.getSvipPrice());
        svipChargeOrder.setOrderState(0);
        svipChargeOrder.setCreateTime(new Date());
        svipChargeOrder.setCustomerId(customerId);
        svipChargeOrder.setBrandId(brandId);
        svipChargeOrder.setShopDetailId(shopId);
        svipChargeOrder.setActivityId(activityId);
        svipChargeOrder.setCustomerId(customerId);

        Svip svip = new Svip();
        svip.setId(ApplicationUtils.randomUUID());
        svip.setChargeMoney(svipActivity.getSvipPrice());
        svip.setSvipExpire(svipActivity.getSvipExpire());
        svip.setShopDetailId(shopId);
        Date dataTime = new Date();
        if(svipActivity.getSvipExpireType()==0){
            svip.setSvipExpireType(svipActivity.getSvipExpireType());
            svip.setBeginDateTime(dataTime);
            svip.setEndDateTime(getDateAfter(dataTime, svipActivity.getSvipExpire()));
        }
        svip.setSvipExpireType(svipActivity.getSvipExpireType());

        svip.setActivityId(svipActivity.getId());
        svip.setCustomerId("666666");

        svipChargeOrder.setSvipId(svip.getId());

        svipMapper.insert(svip);
        svipchargeorderMapper.insert(svipChargeOrder);
        return svipChargeOrder;
    }

    @Override
    public void chargeorderWxPaySuccess(SvipChargeItem svipChargeItem) {
        SvipChargeOrder svipChargeOrder = svipchargeorderMapper.selectByPrimaryKey(svipChargeItem.getChargeOrderId());
        svipChargeOrder.setOrderState(1);
        svipChargeOrder.setFinishTime(new Date());

        Svip svip = svipMapper.selectByPrimaryKey(svipChargeOrder.getSvipId());
        svip.setCustomerId(svipChargeOrder.getCustomerId());
        svip.setBeSvipTime(new Date());

        svipchargeorderMapper.updateByPrimaryKeySelective(svipChargeOrder);
        svipMapper.updateByPrimaryKeySelective(svip);

        svipChargeItemMapper.insert(svipChargeItem);

    }

    public Date getDateAfter(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }
}
