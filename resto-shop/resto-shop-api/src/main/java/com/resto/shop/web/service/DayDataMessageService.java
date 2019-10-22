package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.DayDataMessage;

import java.util.List;

public interface DayDataMessageService extends GenericService<DayDataMessage, String> {

    List<DayDataMessage> selectListByTime(int normal, String date, int dayMessage);
}
