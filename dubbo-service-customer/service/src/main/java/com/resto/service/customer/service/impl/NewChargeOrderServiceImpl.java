package com.resto.service.customer.service.impl;

import com.resto.api.customer.entity.ChargeOrder;
import com.resto.api.customer.service.NewChargeOrderService;
import com.resto.service.customer.service.impl.service.ChargeOrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-10-30 17:49
 */

@RestController
public class NewChargeOrderServiceImpl implements NewChargeOrderService{

    @Resource
    ChargeOrderService chargeOrderService;

    @Override
    public ChargeOrder dbSave(String brandId, ChargeOrder chargeOrder) {
        chargeOrderService.dbSave(chargeOrder);
        return chargeOrder;
    }

    @Override
    public int dbInsertList(String brandId, List<ChargeOrder> list) {
        return chargeOrderService.dbInsertList(list);
    }

    @Override
    public int dbDelete(String brandId, ChargeOrder chargeOrder) {
        return chargeOrderService.dbDelete(chargeOrder);
    }

    @Override
    public int dbDeleteByPrimaryKey(String brandId, Object var) {
        return chargeOrderService.dbDeleteByPrimaryKey(var.toString());
    }

    @Override
    public int dbDeleteByIds(String brandId, String var) {
        return chargeOrderService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(String brandId, ChargeOrder chargeOrder) {
        return chargeOrderService.dbUpdate(chargeOrder);
    }

    @Override
    public List<ChargeOrder> dbSelect(String brandId, ChargeOrder t) {
        return chargeOrderService.dbSelect(t);
    }

    @Override
    public List<ChargeOrder> dbSelectPage(String brandId, ChargeOrder t, Integer pageNum, Integer pageSize) {
        return chargeOrderService.dbSelectPage(t,pageNum ,pageSize );
    }

    @Override
    public ChargeOrder dbSelectByPrimaryKey(String brandId, Object key) {
        return chargeOrderService.dbSelectByPrimaryKey(key);
    }

    @Override
    public ChargeOrder dbSelectOne(String brandId, ChargeOrder record) {
        return chargeOrderService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(String brandId, ChargeOrder record) {
        return chargeOrderService.dbSelectCount(record);
    }

    @Override
    public List<ChargeOrder> dbSelectByIds(String brandId, String ids) {
        return chargeOrderService.dbSelectByIds(ids);
    }

    @Override
    public void refundCharge(String brandId, BigDecimal payValue, String id, String shopDetailId) {
        chargeOrderService.refundCharge(payValue,id ,shopDetailId );
    }

    @Override
    public void refundMoney(String brandId, BigDecimal charge, BigDecimal reward, String id, String shopDetailId) {
        chargeOrderService.refundMoney(charge,reward ,id , shopDetailId);
    }

    @Override
    public void refundReward(String brandId, BigDecimal payValue, String id, String shopDetailId) {
        chargeOrderService.refundReward(payValue,id ,shopDetailId );
    }

    @Override
    public List<ChargeOrder> selectByDateAndShopId(String brandId, String beginDate, String endDate, String shopId) {
        return chargeOrderService.selectByDateAndShopId(beginDate,endDate ,shopId );
    }

    @Override
    public List<ChargeOrder> selectByDateAndBrandId(String brandId, String beginDate, String endDate) {
        return chargeOrderService.selectByDateAndBrandId(beginDate,endDate ,brandId );
    }

    @Override
    public List<Map<String, Object>> selectByShopToDay(String brandId, Map<String, Object> selectMap) {
        return chargeOrderService.selectByShopToDay(selectMap);
    }

    @Override
    public List<ChargeOrder> selectListByDateAndShopId(String brandId, String zuoriDay, String zuoriDay1, String id) {
        return chargeOrderService.selectListByDateAndShopId(zuoriDay, zuoriDay1,id );
    }

    @Override
    public List<ChargeOrder> selectByCustomerIdAndBrandId(String brandId, String customerId) {
        return chargeOrderService.selectByCustomerIdAndBrandId(customerId,brandId );
    }

    @Override
    public List<Map<String, Object>> getChargeSumInfo(String brandId, Map<String, Object> selectMap) {
        return chargeOrderService.getChargeSumInfo(selectMap);
    }

    @Override
    public List<String> selectCustomerChargeOrder(String brandId, List<String> customerIds) {
        return chargeOrderService.selectCustomerChargeOrder(customerIds);
    }

    @Override
    public BigDecimal selectTotalBalanceByTimeAndShopId(String brandId, Date monthBegin, Date monthEnd, String shopId) {
        return chargeOrderService.selectTotalBalanceByTimeAndShopId(monthBegin,monthEnd ,shopId );
    }

    @Override
    public List<ChargeOrder> selectRemainderReturn(String brandId) {
        return chargeOrderService.selectRemainderReturn();
    }

    @Override
    public void updateRemainderReturn(String brandId, ChargeOrder chargeOrder) {
        chargeOrderService.updateRemainderReturn(chargeOrder);
    }

    @Override
    public BigDecimal selectByCustomerIdNotChangeId(String brandId, String customerId) {
        return chargeOrderService.selectByCustomerIdNotChangeId(customerId);
    }
}
