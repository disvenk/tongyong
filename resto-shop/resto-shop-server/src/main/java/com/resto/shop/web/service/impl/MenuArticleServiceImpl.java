package com.resto.shop.web.service.impl;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.PinyinUtil;
import com.resto.shop.web.dao.ArticleUnitDetailMapper;
import com.resto.shop.web.dao.ArticleUnitNewMapper;
import com.resto.shop.web.dao.MenuArticleMapper;
import com.resto.shop.web.dao.SupportTimeMapper;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.MealAttrService;
import com.resto.shop.web.service.MealItemService;
import com.resto.shop.web.service.MenuArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.Date;
import java.util.List;

@Component
@Service
public class MenuArticleServiceImpl extends GenericServiceImpl<MenuArticle, Long> implements MenuArticleService {

    @Autowired
    private MenuArticleMapper menuarticleMapper;

    @Autowired
    private ArticleUnitNewMapper articleUnitNewMapper;

    @Autowired
    private ArticleUnitDetailMapper articleUnitDetailMapper;

    @Autowired
    private MealAttrService mealAttrService;

    @Autowired
    private MealItemService mealItemService;

    @Autowired
    private SupportTimeMapper supporttimeMapper;

    @Override
    public GenericDao<MenuArticle, Long> getDao() {
        return menuarticleMapper;
    }

    @Override
    public List<MenuArticle> selectListMenuId(String menuId) {
        List<MenuArticle> menuArticles = menuarticleMapper.selectListMenuId(menuId);
        if (menuArticles!=null && !menuArticles.isEmpty()) {
            for (MenuArticle menuArticle : menuArticles) {
                List<Integer> list = supporttimeMapper.selectByArticleId(menuArticle.getArticleId());
                Integer[]  supportime = list.toArray(new Integer[list.size()]);
                menuArticle.setSupportTimes(supportime);
                if (menuArticle.getArticleType()==1) {
                    List<ArticleUnitNew> articleUnitNews = articleUnitNewMapper.selectArticleUnitNewByArticleId(menuArticle.getArticleId());
                    if (articleUnitNews!=null && articleUnitNews.size()>0){
                        for (ArticleUnitNew articleUnitNew : articleUnitNews) {
                            List<ArticleUnitDetail> details = articleUnitDetailMapper.selectArticleUnitDetailByunitId(articleUnitNew.getId());
                            articleUnitNew.setArticleUnitDetails(details);
                        }
                    }
                    menuArticle.setUnits(articleUnitNews);
                }else if (menuArticle.getArticleType()==2){
                    List<MealAttr> mealAttrs = mealAttrService.selectList(menuArticle.getArticleId());
                    if (mealAttrs!=null && mealAttrs.size()>0){
                        for (MealAttr mealAttr : mealAttrs) {
                            List<MealItem> mealItems = mealItemService.selectByAttrId(mealAttr.getId());
                            mealAttr.setMealItems(mealItems);
                        }
                    }
                    menuArticle.setMealAttrs(mealAttrs);
                }
            }
        }
        return menuArticles;
    }

    @Override
    public int deleteByMenuId(String menuId) {
      return menuarticleMapper.deleteByMenuId(menuId);
    }

    @Override
    public int deleteArticleId(String articleId) {
        return menuarticleMapper.deleteArticleId(articleId);
    }

    @Override
    public void updateMenuArticle(MenuArticle menuarticle) {
        supporttimeMapper.deleteArticleSupportTime(menuarticle.getArticleId());
        if(menuarticle.getSupportTimes()!=null&&menuarticle.getSupportTimes().length>0){
            supporttimeMapper.insertArticleSupportTime(menuarticle.getArticleId(),menuarticle.getSupportTimes());

        }
        if (menuarticle.getArticleType() == 1) {
            //修改单品
            menuarticleMapper.updateByPrimaryKeySelective(menuarticle);
            List<ArticleUnitNew> units = menuarticle.getUnits();
            articleUnitNewMapper.deleteArticleUnitByArticleId(menuarticle.getArticleId());
            for (ArticleUnitNew unit : units) {
                articleUnitDetailMapper.deleteUnitByUnitId(unit.getId());
                unit.setId(ApplicationUtils.randomUUID());
                unit.setArticleId(menuarticle.getArticleId());
                unit.setIsUsed(1);
                articleUnitNewMapper.insertSelective(unit);
                for (ArticleUnitDetail unitDetail : unit.getArticleUnitDetails()) {
                    unitDetail.setId(ApplicationUtils.randomUUID());
                    unitDetail.setArticleUnitId(unit.getId());
                    unitDetail.setIsUsed(1);
                    articleUnitDetailMapper.insertSelective(unitDetail);
                }
            }
        } else if (menuarticle.getArticleType()== 2) {
            //修改套餐
            menuarticleMapper.updateByPrimaryKeySelective(menuarticle);
            mealAttrService.insertBatch(menuarticle.getMealAttrs(), menuarticle.getArticleId(), menuarticle.getBrandId(),null);
        }
    }

    @Override
    public void insertMenuArticle(MenuArticle menuArticle) {
       /* if (menuArticle.getArticleType() == 1) {
            //创建单品
            menuarticleMapper.insertSelective(menuArticle);
            //是否开启菜品库
            //保存规格
            List<ArticleUnitNew> units = menuArticle.getUnits();
            if (units != null && units.size() > 0) {
                for (ArticleUnitNew unit : units) {
                    unit.setId(ApplicationUtils.randomUUID());
                    unit.setArticleId(menuArticle.getArticleId());
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
        } else if (menuArticle.getArticleType() == 2) {
            //创建套餐
            menuarticleMapper.insertSelective(menuArticle);
            mealAttrService.insertBatch(menuArticle.getMealAttrs(), menuArticle.getArticleId(), menuArticle.getBrandId(), null);
        }*/
        menuarticleMapper.insertSelective(menuArticle);
    }
}
