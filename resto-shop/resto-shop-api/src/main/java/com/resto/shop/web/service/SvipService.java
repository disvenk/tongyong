package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.dto.SvipActReportDto;
import com.resto.shop.web.dto.SvipActivityDto;
import com.resto.shop.web.model.Svip;

import java.util.List;

public interface SvipService extends GenericService<Svip, String> {

    public List<SvipActivityDto> getActReport(String beginDate,String endDate);

    public List<SvipActReportDto> selectListByActId(String beginDate, String endDate, String id);

    public Long isSvip(String id);
}
