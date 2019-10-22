package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dto.ArticleSellDto;
import com.resto.shop.web.dao.MealAttrMapper;
import com.resto.shop.web.model.Article;
import com.resto.shop.web.model.MealAttr;
import com.resto.shop.web.model.MealItem;
import com.resto.shop.web.report.MealAttrMapperReport;
import com.resto.shop.web.service.MealAttrService;
import com.resto.shop.web.service.MealItemService;
import com.resto.shop.web.service.PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
@Service
public class MealAttrServiceImpl extends GenericServiceImpl<MealAttr, Integer> implements MealAttrService {

    @Resource
    private MealAttrMapper mealattrMapper;

    @Resource
	private MealAttrMapperReport mealAttrMapperReport;

    @Resource
    MealItemService mealItemService;

	@Autowired
	private PosService posService;
    
    @Override
    public GenericDao<MealAttr, Integer> getDao() {
        return mealattrMapper;
    }

	@Override
	public void insertBatch(List<MealAttr> mealAttrs, String article_id,String brandId ,String shopId) {
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
//		mealItems.forEach(s -> {
//			//消息通知newpos后台发生变化
//			ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
//			shopMsgChangeDto.setBrandId(brandId);
//			shopMsgChangeDto.setShopId(shopId);
//			shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBMEALITEM);
//			shopMsgChangeDto.setType("add");
//			shopMsgChangeDto.setId(s.getId().toString());
//			try{
//				posService.shopMsgChange(shopMsgChangeDto);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		});
	}

	@Override
	public void deleteByIds(List<Integer> ids) {
		mealattrMapper.deleteByIds(ids);
		mealItemService.deleteByMealAttrIds(ids);
	}

	@Override
	public List<MealAttr> selectList(String article_id) {
		return mealattrMapper.selectList(article_id);
	}

	@Override
	public List<MealAttr> selectFullByArticleId(String articleId,String show,Map<String, Article> articleMap) {
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
				if (articleMap == null) {
					attr.getMealItems().add(mealItem);
				}else if(articleMap != null && articleMap.containsKey(mealItem.getArticleId())){
					attr.getMealItems().add(mealItem);
				}
			}
		}
		return list;
	}

	@Override
	public List<ArticleSellDto> queryArticleMealAttr(Map<String, Object> selectMap) {
		return mealAttrMapperReport.queryArticleMealAttr(selectMap);
	}
    @Override
    public List<MealAttr> selectMealAttrByShopId(String shopId) {
        return mealattrMapper.selectMealAttrByShopId(shopId);
    }


}
