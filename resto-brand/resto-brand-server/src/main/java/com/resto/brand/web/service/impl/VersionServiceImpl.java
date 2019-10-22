package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.*;
import com.resto.brand.web.model.Version;
import com.resto.brand.web.model.VersionBrandPackage;
import com.resto.brand.web.model.VersionPackage;
import com.resto.brand.web.service.VersionService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
public class VersionServiceImpl extends GenericServiceImpl<Version, Integer> implements VersionService {
    @Autowired
    VersionMapper versionMapper;

    @Autowired
    VersionPosBrandMapper versionPosBrandMapper;

    @Autowired
    VersionPosMapper versionPosMapper;
    @Autowired
    VersionPosShopDetailMapper versionPosShopDetailMapper;

    @Autowired
    versionPackageMapper versionPackageMapper;

    @Autowired
    VersionBrandPackageMapper versionBrandPackageMapper;

    @Override
    public GenericDao<Version, Integer> getDao() {
        return versionMapper;
    }

    @Override
    public int update(Version version) {
        versionPosBrandMapper.updateVersionNOAndDownloadAddressByVersionId(version.getVersionNo(), version.getDownloadAddress(), version.getSubstituteMode(), version.getVoluntarily(), version.getId());
        versionPosShopDetailMapper.updateVersionNOAndDownloadAddressByVersionId(version.getVersionNo(), version.getDownloadAddress(), version.getSubstituteMode(), version.getVoluntarily(), version.getId());
        versionPosMapper.updateVersionNOAndDownloadAddressByVersionId(version.getVersionNo(), version.getDownloadAddress(), version.getSubstituteMode(), version.getVoluntarily(), version.getId());
        return super.update(version);
    }

    @Override
    public int insert(Version version) {
        return super.insert(version);
    }

    @Override
    public int delete(Integer id) {
        versionPosBrandMapper.deleteByVersionId(id);
        versionPosShopDetailMapper.deleteByVersionId(id);
        return super.delete(id);
    }

    @Override
    public List<VersionPackage> selectversionPackage() {
        List<VersionPackage> versionPackages = versionPackageMapper.selectversionPackage();
        if (versionPackages != null && versionPackages.size() > 0) {
            for (VersionPackage versionPackage : versionPackages) {
                List<VersionBrandPackage> packages = versionBrandPackageMapper.selectBrandPackage(versionPackage.getId());
                versionPackage.setVersionBrandPackage(packages);
            }
        }
        return versionPackages;
    }

    @Override
    public void createPackage(String version) {
        Map map = new HashMap();
        map.put("versionBrandPackage", VersionBrandPackage.class);
        VersionPackage versionPackage = (VersionPackage) JSONObject.toBean(JSONObject.fromObject(version), VersionPackage.class, map);
        versionPackageMapper.insert(versionPackage);
        List<VersionBrandPackage> versionBrandPackages = versionPackage.getVersionBrandPackage();
        if (versionBrandPackages != null && versionBrandPackages.size() > 0) {
            for (VersionBrandPackage versionBrandPackage : versionBrandPackages) {
                versionBrandPackageMapper.deleteByBrandId(versionBrandPackage.getBrandId());
                VersionBrandPackage brandPackage = new VersionBrandPackage();
                brandPackage.setPackageId(versionPackage.getId());
                brandPackage.setBrandId(versionBrandPackage.getBrandId());
                versionBrandPackageMapper.insertSelective(brandPackage);
            }
        }
    }

    @Override
    public List<Version> selectVersionAsc() {
        return versionMapper.selectVersionAsc();
    }

    @Override
    public VersionPackage selectPackageUrlByBrandId(String brandId) {
        return versionPackageMapper.selectPackageUrlByBrandId(brandId);
    }

    @Override
    public void updatePackage(String version) {
        Map map = new HashMap();
        map.put("versionBrandPackage", VersionBrandPackage.class);
        VersionPackage versionPackage = (VersionPackage) JSONObject.toBean(JSONObject.fromObject(version), VersionPackage.class, map);
        versionPackageMapper.updateByPrimaryKeySelective(versionPackage);
        List<VersionBrandPackage> versionBrandPackages = versionPackage.getVersionBrandPackage();
        versionBrandPackageMapper.deleteByPackageId(versionPackage.getId());
        if (versionBrandPackages != null && versionBrandPackages.size() > 0) {
            for (VersionBrandPackage versionBrandPackage : versionBrandPackages) {
                VersionBrandPackage brandPackage = new VersionBrandPackage();
                brandPackage.setPackageId(versionPackage.getId());
                brandPackage.setBrandId(versionBrandPackage.getBrandId());
                versionBrandPackageMapper.insertSelective(brandPackage);
            }
        }
    }
}
