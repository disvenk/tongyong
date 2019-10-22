package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.PinyinUtil;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.constant.ArticleType;
import com.resto.shop.web.dao.*;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.ArticleManageService;
import com.resto.shop.web.service.MealAttrService;
import com.resto.shop.web.service.MealItemService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/11/5/0005 13:52
 * @Description:
 */
@Component
@Service
public class ArticleManageServiceImpl implements ArticleManageService {

    @Autowired
    ArticleLibraryMapper articleLibraryMapper;

    @Autowired
    BrandSettingService brandSettingService;

    @Autowired
    UnitMapper unitMapper;

    @Autowired
    private MealAttrService mealAttrService;

    @Autowired
    MealItemService mealItemService;

    @Autowired
    ArticleUnitNewMapper articleUnitNewMapper;

    @Autowired
    ArticleUnitDetailMapper articleUnitDetailMapper;

    @Autowired
    ArticleFamilyMapper articleFamilyMapper;

    @Autowired
    private SupportTimeMapper supporttimeMapper;


    @Override
    public List<ArticleLibrary> selectAll(String brandId) {
        List<ArticleLibrary> articleLibraries = articleLibraryMapper.selectAll(brandId);
        if (articleLibraries!=null && articleLibraries.size()>0) {
            for (ArticleLibrary articleLibrary : articleLibraries) {
                List<Integer> list = supporttimeMapper.selectByArticleId(articleLibrary.getId());
                Integer[]  supportime = list.toArray(new Integer[list.size()]);
                articleLibrary.setSupportTimes(supportime);
                ArticleFamily family = articleFamilyMapper.selecNametByPrimaryKey(articleLibrary.getArticleFamilyId());
                if (family!=null) {
                    articleLibrary.setArticleFamilyName(family.getName());
                }else {
                    articleLibrary.setArticleFamilyName("");
                }
                if (articleLibrary.getArticleType()==1) {
                    List<ArticleUnitNew> articleUnitNews = articleUnitNewMapper.selectArticleUnitNewByArticleId(articleLibrary.getId());
                    if (articleUnitNews!=null && articleUnitNews.size()>0){
                        for (ArticleUnitNew articleUnitNew : articleUnitNews) {
                            List<ArticleUnitDetail> details = articleUnitDetailMapper.selectArticleUnitDetailByunitId(articleUnitNew.getId());
                            articleUnitNew.setArticleUnitDetails(details);
                        }
                    }
                    articleLibrary.setUnits(articleUnitNews);
                }else if (articleLibrary.getArticleType()==2){
                    List<MealAttr> mealAttrs = mealAttrService.selectList(articleLibrary.getId());
                    if (mealAttrs!=null && mealAttrs.size()>0){
                        for (MealAttr mealAttr : mealAttrs) {
                            List<MealItem> mealItems = mealItemService.selectByAttrId(mealAttr.getId());
                            mealAttr.setMealItems(mealItems);
                        }
                    }
                    articleLibrary.setMealAttrs(mealAttrs);
                }
            }
        }
        return articleLibraries;
    }


    @Override
    public Result deleteArticle(String articleId) {
        ArticleLibrary articleLibrary = articleLibraryMapper.selectByPrimaryKey(articleId);
        if (articleLibrary.getArticleType() == ArticleType.SIMPLE_ARTICLE) {
//            //单品时校验
            List<ArticleLibrary> articleLibraries = articleLibraryMapper.delCheckArticle(articleId);
            if (articleLibraries != null && articleLibraries.size() > 0) {
                StringBuffer mess = new StringBuffer();
                for (ArticleLibrary art : articleLibraries) {
                    mess.append(art.getName() + "，");
                }
                Result result = new Result();
                result.setSuccess(false);
                result.setMessage("删除失败，在" + mess.toString().substring(0, mess.toString().length() - 1) + "套餐存在！");
                result.setStatusCode(100);
                return result;
            }
        }
        articleLibraryMapper.deleteByPrimaryKey(articleId);
        Result result = new Result();
        result.setSuccess(true);
        result.setMessage("删除成功");
        return result;
    }

    @Override
    public Result modifyArticle(ArticleLibrary articlelibrary) {
        Result result = new Result();
        if (articlelibrary.getId()==null){
            result.setMessage("id不能为空");
            result.setSuccess(false);
        }
        if (articlelibrary.getArticleType() == 1) {
            //修改单品
            modifySingle(articlelibrary);
        } else if (articlelibrary.getArticleType() == 2) {
            //修改套餐
            modifyPackage(articlelibrary);
        }
        result.setMessage("修改成功");
        result.setSuccess(true);
        return result;
    }

    @Override
    public int insertSelective(ArticleLibrary record) {

        return articleLibraryMapper.insertSelective(record);
    }

    @Override
    public List<ArticleLibrary> getProductsItem() {

        return articleLibraryMapper.getProductsItem();
    }

    /**
     * 修改单品
     * @param articlelibrary
     */
    private void modifySingle(ArticleLibrary articlelibrary) {
        articlelibrary.setUpdateTime(new Date());
        articleLibraryMapper.updateByPrimaryKeySelective(articlelibrary);
        insertSupportTime(articlelibrary.getId(),articlelibrary.getSupportTimes());
        List<ArticleUnitNew> units = articlelibrary.getUnits();
        if (units != null && units.size() > 0) {
            articleUnitNewMapper.deleteArticleUnitByArticleId(articlelibrary.getId());
            for (ArticleUnitNew unit : units) {
                articleUnitDetailMapper.deleteUnitByUnitId(unit.getId());
                unit.setId(ApplicationUtils.randomUUID());
                unit.setArticleId(articlelibrary.getId());
                unit.setIsUsed(1);
                articleUnitNewMapper.insertSelective(unit);
                for (ArticleUnitDetail unitDetail : unit.getArticleUnitDetails()) {
                    unitDetail.setId(ApplicationUtils.randomUUID());
                    unitDetail.setArticleUnitId(unit.getId());
                    unitDetail.setIsUsed(1);
                    articleUnitDetailMapper.insertSelective(unitDetail);
                }
            }
        }
    }

    /**
     * 修改套餐
     * @param articlelibrary
     */
    private void modifyPackage(ArticleLibrary articlelibrary) {
        articlelibrary.setUpdateTime(new Date());
        articleLibraryMapper.updateByPrimaryKeySelective(articlelibrary);
        insertSupportTime(articlelibrary.getId(),articlelibrary.getSupportTimes());
        mealAttrService.insertBatch(articlelibrary.getMealAttrs(), articlelibrary.getId(), articlelibrary.getBrandId(), articlelibrary.getShopId());
    }


    @Override
    public Result createArticle(ArticleLibrary articlelibrary) {

        if (articlelibrary.getArticleType() == 1) {
            //创建单品
            createSingle(articlelibrary);
        } else if (articlelibrary.getArticleType() == 2) {
            //创建套餐
            createPackage(articlelibrary);
        }
        Result result = new Result();
        result.setMessage("插入成功");
        result.setSuccess(true);
        return result;
    }


    /**
     * 创建单品
     *
     * @param articlelibrary
     */

    private void createSingle(ArticleLibrary articlelibrary) {
        articlelibrary.setId(ApplicationUtils.randomUUID());
        articlelibrary.setInitials(PinyinUtil.getPinYinHeadChar(articlelibrary.getName()));
        articlelibrary.setArticleType(1);
        articlelibrary.setCreateTime(new Date());
        articleLibraryMapper.insertSelective(articlelibrary);
        insertSupportTime(articlelibrary.getId(),articlelibrary.getSupportTimes());
        //是否开启菜品库
        //保存规格
        List<ArticleUnitNew> units = articlelibrary.getUnits();
        if (units != null && units.size() > 0) {
            for (ArticleUnitNew unit : units) {
                unit.setId(ApplicationUtils.randomUUID());
                unit.setArticleId(articlelibrary.getId());
                unit.setIsUsed(1);
                articleUnitNewMapper.insertSelective(unit);
                for (ArticleUnitDetail unitDetail : unit.getArticleUnitDetails()) {
                    unitDetail.setId(ApplicationUtils.randomUUID());
                    unitDetail.setArticleUnitId(unit.getId());
                    unitDetail.setIsUsed(1);
                    articleUnitDetailMapper.insertSelective(unitDetail);
                }
            }
        }
    }

    /**
     * 创建套餐
     *
     * @param articlelibrary
     */
    private void createPackage(ArticleLibrary articlelibrary) {
        articlelibrary.setId(ApplicationUtils.randomUUID());
        articlelibrary.setInitials(PinyinUtil.getPinYinHeadChar(articlelibrary.getName()));
        articlelibrary.setArticleType(2);
        articlelibrary.setCreateTime(new Date());
        articleLibraryMapper.insertSelective(articlelibrary);
        insertSupportTime(articlelibrary.getId(),articlelibrary.getSupportTimes());
        mealAttrService.insertBatch(articlelibrary.getMealAttrs(), articlelibrary.getId(), articlelibrary.getBrandId(), articlelibrary.getShopId());
    }


    /**
     * 插入供应时间
     * @param articleId
     * @param supportTimes
     */
    private void insertSupportTime(String articleId,Integer[] supportTimes){
        supporttimeMapper.deleteArticleSupportTime(articleId);
        if(supportTimes!=null&&supportTimes.length>0){
            supporttimeMapper.insertArticleSupportTime(articleId,supportTimes);

        }
    }
}
