package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.Kitchen;
import com.resto.service.shop.entity.OrderItem;
import com.resto.service.shop.mapper.KitchenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenService extends BaseService<Kitchen, Integer> {

    @Autowired
    private KitchenMapper kitchenMapper;

    @Override
    public BaseDao<Kitchen, Integer> getDao() {
        return kitchenMapper;
    }

	public List<Kitchen> selectListByShopId(String shopId) {
		return kitchenMapper.selectListByShopId(shopId);
	}

	public void insertSelective(Kitchen kitchen) {
		kitchenMapper.insertSelective(kitchen);
	}

	public void saveArticleKitchen(String articleId, Integer[] kitchenList) {
		kitchenMapper.deleteArticleKitchen(articleId);
		if(kitchenList!=null&&kitchenList.length>0){
			kitchenMapper.insertArticleKitchen(articleId, kitchenList);;
		}
	}

	public List<Integer> selectIdsByArticleId(String articleId) {
		return kitchenMapper.selectIdsByArticleId(articleId);
	}

	public List<Kitchen> selectInfoByArticleId(String articleId) {
		return kitchenMapper.selectInfoByArticleId(articleId);
	}

	public Kitchen selectMealKitchen(OrderItem mealItems) {
		Kitchen kitchen = kitchenMapper.selectKitchenByMealsItemId(mealItems.getId());
		return kitchen;
	}

	public Kitchen selectKitchenByOrderItem(OrderItem item,List<Long> mealAttrId) {
		return kitchenMapper.selectKitchenByOrderItem(item,mealAttrId);
	}

	public List<Long> getMealAttrId(OrderItem orderItem) {
		return kitchenMapper.getMealAttrId(orderItem);
	}

	public Kitchen getItemKitchenId(OrderItem orderItem) {
		return kitchenMapper.getItemKitchenId(orderItem);
	}

}
