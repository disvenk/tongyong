package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.dto.MeiTuanOrderDetailDto;
import com.resto.shop.web.dao.PlatformOrderDetailMapper;
import com.resto.shop.web.model.PlatformOrderDetail;
import com.resto.shop.web.model.PlatformOrderExtra;
import com.resto.shop.web.service.PlatformOrderDetailService;
import com.resto.shop.web.service.PlatformOrderExtraService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Component
@Service
public class PlatformOrderDetailServiceImpl extends GenericServiceImpl<PlatformOrderDetail, String> implements PlatformOrderDetailService {

    @Resource
    private PlatformOrderDetailMapper platformorderdetailMapper;
    @Resource
    private PlatformOrderExtraService platformOrderExtraService;

    @Override
    public GenericDao<PlatformOrderDetail, String> getDao() {
        return platformorderdetailMapper;
    }

    @Override
    public List<PlatformOrderDetail> selectByPlatformOrderId(String platformOrderId) {
        return platformorderdetailMapper.selectByPlatformOrderId(platformOrderId);
    }

    @Override
    public void meituanOrderDetail(String orderId,String orderDetail) {
        List<MeiTuanOrderDetailDto> orderDetailList = JSON.parseArray(orderDetail,MeiTuanOrderDetailDto.class);
        int boxNum = 0;
        double boxPrice = 0;
        for (MeiTuanOrderDetailDto detail : orderDetailList ){
            boxNum += detail.getBox_num();
            boxPrice += detail.getBox_price() * detail.getBox_num();
            platformorderdetailMapper.insertSelective(meituanConvertToPlatformOrderDetail(orderId,detail));
        }
        if(boxPrice>0){
            PlatformOrderExtra orderExtra = new PlatformOrderExtra();
            orderExtra.setId(ApplicationUtils.randomUUID());
            orderExtra.setPlatformOrderId(orderId);
            orderExtra.setName("餐盒费");
            orderExtra.setQuantity(boxNum);
            orderExtra.setPrice(new BigDecimal(Double.toString(boxPrice)));
            platformOrderExtraService.insert(orderExtra);
        }
    }

    public PlatformOrderDetail meituanConvertToPlatformOrderDetail(String orderId,MeiTuanOrderDetailDto meiTuanOrderDetail){
        PlatformOrderDetail platformOrderDetail = new PlatformOrderDetail();
        platformOrderDetail.setId(ApplicationUtils.randomUUID());
        platformOrderDetail.setPlatformOrderId(orderId);
        platformOrderDetail.setName(meiTuanOrderDetail.getFood_name());
        platformOrderDetail.setPrice(new BigDecimal(Double.toString(meiTuanOrderDetail.getPrice())));
        platformOrderDetail.setQuantity(meiTuanOrderDetail.getQuantity());
        String showName = meiTuanOrderDetail.getFood_name()+(StringUtils.isEmpty(meiTuanOrderDetail.getSpec()) ?"":"（"+meiTuanOrderDetail.getSpec()+"）") ;
        platformOrderDetail.setShowName(showName);
        return platformOrderDetail;
    }

}
