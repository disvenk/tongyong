package com.resto.service.shop.service;


import com.resto.api.brand.define.api.BrandApiShopDetail;
import com.resto.api.brand.dto.ShopDetailDto;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.RecommendCategory;
import com.resto.service.shop.mapper.RecommendCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xielc on 2017/6/29.
 */
@Service
public class RecommendCategoryService extends BaseService<RecommendCategory, String> {

    @Autowired
    private RecommendCategoryMapper recommendCategoryMapper;

    @Autowired
    private BrandApiShopDetail apiShopDetail;

    public BaseDao<RecommendCategory, String> getDao() {
        return recommendCategoryMapper;
    }

    /**
     * 根据店铺ID查询信息,并进行排序
     */
    public List<RecommendCategory> selectListSortShopId(String shopId) {
        ShopDetailDto shopDetail = apiShopDetail.selectByPrimaryKey(shopId);
        if(shopDetail!=null&&shopDetail.getIsRecommendCategory()==1){
            List<RecommendCategory> recommendCategorys = recommendCategoryMapper.selectListSortShopId(shopId);
            return recommendCategorys;
        }
        return null;
    }
}
