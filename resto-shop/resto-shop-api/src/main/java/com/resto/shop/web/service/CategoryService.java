package com.resto.shop.web.service;



import com.resto.shop.web.baseScm.domain.PageResult;
import com.resto.shop.web.dto.CategoryOne;
import com.resto.shop.web.model.MdCategory;

import java.util.List;

/**
 *
 * 类别管理
 */
public interface CategoryService {


    /***
     * 新增一级分类  二级分类   三级分类
     * @param mdCategory
     * @return
     */


    Integer addCategory(MdCategory mdCategory);



    /**
     *
     *  分业查询一级分类  二级分类   三级分类
     * @param mdCategory
     * @return
     */

    PageResult<MdCategory> query4Page(MdCategory mdCategory);


    /**
     *
     *  根据id查询分类
     * @param id
     * @return
     */
    MdCategory queryById(Long id);

    /**
     *
     *  根据parentId查询 子分类
     * @param parentId
     * @return
     */
    List<MdCategory> queryByParentId(Long parentId);


    /**
     *
     *  修改分类
     * @param mdCategory
     * @return
     */
    Integer updateMdCategory(MdCategory mdCategory);




    Integer deleteById(Long id);


    List<MdCategory> queryByCategoryHierarchy(Integer categoryHierarchy, String shopId);

    List<MdCategory> queryDown(Integer categoryHierarchy, Long id, String shopId);

    List<CategoryOne> queryAll(String shopId, Long supPriceId, Long pmsHeadId);

    List<MdCategory> queryCategories();

}
