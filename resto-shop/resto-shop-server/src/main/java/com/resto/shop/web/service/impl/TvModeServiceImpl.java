package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.TvModeMapper;
import com.resto.shop.web.model.TvMode;
import com.resto.shop.web.service.TvModeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class TvModeServiceImpl extends GenericServiceImpl<TvMode, Integer> implements TvModeService {

    @Resource
    private TvModeMapper tvmodeMapper;

    @Override
    public GenericDao<TvMode, Integer> getDao() {
        return tvmodeMapper;
    }

    @Override
    public TvMode selectByDeviceShopIdSource(String shopId, String deviceToken, Integer appSource) {
        return tvmodeMapper.selectByDeviceShopIdSource(shopId, deviceToken, appSource);
    }

    @Override
    public List<TvMode> selectByShopIdSource(String shopId, Integer appSource) {
        return tvmodeMapper.selectByShopIdSource(shopId, appSource);
    }
}
