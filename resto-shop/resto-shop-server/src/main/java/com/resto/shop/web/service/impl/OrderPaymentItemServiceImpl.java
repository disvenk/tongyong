package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.dto.IncomeReportDto;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.dao.OrderPaymentItemMapper;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.OrderPaymentItem;
import com.resto.shop.web.service.OrderPaymentItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
@Service
public class OrderPaymentItemServiceImpl extends GenericServiceImpl<OrderPaymentItem, String> implements OrderPaymentItemService {

    @Resource
    private OrderPaymentItemMapper orderpaymentitemMapper;


    @Override
    public GenericDao<OrderPaymentItem, String> getDao() {
        return orderpaymentitemMapper;
    }

    @Override
    public List<OrderPaymentItem> selectByOrderId(String orderId) {
        return orderpaymentitemMapper.selectByOrderId(orderId);
    }


    @Override
    public List<OrderPaymentItem> selectByOrderIdList(String orderId) {
        return orderpaymentitemMapper.selectByOrderIdList(orderId);
    }


    @Override
    public List<OrderPaymentItem> selectpaymentByPaymentMode(String shopId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        //查询订单支付记录
        List<OrderPaymentItem> list = orderpaymentitemMapper.selectpaymentByPaymentMode(shopId,begin,end);
        for(OrderPaymentItem item : list){
            item.setPaymentModeVal(PayMode.getPayModeName(item.getPaymentModeId()));
        }
        return orderpaymentitemMapper.selectpaymentByPaymentMode(shopId,begin,end);
    }

    @Override
    public List<IncomeReportDto> selectIncomeList(String brandId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end =  DateUtil.getformatEndDate(endDate);
        return orderpaymentitemMapper.selectIncomeList(brandId,begin,end);
    }

    @Override
    public List<OrderPaymentItem> selectListByShopId(String shopId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IncomeReportDto> selectIncomeListByShopId(String shopId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end =  DateUtil.getformatEndDate(endDate);
        return orderpaymentitemMapper.selectIncomeListByShopId(shopId,begin,end);
    }


    @Override
    public List<OrderPaymentItem> selectListByResultData(String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end =  DateUtil.getformatEndDate(endDate);
        return orderpaymentitemMapper.selectListByResultData(begin,end);
    }



    @Override
    public List<Order> selectOrderMoneyByBrandIdGroupByOrderId(String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end =  DateUtil.getformatEndDate(endDate);
        return orderpaymentitemMapper.selectOrderMoneyByBrandIdGroupByOrderId(begin,end);
    }

    @Override
    public List<OrderPaymentItem> selectDiscountList(String orderId) {
        return orderpaymentitemMapper.selectDiscountList(orderId);
    }

    @Override
    public List<Map<String, Object>> selectShopIncomeList(Map<String, Object> map) {
        return orderpaymentitemMapper.selectShopIncomeList(map);
    }

    @Override
    public List<OrderPaymentItem> selectShopIncomeListByShopId(String beginDate, String endDate, String shopId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end =  DateUtil.getformatEndDate(endDate);
        return orderpaymentitemMapper.selectShopIncomeListByShopId(begin,end,shopId);
    }

    @Override
    public BigDecimal sumOrderMoneyByOrderId(String orderId) {

        return orderpaymentitemMapper.sumOrderMoneyByOrderId(orderId);
    }

    @Override
    public List<OrderPaymentItem> selectRefund(String orderId, Date beginTime, Date endTime) {

        return orderpaymentitemMapper.selectRefund(orderId,beginTime,endTime);
    }

    @Override
    public List<OrderPaymentItem> selectListByResultDataByNoFile(String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end =  DateUtil.getformatEndDate(endDate);
        return orderpaymentitemMapper.selectListByResultDataByNoFile(begin,end);
    }

    @Override
    public OrderPaymentItem selectByOrderIdAndResultData(String orderId) {
        return orderpaymentitemMapper.selectByOrderIdAndResultData(orderId);
    }
    
    @Override
    public List<OrderPaymentItem> selectOrderPayMentItem(Map<String, String> map) {
    	return orderpaymentitemMapper.selectOrderPayMentItem(map);
    }
    
    @Override
    public List<OrderPaymentItem> selectPaymentCountByOrderId(String orderId) {
    	return orderpaymentitemMapper.selectPaymentCountByOrderId(orderId);
    }
    
    @Override
    public OrderPaymentItem selectPayMentSumByrefundOrder(String orderId) {
    	return null;
    }

    @Override
    public OrderPaymentItem selectByShanhuiPayOrder(String orderId) {
        return orderpaymentitemMapper.selectByShanhuiPayOrder(orderId);
    }

    @Override
    public void updateByShanhuiPayOrder(String orderId, String param) {
        orderpaymentitemMapper.updateByShanhuiPayOrder(orderId, param);
    }

    @Override
    public OrderPaymentItem insertByBeforePay(OrderPaymentItem orderPaymentItem) {
        return orderpaymentitemMapper.insertByBeforePay(orderPaymentItem);
    }

    @Override
    public OrderPaymentItem selectWeChatPayResultData(String shopId) {
        return orderpaymentitemMapper.selectWeChatPayResultData(shopId);
    }

    @Override
    public List<OrderPaymentItem> selectRefundPayMent(String orderId) {
        return orderpaymentitemMapper.selectRefundPayMent(orderId);
    }

    @Override
    public int deleteByOrderId(String orderId) {
        return orderpaymentitemMapper.deleteByOrderId(orderId);
    }

    @Override
    public void posSyncDeleteByOrderId(String orderId) {
        orderpaymentitemMapper.posSyncDeleteByOrderId(orderId);
    }

    @Override
    public List<OrderPaymentItem> posSyncListByOrderId(String orderId) {
        return orderpaymentitemMapper.posSyncListByOrderId(orderId);
    }

    @Override
    public void insertItems(List<OrderPaymentItem> orderPaymentItems) {
        if(orderPaymentItems != null && !orderPaymentItems.isEmpty() && orderPaymentItems.size() > 0){
            orderpaymentitemMapper.insertItems(orderPaymentItems);
        }
    }

    @Override
    public List<OrderPaymentItem> selectPayMentByPayMode(String orderId, Integer payMode, Integer type) {
        return orderpaymentitemMapper.selectPayMentByPayMode(orderId, payMode, type);
    }

    @Override
    public int insertMany(List<OrderPaymentItem> orderPaymentItems) {
            Map<String,List<OrderPaymentItem>> map = new HashMap<>();
            map.put("list", orderPaymentItems);
        return orderpaymentitemMapper.insertList(map);
    }

    @Override
    public int updatePayMentByCode(String code, String orderId) {
        return orderpaymentitemMapper.updatePayMentByCode(code,orderId);
    }

    @Override
    public void deletePaymentByToPayId(String toPayId) {
        orderpaymentitemMapper.deletePaymentByToPayId(toPayId);
    }

    @Override
    public BigDecimal selectEnableTicketMoney(String orderId){
        BigDecimal money = orderpaymentitemMapper.selectEnableTicketMoney(orderId);
        return money;
    }

    @Override
    public OrderPaymentItem selectUseProductCoupons(String orderId, String articleId) {
        return orderpaymentitemMapper.selectUseProductCoupons(orderId, articleId);
    }

    @Override
    public OrderPaymentItem selectByToPayIdpaymentModeId(Integer paymentModeId, String toPayId) {
        return orderpaymentitemMapper.selectByToPayIdpaymentModeId(paymentModeId, toPayId);
    }

    @Override
    public List<OrderPaymentItem> selectParentPaymentByOrderId(String orderId) {
        return orderpaymentitemMapper.selectParentPaymentByOrderId(orderId);
    }
}
