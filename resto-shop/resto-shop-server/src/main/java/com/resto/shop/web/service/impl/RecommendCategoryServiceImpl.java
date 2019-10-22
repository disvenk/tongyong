package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.dao.RecommendCategoryArticleMapper;
import com.resto.shop.web.dao.RecommendCategoryMapper;
import com.resto.shop.web.model.RecommendCategory;
import com.resto.shop.web.model.RecommendCategoryArticle;
import com.resto.shop.web.service.RecommendCategoryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xielc on 2017/6/29.
 */
@Component
@Service
public class RecommendCategoryServiceImpl extends GenericServiceImpl<RecommendCategory, String> implements RecommendCategoryService {

    @Resource
    private RecommendCategoryMapper recommendCategoryMapper;

    @Resource
    private RecommendCategoryArticleMapper recommendCategoryArticleMapper;

    @Resource
    ShopDetailService shopDetailService;

    @Override
    public GenericDao<RecommendCategory, String> getDao() {
        return recommendCategoryMapper;
    }

    /**
     * 根据店铺ID查询信息
     */
    @Override
    public List<RecommendCategory> selectListByShopId(String shopId) {
        List<RecommendCategory> recommendCategorys = recommendCategoryMapper.getRecommendCategoryList(shopId);
        return recommendCategorys;
    }

    /**
     * 根据店铺ID查询信息,并进行排序
     */
    @Override
    public List<RecommendCategory> selectListSortShopId(String shopId) {
        ShopDetail shopDetail = shopDetailService.selectById(shopId);
        if(shopDetail!=null&&shopDetail.getIsRecommendCategory()==1){
            List<RecommendCategory> recommendCategorys = recommendCategoryMapper.selectListSortShopId(shopId);
            return recommendCategorys;
        }
        return null;
    }

    /**
     * 添加 信息
     * @param recommendCategory
     */
    @Override
    public void create(RecommendCategory recommendCategory,String[] articleId,String[] articleName,String[] recommendSort) {
        recommendCategory.setId(ApplicationUtils.randomUUID());
        recommendCategoryMapper.insertSelective(recommendCategory);
        if(articleId!=null && articleId.length>0){
            for(int i = 0; i <articleId.length ; i++){
                RecommendCategoryArticle recommendCategoryArticle = new RecommendCategoryArticle();
                recommendCategoryArticle.setArticleId(articleId[i]);
                recommendCategoryArticle.setArticleName(articleName[i]);
                recommendCategoryArticle.setRecommendSort(Integer.parseInt(recommendSort[i]));
                recommendCategoryArticle.setId(ApplicationUtils.randomUUID());
                recommendCategoryArticle.setRecommendCategoryId(recommendCategory.getId());
                recommendCategoryArticleMapper.insertSelective(recommendCategoryArticle);
            }
        }
    }

    /**
     * 删除信息
     */
    @Override
    public void deleteInfo(String id) {
        recommendCategoryMapper.deleteByPrimaryKey(id);
        recommendCategoryArticleMapper.deleteRecommendCategoryId(id);
    }

    /**
     * 修改信息
     */
    @Override
    public void updateInfo(RecommendCategory recommendCategory,String[] articleId,String[] articleName,String[] recommendSort) {
        recommendCategory.setUpdateTime(new java.sql.Date(System.currentTimeMillis()));
        recommendCategoryMapper.updateByPrimaryKeySelective(recommendCategory);
        //删除菜品信息
        recommendCategoryArticleMapper.deleteRecommendCategoryId(recommendCategory.getId());
        if(articleId!=null && articleId.length>0) {
            for (int i = 0; i < articleId.length; i++) {
                RecommendCategoryArticle recommendCategoryArticle = new RecommendCategoryArticle();
                recommendCategoryArticle.setArticleId(articleId[i]);
                recommendCategoryArticle.setArticleName(articleName[i]);
                recommendCategoryArticle.setRecommendSort(Integer.parseInt(recommendSort[i]));
                recommendCategoryArticle.setId(ApplicationUtils.randomUUID());
                recommendCategoryArticle.setRecommendCategoryId(recommendCategory.getId());
                recommendCategoryArticleMapper.insertSelective(recommendCategoryArticle);
            }
        }
    }
}
