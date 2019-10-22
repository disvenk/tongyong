package com.resto.shop.web.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.SmsLog;

public interface SmsLogService extends GenericService<SmsLog, Long> {


    /**
     * 根据店铺ID查询短信记录
     * @param shopId
     * @return
     */
	List<SmsLog> selectListByShopId(String shopId);

    /**
     * 根据店铺ID查询两天内的短信记录
     * @param shopId
     * @return
     */
	List<SmsLog> selectListByShopIdAndDate(String shopId);

    /**
     * 根据时间和店铺查询记录
     * @param begin
     * @param end
     * @param shopIds
     * @return
     */
	List<SmsLog> selectListWhere(String begin, String end,String shopIds);


	/**
	 * 根据品牌
	 * 查询全部短信记录
	 * @return
	 */
	List<SmsLog> selecByBrandId(String brandId);


    SmsLog selectByMap(Map<String, Object> selectMap);

	/**
	 * 短信发送功能
	 *
	 * 1.把发模板短信 和 验证码短信 放在一起  根据 smsType来判断发验证码 或者 是 模板短信
	 * jsonObject 取得数据就是 code 或者是发信息的参数
	 *
	 * 2.之所以把这个放在一起 是为了发结店短信或者其它的短信也存数据库里(不记录日志里) 和 方便做切面扣费
	 *
	 * @param brandId 品牌id
	 * @param shopId 店铺id
	 * @param smsType 短信类型 1.验证码 5结店短信
	 * @param sign 签名
	 * @param code_temp 模板id
	 * @param phone 手机号
	 * @param jsonObject 参数
	 * @return
	 */
	JSONObject sendMessage(String brandId, String shopId,int smsType, String sign, String code_temp,String phone,JSONObject jsonObject);

}
