package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.WeightPackageMapper;
import com.resto.shop.web.model.WeightPackage;
import com.resto.shop.web.model.WeightPackageDetail;
import com.resto.shop.web.service.WeightPackageDetailService;
import com.resto.shop.web.service.WeightPackageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Service
public class WeightPackageServiceImpl extends GenericServiceImpl<WeightPackage, Long> implements WeightPackageService {

    @Resource
    private WeightPackageMapper weightPackageMapper;

    @Resource
    private WeightPackageDetailService weightPackageDetailService;

    @Override
    public GenericDao<WeightPackage, Long> getDao() {
        return weightPackageMapper;
    }

    @Override
    public List<WeightPackage> getAllWeightPackages(String shopId) {
        return weightPackageMapper.getAllWeightPackages(shopId);
    }

    @Override
    public WeightPackage insertDetail(WeightPackage weightPackage) {
        for(WeightPackageDetail weightPackageDetail : weightPackage.getDetails()){
            weightPackageDetailService.insertDetail(weightPackage.getId(), weightPackageDetail);
        }
        return weightPackage;
    }

    @Override
    public void initWeightPackageDetail(WeightPackage weightPackage) {
        weightPackageDetailService.deleteDetails(weightPackage.getId());
    }

    @Override
    public WeightPackage selectByDateShopId(String name, String shopId) {
        return weightPackageMapper.selectByDateShopId(name, shopId);
    }

    @Override
    public WeightPackage getWeightPackageById(Long id) {
        return weightPackageMapper.getWeightPackageById(id);
    }

    @Override
    public List<WeightPackage> selectWeightPackageByShopId(String shopId) {
        return weightPackageMapper.selectWeightPackageByShopId(shopId);
    }

    @Override
    public List<WeightPackage> getWeightPackage() {
        return weightPackageMapper.getWeightPackage();
    }

    @Override
    public List<WeightPackage> getAllBrandWeightPackages(String currentShopId) {
        return weightPackageMapper.getAllBrandWeightPackages(currentShopId);
    }
}
