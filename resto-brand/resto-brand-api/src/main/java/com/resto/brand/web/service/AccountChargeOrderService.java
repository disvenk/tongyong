package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.AccountChargeOrder;

import java.util.List;
import java.util.Map;

public interface AccountChargeOrderService extends GenericService<AccountChargeOrder, String> {

    AccountChargeOrder saveChargeOrder(String brandId,String total_fee);

    /**
     *
     * @param resultMap
     * @return
     *
	 * 支付宝异步通知时，检查订单信息和完善充值操作
	 * @param out_trade_no		商家的订单ID
	 * @param trade_no			支付宝支付的订单ID
	 * @param total_fee			总支付金额
	 * @param seller_id			收款人ID，验证是否为餐加
	 * @param aliPayResult		支付宝支付返回的所有参数
	 * @return
	 * 	返回 true，则表示订单匹配，完成充值订单，
	 * 	返回false，则表示订单金额不匹配，不进行充值操作
     *
     */
     boolean checkAccountChargeOrder_AliPay(Map<String,String> resultMap);


	/**
	 * 微信异步通知时，检查订单信息和完善充值操作
	 * @param out_trade_no		商家的订单ID
	 * @param trade_no			微信支付的订单ID
	 * @param total_fee			总支付金额
	 * @param wxPayResult		微信支付返回的所有参数
	 * @return
	 * 	返回 true，则表示订单匹配，完成充值订单，
	 * 	返回false，则表示订单金额不匹配，不进行充值操作
	 */
	boolean checAccountChargeOrder_WxPay(Map<String,String>resultMap);


	/**
	 * 当支付验证失败时，保存第三方支付返回的所有参数
	 * @param resultMap	第三方返回的参数
	 * @param payType	支付方式
	 */
	void saveResultParam(Map<String,String> resultMap,String payType);


    /**
     * 查询已支付 且正常的订单
     * @return
     */
    List<AccountChargeOrder> selectHasPayList();

    List<AccountChargeOrder> selectHasPayListByBrandId(String brandId);


    List<AccountChargeOrder> selectListByBrandIdAndTime(String beginTime,String endTime,String brandId);

}
