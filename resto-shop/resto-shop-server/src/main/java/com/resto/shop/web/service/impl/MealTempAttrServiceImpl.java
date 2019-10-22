package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.MealTempAttrMapper;
import com.resto.shop.web.model.MealTempAttr;
import com.resto.shop.web.service.MealTempAttrService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class MealTempAttrServiceImpl extends GenericServiceImpl<MealTempAttr, Integer> implements MealTempAttrService {

    @Resource
    private MealTempAttrMapper mealtempattrMapper;

    @Override
    public GenericDao<MealTempAttr, Integer> getDao() {
        return mealtempattrMapper;
    }

	@Override
	public void insertBatch(List<MealTempAttr> attrs, Integer mealTempId) {
		mealtempattrMapper.deleteByTempId(mealTempId);
		if(attrs!=null&&attrs.size()>0){
			mealtempattrMapper.insertBatch(attrs,mealTempId);
		}
	}
	
	@Override
	public void deleteByTempId(Integer tempId) {
		mealtempattrMapper.deleteByTempId(tempId);
	}

	@Override
	public List<MealTempAttr> selectByTempId(Integer id) {
		return mealtempattrMapper.selectByTempId(id);
	}

}
