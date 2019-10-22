package com.resto.service.shop.service;

import com.resto.conf.util.DateUtil;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.SupportTime;
import com.resto.service.shop.mapper.SupportTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SupportTimeService extends BaseService<SupportTime, Integer>{

    @Autowired
    private SupportTimeMapper supporttimeMapper;
    @Autowired
	private FreedayService freedayService;

    @Override
    public BaseDao<SupportTime, Integer> getDao() {
        return supporttimeMapper;
    }

    public List<SupportTime> selectList(String shopDetailId) {
        return supporttimeMapper.selectList(shopDetailId);
    }

	public void saveSupportTimes(String articleId, Integer[] supportTimes) {
		supporttimeMapper.deleteArticleSupportTime(articleId);
		if(supportTimes!=null&&supportTimes.length>0){
			supporttimeMapper.insertArticleSupportTime(articleId,supportTimes);
		}
	}

	public List<Integer> selectByIdsArticleId(String articleId) {
		return supporttimeMapper.selectByArticleId(articleId);
	}

	public List<SupportTime> selectNowSopport(String currentShopId) {
		List<SupportTime> supportTime = supporttimeMapper.selectList(currentShopId);
		boolean isFreeDay = freedayService.selectExists(new Date(),currentShopId);
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
			System.out.println(bin&todaybin);
			System.out.println(bin&freeDaybin);
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

}
