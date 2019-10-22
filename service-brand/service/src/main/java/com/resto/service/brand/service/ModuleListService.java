package com.resto.service.brand.service;

import java.util.List;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.ModuleList;
import com.resto.service.brand.mapper.ModuleListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ModuleListService extends BaseService<ModuleList, Integer> {

    @Autowired
    private ModuleListMapper modulelistMapper;

    public BaseDao<ModuleList, Integer> getDao() {
        return modulelistMapper;
    }

	public List<Integer> selectBrandHasModule(String brandId) {
		return modulelistMapper.selectBrandHasModule(brandId);
	}


}
