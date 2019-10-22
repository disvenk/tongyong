package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.RecommendCategory;

import java.util.List;

/**
 * Created by xielc on 2017/6/29.
 */
public interface RecommendCategoryService extends GenericService<RecommendCategory, String> {

    /**
     * 根据店铺ID查询信息
     * @return
     */
    List<RecommendCategory> selectListByShopId(String shopId);

    /**
     * 根据店铺ID查询信息,并进行排序，微信端接口
     * @return
     */
    List<RecommendCategory> selectListSortShopId(String shopId);

    /**
     * 添加信息
     * @param recommendCategory
     */
    void create(RecommendCategory recommendCategory,String[] articleId,String[] articleName,String[] recommendSort);

    /**
     * 删除信息
     * @param id
     */
    void deleteInfo(String id);

    /**
     * 修改信息
     * @param recommendCategory
     */
    void updateInfo(RecommendCategory recommendCategory,String[] articleId,String[] articleName,String[] recommendSort);
}
