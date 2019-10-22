package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.VersionPosShopDetailMapper;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.VersionPosShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.VersionPosShopDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class VersionPosShopDetailServiceImpl extends GenericServiceImpl<VersionPosShopDetail,Integer> implements VersionPosShopDetailService {
    @Autowired
    VersionPosShopDetailMapper versionPosShopDetailMapper;

    @Autowired
    ShopDetailService shopDetailService;

    @Override
    public GenericDao<VersionPosShopDetail, Integer> getDao() {
        return versionPosShopDetailMapper;
    }

    @Override
    public List<VersionPosShopDetail> selectList() {
        List<VersionPosShopDetail> versionPosShopDetails = super.selectList();
        if (versionPosShopDetails !=null && versionPosShopDetails.size()>0){
            for (VersionPosShopDetail versionPosShopDetail : versionPosShopDetails){
                ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(versionPosShopDetail.getShopdetailId());
                versionPosShopDetail.setShopName(shopDetail.getName());
            }
        }
        return versionPosShopDetails;
    }

    @Override
    public int deleteByBrandId(String brandId) {
        return versionPosShopDetailMapper.deleteByBrandId(brandId);
    }

    @Override
    public List<VersionPosShopDetail> selectByBrandId(String brandId) {
        List<VersionPosShopDetail> versionPosShopDetails = versionPosShopDetailMapper.selectByBrandId(brandId);
        if (versionPosShopDetails !=null && versionPosShopDetails.size()>0){
            for (VersionPosShopDetail versionPosShopDetail : versionPosShopDetails){
                ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(versionPosShopDetail.getShopdetailId());
                versionPosShopDetail.setShopName(shopDetail.getName());
            }
        }
        return versionPosShopDetails;
    }

    @Override
    public int updatePosVersion(String brandId, String shopdetailId, String shopVersion) {
        return versionPosShopDetailMapper.updatePosVersion(brandId,shopdetailId,shopVersion);
    }
}
