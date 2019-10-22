package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Version;
import com.resto.brand.web.model.VersionPackage;

import java.util.List;

public interface VersionService extends GenericService<Version, Integer> {

    List<VersionPackage> selectversionPackage();

    void createPackage(String version);

    List<Version> selectVersionAsc();

    VersionPackage selectPackageUrlByBrandId(String brandId);

    void updatePackage(String version);
}
