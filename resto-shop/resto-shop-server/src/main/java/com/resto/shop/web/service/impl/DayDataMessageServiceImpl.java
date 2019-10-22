package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.shop.web.dao.DayDataMessageMapper;
import com.resto.shop.web.model.DayDataMessage;
import com.resto.shop.web.report.DayDataMessageMapperReport;
import com.resto.shop.web.service.DayDataMessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class DayDataMessageServiceImpl extends GenericServiceImpl<DayDataMessage, String> implements DayDataMessageService {

    @Resource
    private DayDataMessageMapper daydatamessageMapper;

    @Resource
    private DayDataMessageMapperReport dayDataMessageMapperReport;

    @Override
    public GenericDao<DayDataMessage, String> getDao() {
        return daydatamessageMapper;
    }

    @Override
    public List<DayDataMessage> selectListByTime(int normal, String date, int dayMessage) {
        Date dateTime = DateUtil.fomatDate(date);
        return dayDataMessageMapperReport.selectListByTime(normal,dayMessage,dateTime);
    }
}
