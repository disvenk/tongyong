package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.entity.MyComaparator;
import com.resto.brand.core.enums.BrandAccountNoticeType;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.dao.AccountSettingMapper;
import com.resto.brand.web.model.AccountNotice;
import com.resto.brand.web.model.AccountSetting;
import com.resto.brand.web.model.BrandAccount;
import com.resto.brand.web.service.AccountNoticeService;
import com.resto.brand.web.service.AccountSettingService;
import com.resto.brand.web.service.BrandAccountService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class AccountSettingServiceImpl extends GenericServiceImpl<AccountSetting, Long> implements AccountSettingService {

    @Resource
    private AccountSettingMapper accountsettingMapper;

    @Resource
    private BrandAccountService accountService;


    @Resource

	private AccountNoticeService accountNoticeService;

    @Override
    public GenericDao<AccountSetting, Long> getDao() {
        return accountsettingMapper;
    }

    @Override
    public AccountSetting selectByBrandSettingId(String brandSettingId) {
        AccountSetting accountSetting = accountsettingMapper.selectByBrandSettingId(brandSettingId);
		List<AccountNotice> accountNotices = new ArrayList<>();
		if(accountSetting!=null){
			accountNotices	=	accountNoticeService.selectByAccountId(accountSetting.getAccountId());
		}
		if(null!=accountNotices&&!accountNotices.isEmpty()){
			List<Integer> list = new ArrayList<>();
			for(AccountNotice accountNotice:accountNotices){
				list.add(accountNotice.getNoticePrice().intValue());
			}
			Object[] objs = list.toArray();
			Arrays.sort(objs,new MyComaparator());
			//拼接返回的账户提醒设置
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<objs.length;i++){
				if(i==objs.length-1){
					sb.append(objs[i]);
				}else {
					sb.append(objs[i]).append(",");
				}
			}
			String str = sb.toString();
			accountSetting.setRemainAccount(sb.toString());
		}
		return accountSetting;

    }


    @Override
    public void insertAccountSetting(AccountSetting at) {
        //根据品牌设置id 查询品牌账户
        BrandAccount account = accountService.selectByBrandSettingId(at.getBrandSettingId());
        if(null!=account){
           at.setAccountId(account.getId());
        }
        at.setCreateTime(new Date());
        at.setUpdateTime(at.getCreateTime());
		String remainSms = at.getRemainAccount();
		remainSms = remainSms.replaceAll("，",",");//防止输入中文的，
		//删除原有的短信提醒设置
		accountNoticeService.deleteByAccountId(account.getId());
		//插入提醒
		String[] arr = remainSms.split(",");
		for(String str:arr){
			AccountNotice accountNotice = new AccountNotice();
			accountNotice.setAccountId(account.getId());
			BigDecimal price = new BigDecimal(str);
			if(price.compareTo(BigDecimal.ZERO)>0){
				accountNotice.setType(BrandAccountNoticeType.NOT_ENOUGH);
			}else {
				accountNotice.setType(BrandAccountNoticeType.ARREARS);
			}
			accountNotice.setNoticePrice(price);
			accountNoticeService.insert(accountNotice);

		}

        accountsettingMapper.insertSelective(at);

    }

	@Override
	public void updateAccountSetting(AccountSetting at) {
		//根据品牌设置id 查询品牌账户
		BrandAccount account = accountService.selectByBrandSettingId(at.getBrandSettingId());
		String remainSms = at.getRemainAccount();
		//防止输入中文的，
		remainSms = remainSms.replaceAll("，",",");
		//删除原有的短信提醒设置
		accountNoticeService.deleteByAccountId(account.getId());
		//插入提醒

		if(StringUtils.isNotEmpty(remainSms)){
			String[] arr = remainSms.split(",");
			for(String str:arr){
				AccountNotice accountNotice = new AccountNotice();
				accountNotice.setAccountId(account.getId());
				BigDecimal price = new BigDecimal(str);
				if(price.compareTo(BigDecimal.ZERO)>0){
					accountNotice.setType(BrandAccountNoticeType.NOT_ENOUGH);
				}else {
					accountNotice.setType(BrandAccountNoticeType.ARREARS);
				}
				accountNotice.setNoticePrice(price);
				accountNoticeService.insert(accountNotice);
			}
		}

		accountsettingMapper.updateByPrimaryKeySelective(at);
	}

}
