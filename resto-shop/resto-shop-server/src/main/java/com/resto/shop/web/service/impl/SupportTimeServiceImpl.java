package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.shop.web.dao.SupportTimeMapper;
import com.resto.shop.web.model.SupportTime;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.FreedayService;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.service.SupportTimeService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class SupportTimeServiceImpl extends GenericServiceImpl<SupportTime, Integer> implements SupportTimeService {

    @Resource
    private SupportTimeMapper supporttimeMapper;
    @Resource
    private FreedayService freeDayService;
	@Autowired
	private PosService posService;

    @Override
    public GenericDao<SupportTime, Integer> getDao() {
        return supporttimeMapper;
    }

    @Override
    public List<SupportTime> selectList(String shopDetailId) {
        return supporttimeMapper.selectList(shopDetailId);
    }

	@Override
	public void saveSupportTimes(String articleId, Integer[] supportTimes,String brandId,String shopId) {
		supporttimeMapper.deleteArticleSupportTime(articleId);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(brandId);
		shopMsgChangeDto.setShopId(shopId);
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLESUPPORTTIME);
        shopMsgChangeDto.setType("delete");
		shopMsgChangeDto.setId(articleId);
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(supportTimes!=null&&supportTimes.length>0){
			supporttimeMapper.insertArticleSupportTime(articleId,supportTimes);

		}
	}

	@Override
	public List<Integer> selectByIdsArticleId(String articleId) {
		return supporttimeMapper.selectByArticleId(articleId);
	}

	@Override
	public List<SupportTime> selectNowSopport(String currentShopId) {
		List<SupportTime> supportTime = supporttimeMapper.selectList(currentShopId);
		boolean isFreeDay = freeDayService.selectExists(new Date(),currentShopId);
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		int freeDaybin = isFreeDay?1<<8:1<<7;
		//int weekDay = c.get(Calendar.DAY_OF_WEEK)-1;  //这里周末等于1 所以全部减一

        //设置 weekDay
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if(weekDay != 1){//如果为周一到周六，则默认减2。
            weekDay-=2;
        }else{//如果是周日，则不减2，设为默认值6，代表周日 。
            weekDay=6;
        }

		List<SupportTime> support = new ArrayList<>();
		for (SupportTime st : supportTime) {
			int bin = st.getSupportWeekBin();
			int todaybin = 1<<weekDay;
            if((bin&todaybin)>0||(bin&freeDaybin)>0){
				try {
//					Date begin = DateUtil.parseDate(st.getBeginTime(),"HH:mm:ss");
//					Date end = DateUtil.parseDate(st.getEndTime(),"HH:mm:ss");
//					int nowMin = DateUtil.getMinOfDay(now);
//					int beginMin = DateUtil.getMinOfDay(begin);
//					int endMin = DateUtil.getMinOfDay(end);
					Long begin = DateUtil.parseDate(st.getBeginTime(),"HH:mm:ss").getTime();
					Long end = DateUtil.parseDate(st.getEndTime(),"HH:mm:ss").getTime();
					SimpleDateFormat s = new SimpleDateFormat("HH:mm:ss");
					Long nowMin = DateUtil.parseDate(s.format(new Date()),"HH:mm:ss").getTime();
					if(begin<=nowMin&&end>=nowMin){
						support.add(st);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return support;
	}

    @Override
    public Integer insertSupportTime(SupportTime supportTime) {
       int i = supporttimeMapper.insertSelective(supportTime);
        return supportTime.getId();
    }

	@Override
	public List<SupportTime> getSupportTimePackage() {
		return supporttimeMapper.getSupportTimePackage();
	}

	@Override
	public List<SupportTime> selectBrandList(String currentShopId) {
		return supporttimeMapper.selectBrandList(currentShopId);
	}

}
