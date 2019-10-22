package com.resto.service.brand.service;


import com.resto.conf.util.MyComaparator;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.AccountNotice;
import com.resto.service.brand.entity.AccountSetting;
import com.resto.service.brand.mapper.AccountNoticeMapper;
import com.resto.service.brand.mapper.AccountSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
@Service
public class AccountSettingService extends BaseService<AccountSetting, Long> {

    @Autowired
    private AccountSettingMapper accountsettingMapper;


    @Autowired
	private AccountNoticeMapper accountnoticeMapper;

    public BaseDao<AccountSetting, Long> getDao() {
        return accountsettingMapper;
    }

    public AccountSetting selectByBrandSettingId(String brandSettingId) {
        AccountSetting accountSetting = accountsettingMapper.selectByBrandSettingId(brandSettingId);
		List<AccountNotice> accountNotices = new ArrayList<>();
		if(accountSetting!=null){
			accountNotices	=	accountnoticeMapper.selectByAccountId(accountSetting.getAccountId());
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

}
