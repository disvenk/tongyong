package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.WeightPackageDetailMapper;
import com.resto.shop.web.model.WeightPackageDetail;
import com.resto.shop.web.service.WeightPackageDetailService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Service
public class WeightPackageDetailServiceImpl extends GenericServiceImpl<WeightPackageDetail, Long> implements WeightPackageDetailService {

    @Resource
    private WeightPackageDetailMapper weightPackageDetailMapper;

    @Override
    public GenericDao<WeightPackageDetail, Long> getDao() {
        return weightPackageDetailMapper;
    }

    @Override
    public int insertDetail(Long weightPackageId, WeightPackageDetail weightPackageDetail) {
        weightPackageDetail.setWeightPackageId(weightPackageId);
        return weightPackageDetailMapper.insertSelective(weightPackageDetail);
    }

    @Override
    public void deleteDetails(Long weightPackageId) {
        weightPackageDetailMapper.deleteDetails(weightPackageId);
    }

    @Override
    public List<WeightPackageDetail> selectWeightPackageDetailByShopId(String shopId) {
        return weightPackageDetailMapper.selectWeightPackageDetailByShopId(shopId);
    }
}
