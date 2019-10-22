package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.VersionPos;
import com.resto.brand.web.model.VersionPosBrand;

import java.util.List;
import java.util.Map;

public interface VersionPosBrandService extends GenericService<VersionPosBrand,Integer> {

    Map<String,Object> selectListByBrandId(String brandId, String shopDetailId);

    List<Brand> selectNotExistVersionPosBrand();

    List<VersionPosBrand> selectByVersionId(Integer versionId);

    VersionPos selectTotalversion();
}
