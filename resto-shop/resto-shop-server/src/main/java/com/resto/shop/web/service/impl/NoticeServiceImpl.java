package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.shop.web.dao.NoticeMapper;
import com.resto.shop.web.dao.SupportTimeMapper;
import com.resto.shop.web.model.Notice;
import com.resto.shop.web.model.SupportTime;
import com.resto.shop.web.service.FreedayService;
import com.resto.shop.web.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class NoticeServiceImpl extends GenericServiceImpl<Notice, String> implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    @Autowired
    private SupportTimeMapper supportTimeMapper;

    @Autowired
    private FreedayService freedayService;

    @Override
    public GenericDao<Notice, String> getDao() {
        return noticeMapper;
    } 
    
    @Override
    public Notice create(Notice notice) {
    	notice.setId(ApplicationUtils.randomUUID());
    	notice.setCreateDate(new Date());
    	insert(notice);
    	return notice;
    }

	@Override
	public List<Notice> selectListByShopId(String shopId,String customerId, Integer noticeType) {
		List<Notice> notices =  noticeMapper.selectListByShopId(shopId,noticeType,customerId);
		List<SupportTime> supportTime = supportTimeMapper.selectList(shopId);
		boolean isFreeDay = freedayService.selectExists(new Date(),shopId);
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
					Date begin = DateUtil.parseDate(st.getBeginTime(),"HH:mm");
					Date end = DateUtil.parseDate(st.getEndTime(),"HH:mm");
					int nowMin = DateUtil.getMinOfDay(now);
					int beginMin = DateUtil.getMinOfDay(begin);
					int endMin = DateUtil.getMinOfDay(end);
					if(beginMin<nowMin&&endMin>nowMin){
						support.add(st);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		List<Notice> result = new ArrayList<>();
		for(Notice notice : notices){
			List<Integer> list = noticeMapper.getSupportTime(notice.getId());
			if(CollectionUtils.isEmpty(list) ||  checkTime(list,support)){
				result.add(notice);
			}

		}

		return result;
	}

	private Boolean checkTime(List<Integer> list,List<SupportTime> supportTimes){
    	for(SupportTime supportTime : supportTimes){
    		for(Integer id : list){
    			if(id.equals(supportTime.getId())){
    				return true;
				}
			}
		}

		return false;
	}

	@Override
	public void addNoticeHistory(String customerId, String noticeId) {
		noticeMapper.addNoticeHistory(customerId, noticeId);
	}

	@Override
	public List<Notice> selectListAllByShopId(String currentShopId) {
		return noticeMapper.selectListAllByShopId(currentShopId);
	}

	@Override
	public void bindSupportTime(Notice notice) {
		//先清空绑定的时间
		noticeMapper.clearSupportTime(notice.getId());
		for(Integer time : notice.getSupportTimes()){
			noticeMapper.insertSupportTime(notice.getId(),time);
		}
	}

	@Override
	public Integer[] getSupportTime(String noticeId) {
    	List<Integer> supportTime = noticeMapper.getSupportTime(noticeId);
    	if(CollectionUtils.isEmpty(supportTime)){
    		return new Integer[0];
		}else{
    		return supportTime.toArray(new Integer[0]);
		}
	}
}
