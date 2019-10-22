package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.CategoryOne;
import com.resto.shop.web.dto.CategoryThree;
import com.resto.shop.web.model.MdCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MdCategoryDao extends BaseDao<MdCategory> {

    List<CategoryOne> findCategoryList();

    List<CategoryThree> findCategoryThreeList(@Param("id") Long id, @Param("shopId") String shopId);

    List<MdCategory> queryCategories();

    List<CategoryThree> findCategoryThreeListWithSupPriceId(@Param("id") Long id, @Param("shopId") String shopId);


    List<CategoryThree> findCategoryThreeListWithPmsHeadId(@Param("id") Long id, @Param("shopId") String shopId);
}
