package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.FreeDayMapper;
import com.resto.shop.web.model.FreeDay;
import com.resto.shop.web.service.FreedayService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class FreedayServiceImpl extends GenericServiceImpl<FreeDay, String> implements FreedayService {

	@Resource
	private FreeDayMapper freedayMapper;

	@Override
	public GenericDao<FreeDay, String> getDao() {
		return freedayMapper;
	}

	@Override
	public List<FreeDay> list(FreeDay day) {
		return freedayMapper.selectList(day);
	}

	@Override
	public int delete(FreeDay day) {
		return freedayMapper.deleteByDateAndId(day);
	}

	@Override
	public void setMonthWeekend(FreeDay day) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(day.getFreeDay());
		for (int i = 1; i <= cal.getMaximum(Calendar.DAY_OF_MONTH); i++) {
			cal.set(Calendar.DAY_OF_MONTH, i);
			int week = cal.get(Calendar.DAY_OF_WEEK);
			if (week == 1 || week == 7) {
				try {
					day.setFreeDay(cal.getTime());
					this.insert(day);
				} catch (DuplicateKeyException keyException) {
					System.out.println(keyException.getMessage());
				}
			}

		}

	}

	@Override
	public void setYearWeekend(FreeDay day) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(day.getFreeDay());
		for (int i = 0; i < 12; i++) {
			cal.set(Calendar.MONTH, i);
			FreeDay day2 = new FreeDay();
			day2.setShopDetailId(day.getShopDetailId());
			day2.setFreeDay(cal.getTime());
			this.setMonthWeekend(day2);
		}

	}

	@Override
	public boolean selectExists(Date date,String shopId) {
		FreeDay day = freedayMapper.selectByDate(DateFormatUtils.format(date, "yyyy-MM-dd"),shopId);
		return day!=null;
	}


}
