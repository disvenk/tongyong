package com.resto.service.shop.service;

import com.resto.api.brand.dto.ArticleSellDto;
import com.resto.conf.util.ApplicationUtils;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.MealAttr;
import com.resto.service.shop.entity.MealItem;
import com.resto.service.shop.mapper.MealAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MealAttrService extends BaseService<MealAttr, Integer> {

    @Autowired
    private MealAttrMapper mealattrMapper;

    @Autowired
	private MealItemService mealItemService;
    
    @Override
    public BaseDao<MealAttr, Integer> getDao() {
        return mealattrMapper;
    }

	public void insertBatch(List<MealAttr> mealAttrs, String article_id) {
		if(mealAttrs==null){
			return ;
		}
		List<MealAttr> oldMealAttr = selectList(article_id);
		List<Integer> ids = new ArrayList<>(ApplicationUtils.convertCollectionToMap(Integer.class, oldMealAttr).keySet());
		if(!ids.isEmpty()){ //先删除所有旧的 mealAttr 
			deleteByIds(ids);
		}
		List<MealItem> mealItems = new ArrayList<>();
		for(MealAttr mealAttr:mealAttrs){
			mealAttr.setArticleId(article_id);
			if(mealAttr.getMealItems()!=null&&mealAttr.getMealItems().size()>0){
				insert(mealAttr);
				for(MealItem mealItem:mealAttr.getMealItems()){
					mealItem.setMealAttrId(mealAttr.getId());
					mealItems.add(mealItem);
				}
			}
		}
		mealItemService.insertBatch(mealItems);
	}

	public void deleteByIds(List<Integer> ids) {
		mealattrMapper.deleteByIds(ids);
		mealItemService.deleteByMealAttrIds(ids);
	}

	public List<MealAttr> selectList(String article_id) {
		return mealattrMapper.selectList(article_id);
	}

	public List<MealAttr> selectFullByArticleId(String articleId,String show) {
		List<MealAttr> list = selectList(articleId);
		if(list.size()>0){
			Map<Integer,MealAttr> attrMap = ApplicationUtils.convertCollectionToMap(Integer.class, list);
			List<Integer> ids = new ArrayList<>(attrMap.keySet());
			List<MealItem> items = mealItemService.selectByAttrIds(ids,show);
			for (MealItem mealItem : items) {
				MealAttr attr = attrMap.get(mealItem.getMealAttrId());
				if(attr.getMealItems()==null){
					attr.setMealItems(new ArrayList<MealItem>());
				}
				attr.getMealItems().add(mealItem);
			}
		}
		return list;
	}

	public List<ArticleSellDto> queryArticleMealAttr(Map<String, Object> selectMap) {
		return mealattrMapper.queryArticleMealAttr(selectMap);
	}
}
