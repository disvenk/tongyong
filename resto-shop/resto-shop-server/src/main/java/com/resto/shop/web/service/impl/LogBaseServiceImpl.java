package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.ShopDetail;
import com.resto.shop.web.constant.LogBaseState;
import com.resto.shop.web.dao.LogBaseMapper;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.LogBaseService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by carl on 2016/11/14.
 */
@Component
@Service
public class LogBaseServiceImpl extends GenericServiceImpl<LogBase, String> implements LogBaseService {

    @Resource
    private LogBaseMapper logBaseMapper;

    @Override
    public GenericDao<LogBase, String> getDao() {
        return logBaseMapper;
    }

    @Override
    public void insertLogBaseInfoState(ShopDetail shopDetail, Customer customer, String desc, Integer type) {
        if(type == LogBaseState.INTO){
            intoLog(shopDetail, customer, desc);
        } else if (type == LogBaseState.REPLACE){
            replaceLog(shopDetail, customer, desc);
        } else if(type == LogBaseState.CHOICE_T){
            choiceTLog(shopDetail, customer, desc);
        } else if(type == LogBaseState.CANEL_T){
            canelTLog(shopDetail, customer, desc);
        } else if(type == LogBaseState.EMPTY){
            emtypLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.FAIL){
            failLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.CANCEL_ORDER){
            canelOrderLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.PAY){
            payLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.SCAN){
            scanLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.PRINT){
            printLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.APPRAISE){
            appraiseLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.SHARE){
            shareLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.PRINT_TICKET){
            printTicketLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.REGISTER){
            registerLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.FIRST_SHARE_PAY){
            firstSharePayLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.PRINT_KITCHEN){
            printKitchenLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.REFUSE_ORDER){
            refuseOrderLog(shopDetail, customer, desc);
        }else if(type == LogBaseState.WX_PAY){
            wxPayLog(shopDetail, customer, desc);
        }
    }

    @Override
    public void insertLogBaseInfoState(ShopDetail shopDetail, Customer customer, ChargeOrder chargeOrder, Integer type) {
        if(type == LogBaseState.WX_RECHANGE){
            WXRechangeLog(shopDetail, customer, chargeOrder);
        }
    }

    @Override
    public void insertLogBaseInfoState(ShopDetail shopDetail, Customer customer, Article article, Integer type, Integer number) {
        if(type == LogBaseState.CHOICE_D){
            choiceDLog(shopDetail, customer, article, number);
        }
    }

    @Override
    public void insertLogBaseInfoState(ShopDetail shopDetail, Customer customer, Order order, Integer type) {
        if(type == LogBaseState.BUY){
            buyLog(shopDetail, customer, order);
        }else if(type == LogBaseState.BUY_PAY){
            buyPayLog(shopDetail, customer, order);
        }else if(type == LogBaseState.BUY_SCAN){
            buyScanLog(shopDetail, customer, order);
        }else if(type == LogBaseState.BUY_SCAN_PAY){
            buyScanPayLog(shopDetail, customer, order);
        }else if(type == LogBaseState.BUY_ADD){
            buyAddLog(shopDetail, customer, order);
        }else if(type == LogBaseState.NOT_PAYMENT_ORDER){
            notPaymentOrderLog(shopDetail, customer, order);
        }
    }

    //当用户进入店铺是记录log
    public void intoLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        if("scan".equals(desc)){
            logBase.setRemark(customer.getNickname()+"扫码进入了店铺");
        }else{
            logBase.setRemark(customer.getNickname()+"进入了店铺");
        }
        logBase.setDesc("当前店铺为"+shopDetail.getName());
        insert(logBase);
    }

    //当用户切换店铺的时候记录log
    public void replaceLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"切换了店铺");
        logBase.setDesc("当前店铺为"+shopDetail.getName());
        insert(logBase);
    }

    //当用户选择单品的时候记录log
    public void choiceDLog(ShopDetail shopDetail, Customer customer, Article article, Integer number){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        if(number > 0){
            logBase.setRemark(customer.getNickname()+"添加了"+number+"份"+article.getName()+"的单品");
        }else if(number == 0){
            logBase.setRemark(customer.getNickname()+"撤销了1份"+article.getName()+"的单品");
        }else {
            logBase.setRemark(customer.getNickname()+"撤销了"+Math.abs(number)+"份"+article.getName()+"的单品");
        }
        logBase.setDesc(article.getId());
        insert(logBase);
    }

    //当用户添加套餐的时候记录log
    public void choiceTLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"添加了套餐");
        logBase.setDesc(desc);
        insert(logBase);
    }

    //当用户撤销套餐的时候记录log
    public void canelTLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"撤销了套餐");
        logBase.setDesc("ArticleId为："+desc+" 套餐被撤销");
        insert(logBase);
    }

    //当用户清空购物车的时候记录log
    public void emtypLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"清空了购物车");
        logBase.setDesc(desc);
        insert(logBase);
    }

    //当用户下单未付款的时候记录log
    public void buyLog(ShopDetail shopDetail, Customer customer, Order order){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"未扫码下单了未付款(1,0)");
        logBase.setDesc(order.getId());
        insert(logBase);
    }

    //当用户下单已付款的时候记录log
    public void buyPayLog(ShopDetail shopDetail, Customer customer, Order order){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"未扫码下单了已付款(2,0)");
        logBase.setDesc(order.getId());
        insert(logBase);
    }

    //当用户扫码下单未付款的时候记录log
    public void buyScanLog(ShopDetail shopDetail, Customer customer, Order order){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"先扫码进入后下单了未付款(1,1)");
        logBase.setDesc(order.getId());
        insert(logBase);
    }

    //当用户扫码下单已付款的时候记录log
    public void buyScanPayLog(ShopDetail shopDetail, Customer customer, Order order){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"先扫码进入后下单了已付款(2,1)");
        logBase.setDesc(order.getId());
        insert(logBase);
    }

    //当用户下单失败的时候记录log
    public void failLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+desc);
        insert(logBase);
    }

    //当用户加菜的时候记录log
    public void buyAddLog(ShopDetail shopDetail, Customer customer, Order order){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"加菜下单成功");
        logBase.setDesc(order.getId());
        insert(logBase);
    }

    //用户取消订单时记录log
    public void canelOrderLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"取消了一份订单");
        logBase.setDesc("OrderId为："+desc+" 的订单被取消");
        insert(logBase);
    }

    //用户买单的时记录log
    public void payLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"支付了一份订单(o:2)");
        logBase.setDesc("OrderId为："+desc+" 的订单被支付了");
        insert(logBase);
    }

    //用户扫码时记录log
    public void scanLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"推送了一份订单(p:1)");
        logBase.setDesc("OrderId为："+desc+" 的订单被扫码了");
        insert(logBase);
    }

    //用户order订单打印时记录log
    public void printLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"的订单已打印");
        logBase.setDesc(desc);
        insert(logBase);
    }

    //用户评价订单时记录log
    public void appraiseLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"评论了一份订单");
        logBase.setDesc("评论的AppraiseId为："+desc);
        insert(logBase);
    }

    //用户收到分享推送时记录log
    public void shareLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"收到了评论分享的推送");
        logBase.setDesc("AppraiseId为："+desc+" 的评论已推送");
        insert(logBase);
    }

    //打印总单的时记录log
    public void printTicketLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"打印了总单");
        logBase.setDesc(desc);
        insert(logBase);
    }

    //用户注册时记录log
    public void registerLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"用户注册了会员");
        logBase.setDesc(desc);
        insert(logBase);
    }

    //用户是分享注册用户且首单时记录log
    public void firstSharePayLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"首次买单");
        logBase.setDesc(desc);
        insert(logBase);
    }

    //用户微信充值时记录log
    public void WXRechangeLog(ShopDetail shopDetail, Customer customer, ChargeOrder chargeOrder){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"进行微信充值");
        logBase.setDesc(chargeOrder.getId());
        insert(logBase);
    }

    //厨打时记录log
    public void printKitchenLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"厨打订单");
        logBase.setDesc(desc);
        insert(logBase);
    }

    //商家拒绝订单时记录log
    public void refuseOrderLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"的订单被商家拒绝了");
        logBase.setDesc(desc);
        insert(logBase);
    }

    //订单有WX支付时记录log
    public void wxPayLog(ShopDetail shopDetail, Customer customer, String desc){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"的订单微信支付完成");
        logBase.setDesc(desc);
        insert(logBase);
    }

    //用户未支付该订单时记录log
    public void  notPaymentOrderLog(ShopDetail shopDetail, Customer customer, Order order){
        LogBase logBase = new LogBase();
        GeneralRecord(logBase, shopDetail, customer);
        logBase.setRemark(customer.getNickname()+"未支付该订单");
        logBase.setDesc(order.getId());
        insert(logBase);
    }

    //通用添加用户店铺时间信息
    public void GeneralRecord(LogBase logBase, ShopDetail shopDetail, Customer customer){
        logBase.setId(ApplicationUtils.randomUUID());
        logBase.setShopId(shopDetail.getId());
        logBase.setShopName(shopDetail.getName());
        logBase.setCustomerId(customer.getId());
        logBase.setNickname(customer.getNickname());
        logBase.setCreateTime(new Date());
    }
}
