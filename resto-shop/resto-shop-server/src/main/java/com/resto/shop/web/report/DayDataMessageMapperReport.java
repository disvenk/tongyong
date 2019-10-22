package com.resto.shop.web.report;

import com.resto.shop.web.model.DayDataMessage;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DayDataMessageMapperReport{

    List<DayDataMessage> selectListByTime(@Param("state") int normal, @Param("type") int dayMessage, @Param("date") Date dateTime);


}
