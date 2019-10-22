package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.MealTempMapper;
import com.resto.shop.web.model.MealTemp;
import com.resto.shop.web.model.MealTempAttr;
import com.resto.shop.web.service.MealTempAttrService;
import com.resto.shop.web.service.MealTempService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class MealTempServiceImpl extends GenericServiceImpl<MealTemp, Integer> implements MealTempService {

    @Resource
    private MealTempMapper mealtempMapper;

    @Resource
    private MealTempAttrService mealTempAttrService;
    
    @Override
    public GenericDao<MealTemp, Integer> getDao() {
        return mealtempMapper;
    }

	@Override
	public void create(MealTemp mealTemp) {
		insert(mealTemp);
		mealTempAttrService.insertBatch(mealTemp.getAttrs(),mealTemp.getId());
	} 
	
	@Override
	public int update(MealTemp mealTemp) {
		mealTempAttrService.insertBatch(mealTemp.getAttrs(),mealTemp.getId());
		return super.update(mealTemp);
	}

	@Override
	public int delete(Integer id) {
		mealTempAttrService.deleteByTempId(id);
		return super.delete(id);
	}
	
	@Override
	public MealTemp selectFull(Integer id) {
		MealTemp mealTemp = selectById(id);
		List<MealTempAttr> attrs = mealTempAttrService.selectByTempId(id);
		mealTemp.setAttrs(attrs);
		return mealTemp;
	}
	
	@Override
	public List<MealTemp> selectList(String brandId) {
		List<MealTemp> temps = mealtempMapper.selectList(brandId);
		for (MealTemp mealTemp : temps) {
			List<MealTempAttr> attrs = mealTempAttrService.selectByTempId(mealTemp.getId());
			mealTemp.setAttrs(attrs);
		}
		return temps;
	}
}
