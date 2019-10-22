package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.ArticleTopDto;
import com.resto.shop.web.model.ArticleTop;

import java.util.Date;
import java.util.List;

public interface ArticleTopService extends GenericService<ArticleTop, Long> {

    int selectSumGoodByTime(Date begin,Date end,String shopId);

    int selectSumBadByTime(Date begin,Date end,String shopId);

    List<ArticleTopDto> selectListByTimeAndGoodType(Date xunBegin, Date xunEnd, String id);

    List<ArticleTopDto> selectListByTimeAndBadType(Date xunBegin, Date xunEnd, String id);
}
