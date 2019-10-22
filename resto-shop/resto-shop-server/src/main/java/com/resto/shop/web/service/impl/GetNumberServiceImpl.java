package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dto.RedPacketDto;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.constant.WaitModerState;
import com.resto.shop.web.dao.GetNumberMapper;
import com.resto.shop.web.model.GetNumber;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.OrderPaymentItem;
import com.resto.shop.web.producer.MQMessageProducer;
import com.resto.shop.web.report.GetNumberMapperReport;
import com.resto.shop.web.service.GetNumberService;
import com.resto.shop.web.service.OrderPaymentItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by carl on 2016/10/14.
 */
@Component
@Service
public class GetNumberServiceImpl extends GenericServiceImpl<GetNumber, String> implements GetNumberService {

    @Resource
    private GetNumberMapper getNumberMapper;

    @Resource
    private GetNumberMapperReport getNumberMapperReport;

    @Autowired
    private ShopDetailService shopDetailService;

    @Autowired
    MQMessageProducer mqMessageProducer;

    @Resource
    OrderPaymentItemService orderPaymentItemService;

    @Override
    public GenericDao<GetNumber, String> getDao() {
        return getNumberMapper;
    }

    @Override
    public List<GetNumber> selectByTableTypeShopId(String tableType, String shopId) {
        return getNumberMapper.selectByTableTypeShopId(tableType, shopId);
    }

    @Override
    public int getWaitNumber(GetNumber getNumber) {
        return getNumberMapper.getWaitNumber(getNumber);
    }

    @Override
    public Integer selectCount(String tableType, Date date, String shopId) {
        return getNumberMapper.selectCount(tableType, date, shopId).size();
    }

    @Override
    public GetNumber updateGetNumber(GetNumber getNumber, Integer state) {
        if (state == WaitModerState.WAIT_MODEL_NUMBER_ZERO) {
            getNumber.setCallNumberTime(new Date());
            //计算最终等位红包价格
            Long tempTime = (getNumber.getCallNumberTime().getTime() - getNumber.getCreateTime().getTime()) / 1000;  //等待的时间
            BigDecimal endMoney = getNumber.getFlowMoney().multiply(new BigDecimal(tempTime)).setScale(2, BigDecimal.ROUND_HALF_UP);             //最终价钱
            if (getNumber.getCallNumber() == 0) {
                if (endMoney.subtract(getNumber.getHighMoney()).doubleValue() > 0) {
                    getNumber.setFinalMoney(getNumber.getHighMoney());
                } else {
                    getNumber.setFinalMoney(endMoney);
                }
            }
            //其他修改
            getNumber.setCallNumber(getNumber.getCallNumber() + 1);

        } else if (state == WaitModerState.WAIT_MODEL_NUMBER_ONE) {
            getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_ONE);
            getNumber.setEatTime(new Date());
            //计算最终等位红包价格
            Long tempTime = (getNumber.getEatTime().getTime() - getNumber.getCreateTime().getTime()) / 1000;  //等待的时间
            BigDecimal endMoney = getNumber.getFlowMoney().multiply(new BigDecimal(tempTime)).setScale(2, BigDecimal.ROUND_HALF_UP);             //最终价钱

            if (getNumber.getCallNumberTime() == null) {
                if (endMoney.subtract(getNumber.getHighMoney()).doubleValue() > 0) {
                    getNumber.setFinalMoney(getNumber.getHighMoney());
                } else {
                    getNumber.setFinalMoney(endMoney);
                }
            }


        } else if (state == WaitModerState.WAIT_MODEL_NUMBER_TWO) {
            getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_TWO);
            getNumber.setPassNumberTime(new Date());
            //计算最终等位红包价格
            Long tempTime = (getNumber.getPassNumberTime().getTime() - getNumber.getCreateTime().getTime()) / 1000;  //等待的时间
            BigDecimal endMoney = getNumber.getFlowMoney().multiply(new BigDecimal(tempTime)).setScale(2, BigDecimal.ROUND_HALF_UP);             //最终价钱
            if (getNumber.getCallNumberTime() == null) {
                if (endMoney.subtract(getNumber.getHighMoney()).doubleValue() > 0) {
                    getNumber.setFinalMoney(getNumber.getHighMoney());
                } else {
                    getNumber.setFinalMoney(endMoney);
                }
            }
        }
        getNumberMapper.updateByPrimaryKeySelective(getNumber);
        return getNumber;
    }

    @Override
    public GetNumber getWaitInfoByCustomerId(String customerId, String shopId) {
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);

        return getNumberMapper.getWaitInfoByCustomerId(customerId, shopId, shopDetail.getTimeOut());
    }

    @Override
    public void refundWaitMoney(Order order) {
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(order.getShopDetailId());
        GetNumber getNumber = getNumberMapper.getWaitInfoByCustomerId(order.getCustomerId(), order.getShopDetailId(), shopDetail.getTimeOut());
        getNumber.setState(WaitModerState.WAIT_MODEL_NUMBER_ONE);
        update(getNumber);

        OrderPaymentItem item = new OrderPaymentItem();
        item.setId(ApplicationUtils.randomUUID());
        item.setOrderId(order.getId());
        item.setPaymentModeId(PayMode.WAIT_MONEY);
        item.setPayTime(order.getCreateTime());
        item.setPayValue(getNumber.getFinalMoney());
        item.setRemark("退还等位红包:" + order.getWaitMoney());
//        item.setResultData(getNumber.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
        item.setToPayId(getNumber.getId());
        orderPaymentItemService.insert(item);
    }

    @Override
    public List<RedPacketDto> selectGetNumberRed(Map<String, Object> selectMap) {
        return getNumberMapperReport.selectGetNumberRed(selectMap);
    }

    @Override
    public int insertGetNumber(GetNumber getNumber) {
        int count = getNumberMapper.insertSelective(getNumber);
        mqMessageProducer.sendQueueOrder(getNumber);
        return count;
    }

    @Override
    public GetNumber selectGetNumberInfo(String id) {
        return getNumberMapper.selectGetNumberInfo(id);
    }

    @Override
    public Integer selectWaitCountByCodeId(String shopId, String codeId) {
        return getNumberMapper.selectWaitCountByCodeId(shopId, codeId).size();
    }

    @Override
    public List<GetNumber> selectBeforeNumberByCodeId(String shopId, String codeId, Date time) {
        return getNumberMapper.selectBeforeNumberByCodeId(shopId, codeId, time);
    }

    @Override
    public List<GetNumber> selectAfterNumberByCodeId(String shopId, String codeId, Date time) {
        return getNumberMapper.selectAfterNumberByCodeId(shopId, codeId, time);
    }

    @Override
    public GetNumber selectNowNumberByCodeId(String shopId, String codeId) {
        return getNumberMapper.selectNowNumberByCodeId(shopId, codeId);
    }
}
