package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.service.*;
import com.resto.brand.web.service.BrandService;
import com.resto.shop.web.constant.SmsLogType;
import com.resto.shop.web.dao.SmsLogMapper;
import com.resto.shop.web.model.SmsLog;
import com.resto.shop.web.service.SmsLogService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
@Service
public class SmsLogServiceImpl extends GenericServiceImpl<SmsLog, Long> implements SmsLogService {

    @Resource
    private SmsLogMapper smslogMapper;
    
    @Resource
	BrandService brandService;

    
    @Override
    public GenericDao<SmsLog, Long> getDao() {
        return smslogMapper;
    }


	@Override
	public List<SmsLog> selectListByShopId(String shopId) {
		
		return smslogMapper.selectListByShopId(shopId);
	}

	@Override
	public List<SmsLog> selectListByShopIdAndDate(String ShopId) {
		Date begin = DateUtil.getDateBegin(DateUtil.getAfterDayDate(new Date(), -2));
		return smslogMapper.selectListByShopIdAndDate(ShopId,begin);
	}

	@Override
	public List<SmsLog> selectListWhere(String begin,String end,String shopIds) {
        if(("".equals(begin)||begin==null)&&(end==null||"".equals(""))){
            begin = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
            end = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
        }


		Date beginDate = DateUtil.getformatBeginDate(begin);
		Date endDate = DateUtil.getformatEndDate(end);
		String[] temp = shopIds.split(",");
		//查询短信记录
		List<SmsLog> list =  smslogMapper.selectListByWhere(beginDate, endDate, temp);
		for (SmsLog smsLog : list) {
			smsLog.setSmsLogTyPeName(SmsLogType.getSmsLogTypeName(smsLog.getSmsType()));
		}

		return list;

	}

	@Override
	public List<SmsLog> selecByBrandId(String brandId) {
		List<SmsLog> list = smslogMapper.selectListByBrandId(brandId);
		for (SmsLog smsLog : list) {
			smsLog.setSmsLogTyPeName(SmsLogType.getSmsLogTypeName(smsLog.getSmsType()));
		}
		return list;
	}
	


    @Override
    public SmsLog selectByMap(Map<String, Object> selectMap) {
        return smslogMapper.selectByMap(selectMap);
    }

	@Override
	public JSONObject sendMessage(String brandId, String shopId, int smsType, String sign, String code_temp,String phone ,JSONObject jsonObject) {
		return SMSUtils.sendMessage(phone,jsonObject,sign,code_temp);
	}

//	@Override
//	public JSONObject sendMessage(String telephone, JSONObject sms, String sign, String code_temp,String brandId) {
//		return SMSUtils.sendMessage(telephone,sms,sign,code_temp);
//	}
}
