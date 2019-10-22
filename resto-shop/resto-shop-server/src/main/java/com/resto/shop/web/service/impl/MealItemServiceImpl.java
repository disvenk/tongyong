package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.MealItemMapper;
import com.resto.shop.web.model.MealItem;
import com.resto.shop.web.service.MealItemService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class MealItemServiceImpl extends GenericServiceImpl<MealItem, Integer> implements MealItemService {

    @Resource
    private MealItemMapper mealitemMapper;

    @Override
    public GenericDao<MealItem, Integer> getDao() {
        return mealitemMapper;
    }

	@Override
	public void deleteByMealAttrIds(List<Integer> mealAttrIds) {
		mealitemMapper.deleteByMealAttrIds(mealAttrIds);
	}

	@Override
	public void insertBatch(List<MealItem> mealItems) {
		if(mealItems!=null&&mealItems.size()>0){
			mealitemMapper.insertBatch(mealItems);
		}
	}

	@Override
	public List<MealItem> selectByAttrIds(List<Integer> ids,String show) {
		return mealitemMapper.selectByAttrIds(ids,show);
	}

	@Override
	public List<MealItem> selectByIds(Integer[] mealItemIds) {
		return mealitemMapper.selectByIds(mealItemIds);
	}

	@Override
	public List<MealItem> selectByAttrId(Integer attrId) {
		return mealitemMapper.selectByAttrId(attrId);
	}

    @Override
    public List<MealItem> selectMealItemByShopId(String shopId) {
        return mealitemMapper.selectMealItemByShopId(shopId);
    }

	@Override
	public List<MealItem> selectByArticleId(String articleId) {
		return mealitemMapper.selectByArticleId(articleId);
	}

	@Override
	public int updateArticleNameById(String articleName, Integer id) {
		return mealitemMapper.updateArticleNameById(articleName, id);
	}
}
