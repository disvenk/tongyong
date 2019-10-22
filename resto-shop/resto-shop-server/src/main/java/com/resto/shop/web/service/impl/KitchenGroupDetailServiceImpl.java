package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.KitchenGroupDetailMapper;
import com.resto.shop.web.model.KitchenGroupDetail;
import com.resto.shop.web.service.KitchenGroupDetailService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class KitchenGroupDetailServiceImpl extends GenericServiceImpl<KitchenGroupDetail, Integer> implements KitchenGroupDetailService {

    @Resource
    private KitchenGroupDetailMapper kitchengroupdetailMapper;

    @Override
    public GenericDao<KitchenGroupDetail, Integer> getDao() {
        return kitchengroupdetailMapper;
    }

    @Override
    public List<KitchenGroupDetail> selectKitchenGroupDetailByShopId(String shopDetailId) {
        return kitchengroupdetailMapper.selectByShoDetailId(shopDetailId);
    }
}
