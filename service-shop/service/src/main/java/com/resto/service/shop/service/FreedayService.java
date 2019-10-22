package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.FreeDay;
import com.resto.service.shop.mapper.FreeDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

@Service
public class FreedayService extends BaseService<FreeDay, String> {

	@Autowired
	private FreeDayMapper freedayMapper;

	@Override
	public BaseDao<FreeDay, String> getDao() {
		return freedayMapper;
	}

	public boolean selectExists(Date date, String shopId) {
		FreeDay day = freedayMapper.selectByDate(DateFormatUtils.format(date, "yyyy-MM-dd"),shopId);
		return day!=null;
	}


}
