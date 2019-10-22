package com.resto.brand.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ModuleList;

public interface ModuleListService extends GenericService<ModuleList, Integer> {

	void updateBrandModuleList(String brandId, Integer[] hasModule);

	List<Integer> selectBrandHasModule(String brandId);

	List<ModuleList> selectListWithHas(String currentBrandId);

	boolean hasModule(String sign, String brandId);

}
