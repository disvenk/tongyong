package com.resto.shop.web.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.dao.SvipMapper;
import com.resto.shop.web.dto.SvipActReportDto;
import com.resto.shop.web.dto.SvipActivityDto;
import com.resto.shop.web.model.Svip;
import com.resto.shop.web.service.SvipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Service
@Component
public class SvipServiceImpl extends GenericServiceImpl<Svip, String> implements SvipService {

    @Resource
    private SvipMapper svipMapper;

    @Autowired
    private ShopDetailService shopDetailService;

    @Override
    public GenericDao<Svip, String> getDao() {
        return svipMapper;
    }

    @Override
    public List<SvipActivityDto> getActReport(String beginDate,String endDate) {
        beginDate = beginDate+" 00:00:00";
        endDate = endDate + " 23:59:59";
        return svipMapper.getAtcReport(beginDate,endDate);
    }

    @Override
    public List<SvipActReportDto> selectListByActId(String beginDate, String endDate, String id) {
        beginDate = beginDate+" 00:00:00";
        endDate = endDate+" 23:59:59";
        List<SvipActReportDto> svipActReportDtos = svipMapper.selectListByActId(id, beginDate, endDate);
        svipActReportDtos.forEach(n->{
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(n.getShopDetailId());
            n.setShopName(shopDetail.getName());
        });
        return svipActReportDtos;
    }

    @Override
    public Long isSvip(String id) {
        return svipMapper.isSvip(id);
    }
}
