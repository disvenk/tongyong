package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.WeShopScoreMapper;
import com.resto.shop.web.model.WeShopScore;
import com.resto.shop.web.service.WeShopScoreService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 *
 */
@Component
@Service
public class WeShopScoreServiceImpl extends GenericServiceImpl<WeShopScore, Integer> implements WeShopScoreService {

    @Resource
    private WeShopScoreMapper weshopscoreMapper;

    @Override
    public GenericDao<WeShopScore, Integer> getDao() {
        return weshopscoreMapper;
    }

    @Override
    public WeShopScore selectByShopIdAndDate(String id, Date beforeYesterDay) {
        return weshopscoreMapper.selectByShopIdAndDate(id,beforeYesterDay);
    }
}
