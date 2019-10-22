package com.resto.service.shop.service;

import com.resto.api.brand.define.api.BrandApiShopDetail;
import com.resto.api.brand.dto.ShopDetailDto;
import com.resto.conf.util.ApplicationUtils;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.PayMode;
import com.resto.service.shop.constant.WaitModerState;
import com.resto.service.shop.entity.GetNumber;
import com.resto.service.shop.entity.Order;
import com.resto.service.shop.entity.OrderPaymentItem;
import com.resto.service.shop.mapper.GetNumberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class GetNumberService extends BaseService<GetNumber, String> {

    @Autowired
    private GetNumberMapper getNumberMapper;
    @Autowired
    private OrderPaymentItemService orderPaymentItemService;
    @Autowired
    private BrandApiShopDetail shopDetailService;

    @Override
    public BaseDao<GetNumber, String> getDao() {
        return getNumberMapper;
    }

    public GetNumber getWaitInfoByCustomerId(String customerId,String shopId) {
        ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(shopId);

        return getNumberMapper.getWaitInfoByCustomerId(customerId,shopId,shopDetail.getTimeOut());
    }

    public void refundWaitMoney(Order order) {
        ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        GetNumber getNumber = getNumberMapper.getWaitInfoByCustomerId(order.getCustomerId(),order.getShopDetailId(),shopDetail.getTimeOut());
        getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_ONE);
        update(getNumber);

        OrderPaymentItem item = new OrderPaymentItem();
        item.setId(ApplicationUtils.randomUUID());
        item.setOrderId(order.getId());
        item.setPaymentModeId(PayMode.WAIT_MONEY);
        item.setPayTime(order.getCreateTime());
        item.setPayValue(getNumber.getFinalMoney());
        item.setRemark("退还等位红包:" + order.getWaitMoney());
        item.setResultData(getNumber.getId());
        orderPaymentItemService.insert(item);
    }

}
