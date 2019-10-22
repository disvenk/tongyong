package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.Advert;
import com.resto.service.shop.mapper.AdvertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertService extends BaseService<Advert, Integer> {

    @Autowired
    private AdvertMapper advertMapper;

    @Override
    public BaseDao<Advert, Integer> getDao() {
        return advertMapper;
    }

	public List<Advert> selectListByShopId(String shopId) {
		return advertMapper.selectListByShopId(shopId);
	}

}
