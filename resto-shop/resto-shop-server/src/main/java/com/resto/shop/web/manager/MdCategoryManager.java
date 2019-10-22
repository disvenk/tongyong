package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdCategoryDao;
import com.resto.shop.web.dto.CategoryOne;
import com.resto.shop.web.dto.CategoryThree;
import com.resto.shop.web.model.MdCategory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("mdCategoryManager")
public class MdCategoryManager extends BaseManager<MdCategory> {

    @Resource
    private MdCategoryDao mdCategorydao;

    @Override
    public BaseDao<MdCategory> getDao() {
        return this.mdCategorydao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdCategoryDao.xml",
                MdCategoryDao.class, MdCategory.class, "md_category",false);
    }

    public List<CategoryOne> findCategoryList(){
        return mdCategorydao.findCategoryList();
    }

    public List<CategoryThree> findCategoryThreeList(Long id, String shopId){
        return mdCategorydao.findCategoryThreeList(id, shopId);
    }


    public List<CategoryThree> findCategoryThreeListWithSupPriceId(Long id, String shopId){
        return mdCategorydao.findCategoryThreeListWithSupPriceId(id, shopId);
    }

    public List<MdCategory> findCategories(){
        return mdCategorydao.queryCategories();
    }

    public List<CategoryThree> findCategoryThreeListWithPmsHeadId(Long id, String shopId) {
        return mdCategorydao.findCategoryThreeListWithPmsHeadId(id, shopId);
    }
}
