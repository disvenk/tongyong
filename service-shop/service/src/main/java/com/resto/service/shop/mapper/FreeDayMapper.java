package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.FreeDay;

import java.util.Date;
import java.util.List;

public interface FreeDayMapper extends BaseDao<FreeDay, String> {

	FreeDay selectByDate(String format, String shopId);

}