package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.SmsAcountMapper;
import com.resto.brand.web.dao.SmsTicketMapper;
import com.resto.brand.web.model.SmsTicket;
import com.resto.brand.web.service.SmsTicketService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;



/**
 *
 */
@Component
@Service
public class SmsTicketServiceImpl extends GenericServiceImpl<SmsTicket, String> implements SmsTicketService {
	
	@Resource
	private SmsTicketMapper smsTicketMapper;
	
	@Resource
	private SmsAcountMapper smsAcountMapper;
	@Override
	public GenericDao<SmsTicket, String> getDao() {
		return smsTicketMapper;
	}

	@Override
	public List<SmsTicket> selectTicketlist() {
		return smsTicketMapper.selectList();
	}

	@Override
	public List<SmsTicket> selectByBrandId(String brandId) {
		return smsTicketMapper.selectByBrandId(brandId);
	}
	
	

	@Override
	public Result createInvoice(SmsTicket smsTicket) {
		Result result = new Result(true);
		BigDecimal remainerAmcount = smsAcountMapper.selectInvoiceMoney(smsTicket.getBrandId());
		BigDecimal money = smsTicket.getMoney();
		if(remainerAmcount.compareTo(money)==1 || remainerAmcount.compareTo(money)==0){//判断可申请发票金额是否大于等于申请金额
			smsAcountMapper.updateRemainerAmcount(smsTicket.getBrandId(), money);
			smsTicketMapper.insertSelective(smsTicket);
		}else{
			result.setSuccess(false);
			result.setMessage("可申请金额为 "+remainerAmcount+" 元，小于当前申请金额，请调整申请的发票金额！");
		}
		return result;
	}

}
