package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.AccountTicketMapper;
import com.resto.brand.web.model.AccountTicket;
import com.resto.brand.web.model.BrandAccount;
import com.resto.brand.web.service.AccountTicketService;
import com.resto.brand.web.service.BrandAccountService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class AccountTicketServiceImpl extends GenericServiceImpl<AccountTicket, Long> implements AccountTicketService {

    @Resource
    private AccountTicketMapper accountticketMapper;

    @Resource
	private BrandAccountService brandAccountService;

    @Override
    public GenericDao<AccountTicket, Long> getDao() {
        return accountticketMapper;
    }

	@Override
	public Result createAccountTicket(AccountTicket accountTicket) {
		Result result = new Result(true);
		BrandAccount brandAccount = brandAccountService.selectByBrandId(accountTicket.getBrandId());
		BigDecimal money = accountTicket.getMoney();
		BigDecimal remainerAmcount = BigDecimal.ZERO;
		if(brandAccount!=null){
			remainerAmcount = brandAccount.getRemainedInvoiceAmount();
		}
		if(remainerAmcount.compareTo(money)==1 || remainerAmcount.compareTo(money)==0){//判断可申请发票金额是否大于等于申请金额
			//开完这次票之后剩余可开票余额
			//brandAccount.setRemainedInvoiceAmount(remainerAmcount.subtract(money));
			//brandAccount.setUpdateTime(new Date());
			BigDecimal remain = remainerAmcount.subtract(money);
			Integer id = brandAccount.getId();
			BigDecimal usedTickt = brandAccount.getUsedInvoiceAmount().add(money);
			brandAccount = new BrandAccount();
			brandAccount.setUpdateTime(new Date());
			brandAccount.setId(id);
			brandAccount.setRemainedInvoiceAmount(remain);
			brandAccount.setUsedInvoiceAmount(usedTickt);
			brandAccountService.update(brandAccount);
			accountTicket.setAccountId(id);
			accountticketMapper.insertSelective(accountTicket);
		}else{
			result.setSuccess(false);
			result.setMessage("可申请金额为 "+remainerAmcount+" 元，小于当前申请金额，请调整申请的发票金额！");
		}
		return result;
	}

	@Override
	public List<AccountTicket> selectListByBrandId(String brandId) {
		return accountticketMapper.selectListByBrandId(brandId);
	}
}
