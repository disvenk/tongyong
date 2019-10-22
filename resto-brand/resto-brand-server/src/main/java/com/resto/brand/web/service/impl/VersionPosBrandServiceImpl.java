package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ShopDetailMapper;
import com.resto.brand.web.dao.VersionPosBrandMapper;
import com.resto.brand.web.dao.VersionPosMapper;
import com.resto.brand.web.dao.VersionPosShopDetailMapper;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.VersionPosBrandService;
import com.resto.brand.web.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Service
public class VersionPosBrandServiceImpl extends GenericServiceImpl<VersionPosBrand,Integer> implements VersionPosBrandService {

    @Autowired
    VersionPosBrandMapper versionPosBrandMapper;

    @Autowired
    BrandService brandService;

    @Autowired
    ShopDetailMapper shopDetailMapper;
    @Autowired
    ShopDetailService shopDetailService;
    @Autowired
    VersionPosShopDetailMapper versionPosShopDetailMapper;

    @Autowired
    VersionService versionService;


    @Autowired
    VersionPosMapper versionPosMapper;
    @Override
    public GenericDao<VersionPosBrand, Integer> getDao() {
        return versionPosBrandMapper;
    }

    @Override
    public Map<String, Object> selectListByBrandId(String brandId,String shopDetailId) {
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        VersionPosBrand versionPosBrand = versionPosBrandMapper.selectByBrandId(brandId);
        List<VersionPosShopDetail> versionPosShopDetails = versionPosShopDetailMapper.selectByBrandIdAndShopDetailId(brandId,shopDetailId);
        VersionPos versionPos = versionPosMapper.selectVersionMessage();
        stringObjectHashMap.put("versionPos",versionPos);
        stringObjectHashMap.put("brand",versionPosBrand);
        stringObjectHashMap.put("store",versionPosShopDetails.size()>0?versionPosShopDetails.get(0):null);
        return stringObjectHashMap;
    }

    @Override
    public List<Brand> selectNotExistVersionPosBrand() {
        return  versionPosBrandMapper.selectNotExistVersionPosBrand();
    }

    @Override
    public List<VersionPosBrand> selectByVersionId(Integer versionId) {
        List<VersionPosBrand> versionPosBrands = versionPosBrandMapper.selectByVersionId(versionId);
        if(versionPosBrands!=null && versionPosBrands.size()>0){
            for (VersionPosBrand versionPosBrand : versionPosBrands){
                List<VersionPosShopDetail> versionPosShopDetails = versionPosShopDetailMapper.selectByBrandId(versionPosBrand.getBrandId());
                versionPosBrand.setVersionPosShopDetails(versionPosShopDetails);
            }
        }else{
            List<VersionPosShopDetail> versionPosShopDetails = versionPosShopDetailMapper.selectByVersionId(versionId);
            if (versionPosShopDetails!=null && versionPosShopDetails.size()>0){
                VersionPosBrand versionPosBrand = new VersionPosBrand();
                versionPosBrand.setVersionPosShopDetails(versionPosShopDetails);
                versionPosBrands.add(versionPosBrand);
            }
        }
        return versionPosBrands;
    }

    @Override
    public VersionPos selectTotalversion() {
        return versionPosMapper.selectTotalversion();
    }

    @Override
    public List<VersionPosBrand> selectList() {
        List<VersionPosBrand> versionPosBrands = versionPosBrandMapper.selectList();
        if (versionPosBrands!=null &&versionPosBrands.size()>0){
            for (VersionPosBrand versionPosBrand: versionPosBrands){
                Brand brand = brandService.selectByPrimaryKey(versionPosBrand.getBrandId());
                versionPosBrand.setBrandName(brand.getBrandName());
                Version version = versionService.selectById(versionPosBrand.getVersionId());
                versionPosBrand.setVersionNo(version.getVersionNo());
                versionPosBrand.setDownloadAddress(version.getDownloadAddress());
                versionPosBrand.setType(version.getSubstituteMode());
                versionPosBrand.setVoluntarily(version.getVoluntarily());
                List<VersionPosShopDetail> versionPosShopDetails = versionPosShopDetailMapper.selectByBrandId(versionPosBrand.getBrandId());
                if (versionPosShopDetails !=null && versionPosShopDetails.size()>0){
                    for (VersionPosShopDetail versionPosShopDetail : versionPosShopDetails){
                        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(versionPosShopDetail.getShopdetailId());
                        if (shopDetail!=null) {
                            versionPosShopDetail.setShopName(shopDetail.getName());
                        }
                    }
                }
                versionPosBrand.setVersionPosShopDetails(versionPosShopDetails);
            }
        }
        return versionPosBrands;
    }

    @Override
    public int update(VersionPosBrand versionPosBrand) {
        List<VersionPosShopDetail> versionPosShopDetails = versionPosBrand.getVersionPosShopDetails();
        for (VersionPosShopDetail versionPosShopDetail : versionPosShopDetails){
            if(versionPosShopDetail.getVersionId()!=null){
                Version version = versionService.selectById(versionPosShopDetail.getVersionId());
                versionPosShopDetail.setVersionNo(version.getVersionNo());
                versionPosShopDetail.setDownloadAddress(version.getDownloadAddress());
            }
            versionPosShopDetailMapper.updateByPrimaryKey(versionPosShopDetail);
        }
        Version version = versionService.selectById(versionPosBrand.getVersionId());
        versionPosBrand.setVersionNo(version.getVersionNo());
        versionPosBrand.setDownloadAddress(version.getDownloadAddress());
        versionPosBrand.setType(version.getSubstituteMode());
        versionPosBrand.setVoluntarily(version.getVoluntarily());
        String brandId = versionPosBrand.getBrandId();
        List<ShopDetail> shopDetails = shopDetailMapper.selectNotExistShopDetailByBrandId(brandId);
        if (shopDetails!=null && shopDetails.size()>0){
            for (ShopDetail shopDetail : shopDetails){
                VersionPosShopDetail versionPosShopDetail = new VersionPosShopDetail();
                versionPosShopDetail.setBrandId(brandId);
                versionPosShopDetail.setShopdetailId(shopDetail.getId());
                versionPosShopDetailMapper.insertSelective(versionPosShopDetail);
            }
        }
        return super.update(versionPosBrand);
    }

    @Override
    public int insert(VersionPosBrand versionPosBrand) {
        String brandId = versionPosBrand.getBrandId();
        List<ShopDetail> shopDetails = shopDetailMapper.selectListByBrandId(versionPosBrand.getBrandId());
        if(shopDetails!=null){
            for (ShopDetail shopDetail: shopDetails){
                VersionPosShopDetail versionPosShopDetail = new VersionPosShopDetail();
                versionPosShopDetail.setBrandId(brandId);
                versionPosShopDetail.setShopdetailId(shopDetail.getId());
                versionPosShopDetailMapper.insertSelective(versionPosShopDetail);
            }
        }
        Version version = versionService.selectById(versionPosBrand.getVersionId());
        versionPosBrand.setVersionNo(version.getVersionNo());
        versionPosBrand.setDownloadAddress(version.getDownloadAddress());
        versionPosBrand.setType(version.getSubstituteMode());
        versionPosBrand.setVoluntarily(version.getVoluntarily());
        return super.insert(versionPosBrand);
    }
}
