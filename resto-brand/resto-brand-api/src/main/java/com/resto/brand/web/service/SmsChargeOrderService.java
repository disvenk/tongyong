package com.resto.brand.web.service;

import java.util.List;
import java.util.Map;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.SmsChargeOrder;


public interface SmsChargeOrderService extends GenericService<SmsChargeOrder, String> {
	/**
	 * 根据品牌ID 查询短信充值订单
	 * @param brandId
	 * @return
	 */
	List<SmsChargeOrder> selectByBrandId(String brandId);
	
	/**
	 * 保存充值订单
	 * @param brandId
	 * @param total_fee
	 * @return
	 */
	public SmsChargeOrder saveSmsOrder(String brandId,String total_fee);
	
	/**
	 * 保存通过银行卡转账的短信充值订单
	 * @param brandId
	 * @param serialNumber
	 * @return
	 */
	public boolean saveSmsOrderByBank(String brandId,String serialNumber);
	
	/**
	 * 支付宝异步通知时，检查订单信息和完善充值操作
	 * @param out_trade_no		商家的订单ID
	 * @param trade_no			支付宝支付的订单ID
	 * @param total_fee			总支付金额
	 * @param seller_id			收款人ID，验证是否为餐加
	 * @param aliPayResult		支付宝支付返回的所有参数
	 * @return 
	 * 	返回 true，则表示订单匹配，完成充值订单，
	 * 	返回false，则表示订单金额不匹配，不进行充值操作
	
	 * @return
	 */
	public boolean checkSmsChargeOrder_AliPay(Map<String,String> resultMap);
	
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
	public boolean checkSmsChargeOrder_WxPay(Map<String,String> resultMap);

	List<SmsChargeOrder> selectOtherPay();

	/**
	 * 确认银行卡充值的订单
	 * @param id
	 */
	void paySuccess(SmsChargeOrder smsChargeOrder,String brandId);

	List<SmsChargeOrder> selectListData();
	
	/**
	 * 当支付验证失败时，保存第三方支付返回的所有参数
	 * @param resultMap	第三方返回的参数
	 * @param payType	支付方式
	 */
	void saveResultParam(Map<String,String> resultMap,String payType);
}
