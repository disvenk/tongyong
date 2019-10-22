package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.DateUtil;
import com.resto.shop.web.dao.OffLineOrderMapper;
import com.resto.shop.web.dto.OrderNumDto;
import com.resto.shop.web.dto.UnderLineOrderDto;
import com.resto.shop.web.model.OffLineOrder;
import com.resto.shop.web.report.OffLineOrderMapperReport;
import com.resto.shop.web.service.OffLineOrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class OffLineOrderServiceImpl extends GenericServiceImpl<OffLineOrder, String> implements OffLineOrderService {

    @Resource
    private OffLineOrderMapper offlineorderMapper;

    @Resource
    private OffLineOrderMapperReport offLineOrderMapperReport;

    @Override
    public GenericDao<OffLineOrder, String> getDao() {
        return offlineorderMapper;
    }

    @Override
    public OffLineOrder selectByTimeSourceAndShopId(Integer source, String shopId) {
        Date begin = DateUtil.getDateBegin(new Date());
        Date end  = DateUtil.getDateEnd(new Date());

        return offlineorderMapper.selectByTimeSourceAndShopId(source,shopId,begin,end);

    }

    @Override
    public List<OffLineOrder> selectByShopIdAndTime(String shopId, Date beginDate, Date endDate) {
        return offlineorderMapper.selectByShopIdAndTime(shopId,beginDate,endDate);
    }

    @Override
    public List<OffLineOrder> selectlistByTimeSourceAndShopId(String id, Date begin, Date end, int offlinePos) {
        return offLineOrderMapperReport.selectlistByTimeSourceAndShopId(id,begin,end,offlinePos);
    }

    @Override
    public List<OffLineOrder> selectlistByTimeSourceAndShopId(String id, String beginTime, String endTime, int offlinePos) {
        Date beginDate = DateUtil.getformatBeginDate(beginTime);
        Date endDate = DateUtil.getformatEndDate(endTime);
        return offLineOrderMapperReport.selectlistByTimeSourceAndShopId(id,beginDate,endDate,offlinePos);
    }

    @Override
    public OffLineOrder selectSumByTimeSourceAndShopId(int offlinePos, String id, Date begin, Date end) {
        return offlineorderMapper.selectSumByTimeSourceAndShopId(offlinePos,id,begin,end);
    }

	@Override
	public OffLineOrder selectByTimeSourceAndShopId(int offlinePos, String id, Date dateBegin, Date dateEnd) {
		return offlineorderMapper.selectByTimeSourceAndShopId(offlinePos,id,dateBegin,dateEnd);
	}

    @Override
    public List<OrderNumDto> selectOrderNumByTimeAndBrandId(String brandId, String begin, String end) {
        Date beginDate = DateUtil.getformatBeginDate(begin);
        Date endDate = DateUtil.getformatEndDate(end);
        return offLineOrderMapperReport.selectOrderNumByTimeAndBrandId(brandId,beginDate,endDate);
    }

    @Override
    public UnderLineOrderDto selectDateAndMonthByShopId(Date todayBegin, Date todayEnd, Date monthBegin, Date monthEnd, String shopId) {
        return offlineorderMapper.selectDateAndMonthByShopId(todayBegin,todayEnd,monthBegin,monthEnd,shopId);
    }

    @Override
    public UnderLineOrderDto selectXunByShopId(Date xunBegin, Date xunEnd, String shopId) {
        return offlineorderMapper.selectXunByShopId(xunBegin,xunEnd,shopId);
    }

    @Override
    public UnderLineOrderDto selectMonthByShopId(Date monthBegin, Date monthEnd, String shopId) {
        return offlineorderMapper.selectMonthByShopId(monthBegin,monthEnd,shopId);

    }

    @Override
    public UnderLineOrderDto selectMonthXunTodayByShopId(Date todayBegin, Date todayEnd, Date xunBegin, Date xunEnd, Date monthBegin, Date monthEnd, String shopId) {
        return offlineorderMapper.selectMonthXunTodayByShopId(todayBegin,todayEnd,xunBegin,xunEnd,monthBegin,monthEnd,shopId);
    }


}
