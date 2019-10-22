package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.MealItem;
import com.resto.service.shop.mapper.MealItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealItemService extends BaseService<MealItem, Integer>{

    @Autowired
    private MealItemMapper mealitemMapper;

    @Override
    public BaseDao<MealItem, Integer> getDao() {
        return mealitemMapper;
    }

	public void deleteByMealAttrIds(List<Integer> mealAttrIds) {
		mealitemMapper.deleteByMealAttrIds(mealAttrIds);
	}

	public void insertBatch(List<MealItem> mealItems) {
		if(mealItems!=null&&mealItems.size()>0){
			mealitemMapper.insertBatch(mealItems);
		}
	}

	public List<MealItem> selectByAttrIds(List<Integer> ids,String show) {
		return mealitemMapper.selectByAttrIds(ids,show);
	}

	public List<MealItem> selectByIds(Integer[] mealItemIds) {
		return mealitemMapper.selectByIds(mealItemIds);
	}

	public List<MealItem> selectByAttrId(Integer attrId) {
		return mealitemMapper.selectByAttrId(attrId);
	}

	public List<MealItem> selectByArticleId(String articleId) {
		return mealitemMapper.selectByArticleId(articleId);
	}

	public int updateArticleNameById(String articleName, Integer id) {
		return mealitemMapper.updateArticleNameById(articleName, id);
	}
}
