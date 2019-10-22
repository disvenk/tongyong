package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.dao.BrandAccountLogMapper;
import com.resto.brand.web.model.BrandAccount;
import com.resto.brand.web.model.BrandAccountLog;
import com.resto.brand.web.service.BrandAccountLogService;
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
public class BrandAccountLogServiceImpl extends GenericServiceImpl<BrandAccountLog, Long> implements BrandAccountLogService {

    @Resource
    private BrandAccountLogMapper brandaccountlogMapper;


    @Resource
	private BrandAccountService brandAccountService;



    @Override
    public GenericDao<BrandAccountLog, Long> getDao() {
        return brandaccountlogMapper;
    }


	@Override
	public List<BrandAccountLog> selectListByBrandId(String brandId) {
		return brandaccountlogMapper.selectListByBrandId(brandId);
	}

	@Override
	public void updateBrandAccountAndLog(BrandAccountLog brandAccountLog, Integer accountId, BigDecimal jifeiMoney) {
		//扣除计费的钱
		brandAccountService.updateBlance(jifeiMoney,accountId);
		//更新账户余额成功后再记录日志
		//查询出最新的账户情况
		BrandAccount brandAccount = brandAccountService.selectById(accountId);
		brandAccountLog.setRemain(brandAccount.getAccountBalance());
		//记录日志
		brandaccountlogMapper.insertSelective(brandAccountLog);

	}

	@Override
	public List<BrandAccountLog> selectListByBrandIdAndTime(String beginDate, String endDate, String brandId) {
		Date beginTime = DateUtil.getDateBegin(DateUtil.fomatDate(beginDate));
		Date endTime = DateUtil.getDateEnd(DateUtil.fomatDate(endDate));
		return  brandaccountlogMapper.selectListByBrandIdAndTime(beginTime,endTime,brandId);

	}

	@Override
	public BrandAccountLog selectOneBySerialNumAndBrandId(String serialNumber, String brandId) {
		return brandaccountlogMapper.selectOneBySerialNumAndBrandId(serialNumber,brandId);
	}
}
