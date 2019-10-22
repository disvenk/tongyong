package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.enums.PlatformKey;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.dto.MeiTuanOrderDto;
import com.resto.brand.web.dto.PlatformReportDto;
import com.resto.shop.web.dao.PlatformOrderMapper;
import com.resto.shop.web.model.PlatformOrder;
import com.resto.shop.web.report.PlatformOrderMapperReport;
import com.resto.shop.web.service.PlatformOrderDetailService;
import com.resto.shop.web.service.PlatformOrderExtraService;
import com.resto.shop.web.service.PlatformOrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
@Service
public class PlatformOrderServiceImpl extends GenericServiceImpl<PlatformOrder, String> implements PlatformOrderService {

    @Resource
    private PlatformOrderMapper platformorderMapper;
    @Resource
    private PlatformOrderMapperReport platformOrderMapperReport;
    @Resource
    private PlatformOrderDetailService platformOrderDetailService;
    @Resource
    private PlatformOrderExtraService platformOrderExtraService;


    @Override
    public GenericDao<PlatformOrder, String> getDao() {
        return platformorderMapper;
    }

    @Override
    public PlatformOrder selectByPlatformOrderId(String platformOrderId,Integer type) {
        return platformorderMapper.selectByPlatformOrderId(platformOrderId,type);
    }

    @Override
    public PlatformReportDto selectByshopDetailId(String beginDate, String endDate, String shopDetailId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        Map map=new HashMap();
        map.put("beginDate",begin);
        map.put("endDate",end);
        map.put("shopDetailId",shopDetailId);
        return platformorderMapper.selectByshopDetailId(map);
    }

    @Override
    public PlatformReportDto proc_shopdetailId(String beginDate, String endDate, String shopDetailId){
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return platformOrderMapperReport.proc_shopdetailId(begin,end,shopDetailId);
    }

    @Override
    public List<PlatformOrder> selectshopDetailIdList(String beginDate, String endDate, String shopDetailId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        Map map=new HashMap();
        map.put("beginDate",begin);
        map.put("endDate",end);
        map.put("shopDetailId",shopDetailId);
        return platformOrderMapperReport.selectshopDetailIdList(map);
    }
    @Override
    public List<PlatformOrder> getPlatformOrderDetailList(String platformOrderId) {
        return platformOrderMapperReport.getPlatformOrderDetailList(platformOrderId);
    }

    @Override
    public List<PlatformOrder> selectPlatFormErrorOrderList(String currentShopId, Date date) {
        Date begin = DateUtil.getDateBegin(date);
        Date end = DateUtil.getDateEnd(date);
        return platformorderMapper.selectPlatFormErrorOrderList(currentShopId, begin, end);
    }

    @Override
    public BigDecimal sumPlatformOrderPrice(String shopId, String beginDate, String endDate) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);

        return null;
    }

    @Override
    public void meituanNewOrder(MeiTuanOrderDto orderDto) {
        PlatformOrder platformOrder = meituanConvertToPlatformOrder(orderDto);
        platformorderMapper.insertSelective(platformOrder);
        platformOrderDetailService.meituanOrderDetail(orderDto.getOrderId(),orderDto.getDetail());
        platformOrderExtraService.meituanOrderExtra(orderDto);
    }

    public PlatformOrder meituanConvertToPlatformOrder(MeiTuanOrderDto orderDto){
        PlatformOrder platformOrder = new PlatformOrder();
        platformOrder.setId(ApplicationUtils.randomUUID());
        platformOrder.setType(PlatformKey.MEITUAN);
        platformOrder.setPlatformOrderId(orderDto.getOrderId());
        platformOrder.setShopDetailId(orderDto.getePoiId());
        platformOrder.setOriginalPrice(new BigDecimal(Double.toString(orderDto.getOriginalPrice())));
        platformOrder.setTotalPrice(new BigDecimal(Double.toString(orderDto.getTotal())));
        platformOrder.setAddress(orderDto.getRecipientAddress());
        platformOrder.setName(orderDto.getRecipientName());
        platformOrder.setPhone(orderDto.getRecipientPhone());
        platformOrder.setOrderCreateTime(new Date( orderDto.getCtime() * 1000));
        platformOrder.setCreateTime(new Date());
        platformOrder.setPayType(orderDto.getPayType()==1?"货到付款":"在线支付");
        platformOrder.setRemark(orderDto.getCaution());
        platformOrder.setSourceText(orderDto.getSourceText());
        return platformOrder;
    }

}
