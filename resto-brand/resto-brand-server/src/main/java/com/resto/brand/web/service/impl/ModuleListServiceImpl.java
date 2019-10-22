package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ModuleListMapper;
import com.resto.brand.web.model.ModuleList;
import com.resto.brand.web.service.ModuleListService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 *
 */
@Component
@Service
public class ModuleListServiceImpl extends GenericServiceImpl<ModuleList, Integer> implements ModuleListService {

    @Resource
    private ModuleListMapper modulelistMapper;

    @Override
    public GenericDao<ModuleList, Integer> getDao() {
        return modulelistMapper;
    }

	@Override
	public void updateBrandModuleList(String brandId, Integer[] hasModule) {
		modulelistMapper.deleteBrandModuleList(brandId);
		
		//没有选择任何选项
		if(hasModule==null||"".equals(hasModule)){
			//删除所有的功能
			modulelistMapper.deleteBrandModuleList(brandId);
		}else{
			modulelistMapper.insertBatch(brandId,hasModule);
		}
		
	}

	@Override
	public List<Integer> selectBrandHasModule(String brandId) {
		return modulelistMapper.selectBrandHasModule(brandId);
	}

	@Override
	public List<ModuleList> selectListWithHas(String currentBrandId) {
		List<ModuleList> list = selectList();
		List<Integer> moduleIds = selectBrandHasModule(currentBrandId);
		for (ModuleList module : list) {
			if(moduleIds.contains(module.getId())){
				module.setIsOpen(true);
			}else{
				module.setIsOpen(false);
			}
		}
		return list;
	}


	@Override
	public boolean hasModule(String sign, String brandId) {
		ModuleList module = modulelistMapper.selectBySignAndBrandId(sign,brandId);
		return module!=null;
	} 

}
