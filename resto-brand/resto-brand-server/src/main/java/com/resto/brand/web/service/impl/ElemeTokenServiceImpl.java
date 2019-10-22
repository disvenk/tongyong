package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ElemeTokenMapper;
import com.resto.brand.web.model.ElemeToken;
import com.resto.brand.web.service.ElemeTokenService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by carl on 2017/5/26.
 */
@Component
@Service
public class ElemeTokenServiceImpl extends GenericServiceImpl<ElemeToken, Long> implements ElemeTokenService {

    @Resource
    private ElemeTokenMapper elemeTokenMapper;

    @Override
    public GenericDao<ElemeToken, Long> getDao() {
        return elemeTokenMapper;
    }

    @Override
    public ElemeToken getTokenByShopId(String shopId) {
        return elemeTokenMapper.getTokenByShopId(shopId);
    }



    @Override
    public int updateSelectByShopId(ElemeToken elemeToken) {
        return elemeTokenMapper.updateSelectByShopId(elemeToken);
    }
}
