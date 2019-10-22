package com.resto.brand.core.util;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * 计算订单金额的工具类
 */
public class OrderCountUtils {

    //定义支付项
    public static final int WEIXIN_PAY = 1;
    public static final int ACCOUNT_PAY = 2;
    public static final int COUPON_PAY = 3;
    public static final int MONEY_PAY = 4;
    public static final int BANK_CART_PAY = 5;
    public static final int CHARGE_PAY = 6;
    public static final int REWARD_PAY = 7;
    public static final int WAIT_MONEY = 8;
    public static final int HUNGER_MONEY = 9;
    public static final int ALI_PAY = 10;
    public static final int ARTICLE_BACK_PAY = 11;
    public static final int CRASH_PAY = 12;
    /**
     * 13 .14 .15 是由 2拆开出来的
     */
    public static final int APPRAISE_RED_PAY = 13;
    public static final int SHARE_RED_PAY = 14;
    public static final int REFUND_ARTICLE_RED_PAY = 15;

    //1.计算后格式化
    public static String formatDouble(double d) {
        DecimalFormat df = new DecimalFormat("#.00");
        if(d==0){
            return "0.00";
        }
        return df.format(d);
    }

    //2.计算订单金额( 主要是主订单和子订单的支付项在同一订单记录还是不同订单记录造成的)

    public static BigDecimal getOrderMoney(Integer payType, BigDecimal orderMoney, BigDecimal amountWithChildern) {
        BigDecimal temp = BigDecimal.ZERO;
            if (payType == 1 && amountWithChildern.compareTo(BigDecimal.ZERO) > 0) {//如果是后付款 并且子父订单总额大于0
                temp = temp.add(amountWithChildern);
            } else {
                temp = temp.add(orderMoney);
            }

        return temp;
    }
    //
    /**
     * 合并订单项中相同的payModeId
     * @param key
     * @param value
     */
    public static Map<Integer,BigDecimal> addKey(Integer key, BigDecimal value,Map<Integer,BigDecimal> payMap) {
        if(payMap.get(key)!=null){//说明map中已有改值
            payMap.put(key,payMap.get(key).add(value));
        }else {
            payMap.put(key,value);
        }
        return payMap;
    }

    public static   String getPayDetail (Map<Integer,BigDecimal> payMap){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Integer,BigDecimal> map:payMap.entrySet()){
            switch (map.getKey()){
                case WEIXIN_PAY:
                    sb.append("微信支付金额为"+map.getValue());
                    break;
                case ACCOUNT_PAY:
                    sb.append("红包(余额)支付金额为:"+map.getValue());
                    break;
                case COUPON_PAY:
                    sb.append("优惠券支付金额为:"+map.getValue());
                    break;
                case MONEY_PAY:
                    sb.append("其它方式支付金额为:"+map.getValue());
                    break;
                case BANK_CART_PAY:
                    sb.append("银行卡支付金额为:"+map.getValue());
                    break;
                case CHARGE_PAY:
                    sb.append("充值支付金额为:"+map.getValue());
                    break;
                case REWARD_PAY:
                    sb.append("充值返还支付金额为:"+map.getValue());
                    break;
                case WAIT_MONEY:
                    sb.append("等位红包的支付金额为:"+map.getValue());
                    break;
                case HUNGER_MONEY:
                    sb.append("饿了么支付金额为:"+map.getValue());
                    break;
                case ALI_PAY:
                    sb.append("支付宝支付金额为:"+map.getValue());
                    break;
                case ARTICLE_BACK_PAY:
                    sb.append("退菜红包支付金额为:"+map.getValue());
                    break;
                case CRASH_PAY:
                    sb.append("现金支付金额为:"+map.getValue());
                    break;
                default:
                    break;
            }
        }
        return  sb.toString();
    }

    /**
     * 子父订单都有 支付项 当为子订单时 时加 0 因为在父订单中已经做过累加了
     * @param parentOrderId
     * @param orderMoney
     * @param amountWithChildern
     * @return
     */
    public static BigDecimal getOrderMoney(String parentOrderId, BigDecimal orderMoney, BigDecimal amountWithChildern) {
        BigDecimal temp = BigDecimal.ZERO;
        //父订单
        if(parentOrderId == null){
            if(amountWithChildern.compareTo(BigDecimal.ZERO)>0){
                temp = amountWithChildern;
            }else {
                temp = orderMoney;
            }
        }else {
            //子订单
            temp = BigDecimal.ZERO;
        }

        return temp;
    }














}
