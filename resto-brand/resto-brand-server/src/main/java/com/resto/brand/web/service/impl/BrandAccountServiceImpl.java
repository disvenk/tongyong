package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.BrandAccountMapper;
import com.resto.brand.web.model.BrandAccount;
import com.resto.brand.web.model.BrandAccountStream;
import com.resto.brand.web.service.BrandAccountService;
import com.resto.brand.web.service.BrandAccountStreamService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
@Component
@Service
public class BrandAccountServiceImpl extends GenericServiceImpl<BrandAccount, Integer> implements BrandAccountService {

    @Resource
    private BrandAccountMapper accountMapper;


    @Resource
	private BrandAccountStreamService brandAccountStreamService;

    @Override
    public GenericDao<BrandAccount, Integer> getDao() {
        return accountMapper;
    }

    @Override
    public BrandAccount selectByBrandId(String brandId) {
        return accountMapper.selectByBrandId(brandId);
    }

    @Override
    public BrandAccount selectByBrandSettingId(String brandSettingId) {
        return accountMapper.selectByBrandSettingId(brandSettingId);
    }

    @Override
    public int chargeAccount(String brandId, BigDecimal chargeMoney) {

        return accountMapper.chargeAccount(brandId,chargeMoney,new Date());
    }


	@Override
	public int updateBlance(BigDecimal payMoney, Integer id) {
		return accountMapper.updateBlance(id,payMoney,new Date());
	}



	@Override
	public void updateBrandAccountByManual(BrandAccount b, BigDecimal addAccount,BigDecimal totalAccount,String userName,String brandId) {
		//记录充值流水
		BrandAccountStream bs = new BrandAccountStream();
		bs.setAccountId(b.getId().longValue());
		bs.setAddAccount(addAccount);
		bs.setRemainAccount(b.getAccountBalance());
		bs.setTotalAccount(totalAccount);
		bs.setUserName(userName);
		bs.setBrandId(brandId);
		JSONObject json = new JSONObject();
		json.put("操作人",userName);
		json.put("充值前账户余额",totalAccount);
		json.put("充值的金额",addAccount);
		json.put("充值后的金额",b.getAccountBalance());
		bs.setRemark(JSONObject.toJSONString(json));
		accountMapper.updateByPrimaryKeySelective(b);//更新账户余额
		//记录手动充值流水
		brandAccountStreamService.insert(bs);

	}
}
