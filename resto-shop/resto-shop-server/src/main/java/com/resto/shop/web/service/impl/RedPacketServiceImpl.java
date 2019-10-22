package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dto.LogType;
import com.resto.brand.web.dto.RedPacketDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.WeChatService;
import com.resto.brand.web.service.WechatConfigService;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.constant.RedType;
import com.resto.shop.web.dao.RedPacketMapper;
import com.resto.shop.web.dto.ShareMoneyDto;
import com.resto.shop.web.model.*;
import com.resto.shop.web.report.RedPacketMapperReport;
import com.resto.shop.web.service.CustomerService;
import com.resto.shop.web.service.OrderPaymentItemService;
import com.resto.shop.web.service.RedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
public class RedPacketServiceImpl extends GenericServiceImpl<RedPacket, String> implements RedPacketService {

    @Resource
    private RedPacketMapper redPacketMapper;

    @Resource
    private RedPacketMapperReport redPacketMapperReport;

    @Resource
    private OrderPaymentItemService orderPaymentItemService;

    @Resource
    private AccountServiceImpl accountService;

    @Resource
    private CustomerService customerService;

    @Resource
    private BrandService brandService;

    @Resource
    private WechatConfigService wechatConfigService;

    @Autowired
    WeChatService weChatService;

    @Override
    public GenericDao<RedPacket, String> getDao() {
        return redPacketMapper;
    }

    @Override
    public void useRedPacketPay(BigDecimal redPay, String customerId, Order order, Brand brand, ShopDetail shopDetail) {
        //扣除红包，扣除顺序 评论红包-->分享红包-->退菜红包-->第三方储值余额-->消费返利余额   --2017-08-23新增第三方储值余额红包类型 auto by wtl-->2017-09-29新增红包消费返利余额类型 auto by wtl
        Integer[] redType = {0,1,2,3,4};
        for(Integer type : redType){
            redPay = useRedPacket(type,redPay,customerId,order,brand,shopDetail);
            //如果已扣完则不再扣除
            if(redPay.compareTo(BigDecimal.ZERO) == 0){
                break;
            }
        }
    }

    private BigDecimal useRedPacket(Integer redType, BigDecimal redPay, String customerId, Order order, Brand brand, ShopDetail shopDetail){
        //获得要扣除的红包
        RedPacket redPacket = redPacketMapper.selectFirstRedPacket(customerId,redType);
        if (redPacket == null && redPay.compareTo(BigDecimal.ZERO) > 0){
            return redPay;
        }
        //声明Map集合封装要修改的信息
        Map<String, Object> param = new HashMap<>();
        if(redPay.compareTo(redPacket.getRedRemainderMoney()) >= 0){
            param.put("id",redPacket.getId());
            param.put("redRemainderMoney",BigDecimal.ZERO);
            param.put("finishTime",new Date());
            redPacketMapper.updateRedRemainderMoney(param);
            OrderPaymentItem item = new OrderPaymentItem();
			item.setId(ApplicationUtils.randomUUID());
			item.setOrderId(order.getId());
            switch (redType){
                case RedType.APPRAISE_RED:
                    item.setPaymentModeId(PayMode.APPRAISE_RED_PAY);
                    break;
                case RedType.SHARE_RED:
                    item.setPaymentModeId(PayMode.SHARE_RED_PAY);
                    break;
                case RedType.REFUND_ARTICLE_RED:
                    item.setPaymentModeId(PayMode.REFUND_ARTICLE_RED_PAY);
                    break;
                case RedType.THIRD_MONEY:
                    item.setPaymentModeId(PayMode.THIRD_MONEY_RED_PAY);
                    break;
                case RedType.REBATE_MONEY:
                    item.setPaymentModeId(PayMode.REBATE_MONEY_RED_PAY);
                    break;
                 default:
                     break;
            }
			item.setPayTime(new Date());
			item.setPayValue(redPacket.getRedRemainderMoney());
			item.setRemark(RedType.GETREDTYPE.get(redType)+"支付:" + item.getPayValue());
//			item.setResultData(redPacket.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
            item.setToPayId(redPacket.getId());
			orderPaymentItemService.insert(item);
            redPay = redPay.subtract(redPacket.getRedRemainderMoney());
            if(redPay.compareTo(BigDecimal.ZERO) > 0) {
                return useRedPacket(redType, redPay, customerId, order, brand, shopDetail);
            }
        }else{
            param.put("id",redPacket.getId());
            param.put("redRemainderMoney",redPacket.getRedRemainderMoney().subtract(redPay));
            redPacketMapper.updateRedRemainderMoney(param);
            OrderPaymentItem item = new OrderPaymentItem();
            item.setId(ApplicationUtils.randomUUID());
            item.setOrderId(order.getId());
            switch (redType){
                case RedType.APPRAISE_RED:
                    item.setPaymentModeId(PayMode.APPRAISE_RED_PAY);
                    break;
                case RedType.SHARE_RED:
                    item.setPaymentModeId(PayMode.SHARE_RED_PAY);
                    break;
                case RedType.REFUND_ARTICLE_RED:
                    item.setPaymentModeId(PayMode.REFUND_ARTICLE_RED_PAY);
                    break;
                case RedType.THIRD_MONEY:
                    item.setPaymentModeId(PayMode.THIRD_MONEY_RED_PAY);
                    break;
                case RedType.REBATE_MONEY:
                    item.setPaymentModeId(PayMode.REBATE_MONEY_RED_PAY);
                    break;
                 default:
                     break;
            }
            item.setPayTime(new Date());
            item.setPayValue(redPay);
            item.setRemark(RedType.GETREDTYPE.get(redType)+"支付:" + item.getPayValue());
//            item.setResultData(redPacket.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
            item.setToPayId(redPacket.getId());
            orderPaymentItemService.insert(item);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<RedPacketDto> selectRedPacketLog(Map<String, Object> selectMap) {
        return redPacketMapperReport.selectRedPacketLog(selectMap);
    }

    @Override
    public Map<String, BigDecimal> selectUseRedOrder(Map<String, Object> selectMap) {
        return redPacketMapperReport.selectUseRedOrder(selectMap);
    }

    @Override
    public void refundRedPacket(BigDecimal payValue, String Id) {
        redPacketMapper.refundRedPacket(payValue,Id);
    }

    @Override
    public List<ShareMoneyDto> selectShareMoneyList(String customerId, Integer currentPage, Integer showCount) {
        return redPacketMapper.selectShareMoneyList(customerId, currentPage, showCount);
    }

    @Override
    public void fixErrorAppraiseRedMoney() {
        List<RedPacket> redPackets = redPacketMapper.selectByStateZero();
        for(RedPacket redPacket : redPackets){
            Customer customer = customerService.selectById(redPacket.getCustomerId());
            accountService.addAccount(redPacket.getRedMoney(),customer.getAccountId(), " 评论奖励红包:"+redPacket.getRedMoney(), AccountLog.APPRAISE_RED_PACKAGE,redPacket.getShopDetailId());
            RedPacket newRedPacket = new RedPacket();
            newRedPacket.setId(redPacket.getId());
            newRedPacket.setState(1);
            redPacketMapper.updateByPrimaryKeySelective(newRedPacket);
            sendShareGiveMoneyDelayMsg(redPacket, customer);
        }
    }

    private void sendShareGiveMoneyDelayMsg(RedPacket redPacket , Customer customer) {
        StringBuffer msg = new StringBuffer();
        Brand brand = brandService.selectById(redPacket.getBrandId());
        WechatConfig config = wechatConfigService.selectByBrandId(redPacket.getBrandId());
        msg.append("太好了！评论红包已经送达您的账户！");
        String jumpurl = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?dialog=myYue&subpage=my";
        msg.append("<a href='" + jumpurl + "'>前往查看</a>");
        weChatService.sendCustomerMsgASync(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
    }
}