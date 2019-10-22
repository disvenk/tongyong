package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.alipay.config.AlipayConfig;
import com.resto.brand.core.enums.ChargeOrderStatus;
import com.resto.brand.core.enums.ChargePayType;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.dao.SmsAcountMapper;
import com.resto.brand.web.dao.SmsChargeOrderMapper;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.model.SmsAcount;
import com.resto.brand.web.model.SmsChargeOrder;
import com.resto.brand.web.service.BrandUserService;
import com.resto.brand.web.service.SmsChargeOrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Component
@Service
public class SmsChargeServiceImpl extends GenericServiceImpl<SmsChargeOrder, String> implements SmsChargeOrderService {

	@Resource
	SmsChargeOrderMapper smsChargeOrderMapper;

	@Resource
	private SmsAcountMapper smsAcountMapper;
	
	@Resource
	private BrandUserService brandUserService;
	

	@Override
	public GenericDao<SmsChargeOrder, String> getDao() {
		return smsChargeOrderMapper;
	}

	@Override
	public List<SmsChargeOrder> selectByBrandId(String brandId) {
		return smsChargeOrderMapper.selectByBrandId(brandId);
	}


	@Override
	public SmsChargeOrder saveSmsOrder(String brandId,String total_fee) {
		BigDecimal chargeMoney = new BigDecimal(total_fee).setScale(2, BigDecimal.ROUND_HALF_DOWN);//保留两位小数，不进行四舍五入操作
		SmsChargeOrder smsChargeOrder = new SmsChargeOrder();
		smsChargeOrder.setId(ApplicationUtils.randomUUID());
		smsChargeOrder.setBrandId(brandId);
		smsChargeOrder.setChargeMoney(chargeMoney);
		smsChargeOrder.setCreateTime(new Date());
		BigDecimal smsUnitPrice = smsAcountMapper.selectSmsUnitPriceByBrandId(brandId);
		smsChargeOrder.setSmsUnitPrice(smsUnitPrice);
		Integer number = chargeMoney.divide(smsUnitPrice, 0, BigDecimal.ROUND_HALF_UP).intValue();// 四舍五入---> 计算应该充值的短信数量
		smsChargeOrder.setNumber(number);
		smsChargeOrder.setOrderStatus(ChargeOrderStatus.NO_PAY);
		smsChargeOrderMapper.insertSelective(smsChargeOrder);
		return smsChargeOrder;
	}

	@Override
	public boolean checkSmsChargeOrder_AliPay(Map<String,String> resultMap) {
		boolean flag = false;
		SmsChargeOrder smsChargeOrder = smsChargeOrderMapper.selectByPrimaryKey(resultMap.get("out_trade_no"));
		smsChargeOrder.setRemark(appendResult(smsChargeOrder.getRemark(), resultMap, "支付宝", "支付成功"));
		BigDecimal chargeMoney = smsChargeOrder.getChargeMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);//得到两位小数，不进行四舍五入操作
		BigDecimal totalFee = new BigDecimal(resultMap.get("total_fee")).setScale(2, BigDecimal.ROUND_HALF_DOWN);
		log.info("支付宝：返回的的  支付金额为："+totalFee+"  --->   数据库储存的应支付金额为："+chargeMoney);
		// 判断当前订单是否与创建的订单信息匹配
		if (chargeMoney.equals(totalFee) && AlipayConfig.seller_id.equals(resultMap.get("seller_id"))
				&& ChargeOrderStatus.NO_PAY == smsChargeOrder.getOrderStatus()) {
			flag = updateSmsOrder(smsChargeOrder, resultMap.get("trade_no"), ChargePayType.ALI_PAY);
		}
		return flag;
	}
	
	@Override
	public boolean checkSmsChargeOrder_WxPay(Map<String,String> resultMap) {
		boolean flag = false;
		SmsChargeOrder smsChargeOrder = smsChargeOrderMapper.selectByPrimaryKey(resultMap.get("out_trade_no"));
		smsChargeOrder.setRemark(appendResult(smsChargeOrder.getRemark(), resultMap, "微信支付", "支付成功"));
		BigDecimal chargeMoney = smsChargeOrder.getChargeMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);//得到两位小数，不进行四舍五入操作
		//微信金额的单位是 分，比较时，需要除以一百
		BigDecimal totalFee = new BigDecimal(resultMap.get("total_fee")).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN);
		log.info("微信：返回的的  支付金额为："+totalFee+"  --->   数据库储存的应支付金额为："+chargeMoney);
		// 判断当前订单是否与创建的订单信息匹配
		if (chargeMoney.equals(totalFee) && ChargeOrderStatus.NO_PAY == smsChargeOrder.getOrderStatus()) {
			flag = updateSmsOrder(smsChargeOrder, resultMap.get("transaction_id"), ChargePayType.WECHAT_PAY);
		}
		return flag;
	}
	
	//更新数据库中的短信充值订单
	public boolean updateSmsOrder(SmsChargeOrder smsChargeOrder,String trade_no,int payType){
		boolean flag = false;
		int row = smsAcountMapper.chargeSms(smsChargeOrder.getBrandId(), smsChargeOrder.getNumber(),smsChargeOrder.getChargeMoney());
		if (row > 0) {//判断是否成功执行充值操作
			smsChargeOrder.setOrderStatus(ChargeOrderStatus.HAS_PAY);
			smsChargeOrder.setPushOrderTime(new Date());
			smsChargeOrder.setTradeNo(trade_no);
			smsChargeOrder.setPayType(payType);
			smsChargeOrderMapper.updateByPrimaryKeySelective(smsChargeOrder);
			flag = true;
			//更新短信后给商家发短信
			//查询该账户目前剩余短信数量和已使用短信数量
			String brandId = smsChargeOrder.getBrandId();
			SmsAcount smsAcount = smsAcountMapper.selectByBrandId(brandId);
			//给谁发短信 查询是品牌管理员的phone
			BrandUser brandUser = brandUserService.selectOneByBrandId(brandId);
			//调用发短信的接口
			//String data = "{'name':'" + brandName + "','time':'" + date + "','pay':'" + chargeMoney + "','nm1':'" + remainderNum + "','nm2':'" + usedNum + "'}";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name",brandUser.getBrandName());
			jsonObject.put("time",DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			jsonObject.put("pay",smsChargeOrder.getChargeMoney());
			jsonObject.put("nm1",smsAcount.getRemainderNum());
			jsonObject.put("nm2",smsAcount.getUsedNum());
			//模板id 是返回
			SMSUtils.sendMessage(brandUser.getPhone(),jsonObject,SMSUtils.SIGN,SMSUtils.SMS_CHARGE_SUCCESS);
		}else{
			smsChargeOrder.setOrderStatus(ChargeOrderStatus.NO_CHARGE);
			StringBuffer str = new StringBuffer(smsChargeOrder.getRemark());
			str.append(DateUtil.getTime()+"执行充值语句影响了 "+row+" 行");
			smsChargeOrder.setRemark(str.toString());
			smsChargeOrderMapper.updateByPrimaryKeySelective(smsChargeOrder);
		}
		return flag;
	}


	@Override
	public List<SmsChargeOrder> selectOtherPay() {
		return smsChargeOrderMapper.selectOtherPay();
	}

	@Override
	public void paySuccess(SmsChargeOrder smsChargeOrder,String brandId) {
		//更新短信订单
		smsChargeOrder.setOrderStatus(ChargeOrderStatus.HAS_PAY);
		smsChargeOrder.setPushOrderTime(new Date());
		String remark = smsChargeOrder.getRemark();
		StringBuilder sremark = new StringBuilder(remark!=null?remark:"");
		sremark.append("{操作人:"+brandId+"}");
		smsChargeOrder.setRemark(sremark.toString()); 
		//更新订单
		smsChargeOrderMapper.updateByPrimaryKeySelective(smsChargeOrder);
		//充值
		int num = smsAcountMapper.chargeSms(smsChargeOrder.getBrandId(), smsChargeOrder.getNumber(), smsChargeOrder.getChargeMoney());
		if(num>0){
			//获取商家管理员电话
			BrandUser brandUser = brandUserService.selectOneByBrandId(smsChargeOrder.getBrandId());
			//获取短信账户的剩余短信数量和已使用短信数量
			SmsAcount smsAcount = smsAcountMapper.selectByBrandId(smsChargeOrder.getBrandId());
			//发短信告知商家有关信息
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name",brandUser.getBrandName());
			jsonObject.put("time",DateUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
			jsonObject.put("pay",smsChargeOrder.getChargeMoney());
			jsonObject.put("nm1",smsAcount.getRemainderNum());
			jsonObject.put("nm2",smsAcount.getUsedNum());
			//return sendMessage(phone, jsonObject, SIGN, SMS_CHARGE_SUCCESS);

			SMSUtils.sendMessage(brandUser.getPhone(),jsonObject,SMSUtils.SIGN,SMSUtils.SMS_CHARGE_SUCCESS);
		}
	}

	@Override
	public boolean saveSmsOrderByBank(String brandId, String serialNumber) {
		SmsChargeOrder smsChargeOrder = new SmsChargeOrder();
		smsChargeOrder.setId(ApplicationUtils.randomUUID());
		smsChargeOrder.setBrandId(brandId);
		smsChargeOrder.setCreateTime(new Date());
		BigDecimal smsUnitPrice = smsAcountMapper.selectSmsUnitPriceByBrandId(brandId);
		smsChargeOrder.setSmsUnitPrice(smsUnitPrice);
		smsChargeOrder.setOrderStatus(ChargeOrderStatus.AUDIT);
		smsChargeOrder.setPayType(ChargePayType.BANCARD_PAY);
		smsChargeOrder.setTradeNo(serialNumber);//保存输入的 银行转账流水号
		int row = smsChargeOrderMapper.insertSelective(smsChargeOrder);
		return row>0?true:false;
	}

	@Override
	public List<SmsChargeOrder> selectListData() {
		return smsChargeOrderMapper.selectListData();
	}

	@Override
	public void saveResultParam(Map<String, String> resultMap,String payType) {
		SmsChargeOrder smsChargeOrder = smsChargeOrderMapper.selectByPrimaryKey(resultMap.get("out_trade_no"));
		smsChargeOrder.setRemark(appendResult(smsChargeOrder.getRemark(), resultMap, payType, "支付失败"));
		smsChargeOrderMapper.updateByPrimaryKeySelective(smsChargeOrder);
	}

	public  String appendResult(String oldRemark, Map<String, String> resultMap, String payType, String payResult){
		StringBuilder remark = new StringBuilder(oldRemark!=null?oldRemark:"");
		remark.append("{\"支付类型\":\""+payType+"\",\"操作时间\":\""+ DateUtil.getTime()+"\",\"支付结果\":\""+payResult+"\",\"返回参数\":\""+ JSONObject.toJSONString(resultMap)+"\"};");
		return remark.toString();
	}

}
