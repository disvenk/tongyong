package com.resto.service.shop.service;

import com.resto.conf.redis.RedisService;
import com.resto.conf.util.ApplicationUtils;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.api.shop.util.Common;
import com.resto.service.shop.entity.*;
import com.resto.service.shop.mapper.ArticleMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleService extends BaseService<Article, String> {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticlePriceService articlePriceServer;
    @Autowired
    private SupportTimeService supportTimeService;
    @Autowired
    private KitchenService kitchenService;
    @Autowired
    private MealAttrService mealAttrService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RecommendCategoryService recommendCategoryService;

    @Override
    public BaseDao<Article, String> getDao() {
        return articleMapper;
    }

    @Override
    public int update(Article article) {
        kitchenService.saveArticleKitchen(article.getId(), article.getKitchenList());
        if (article.getArticleType() == Article.ARTICLE_TYPE_SIGNLE) {
            articlePriceServer.saveArticlePrices(article.getId(), article.getArticlePrices());
        } else if (article.getArticleType() == Article.ARTICLE_TYPE_MEALS) {
            mealAttrService.insertBatch(article.getMealAttrs(), article.getId());
        }
        supportTimeService.saveSupportTimes(article.getId(), article.getSupportTimes());
        return super.update(article);
    }

    public List<Article> selectList(String currentShopId) {
        Map<String, Article> discountMap = selectAllSupportArticle(currentShopId);
        List<Article> articleList = articleMapper.selectList(currentShopId);
        for (Article article : articleList) {
            if (discountMap.containsKey(article.getId())) {
                article.setDiscount(discountMap.get(article.getId()).getDiscount());
            }
        }
        return articleMapper.selectList(currentShopId);
    }

    public List<Article> selectListFull(String currentShopId, Integer distributionModeId, String show) {
        List<Article> articleList = articleMapper.selectListByShopIdAndDistributionId(currentShopId, distributionModeId);
        //当前时间的年月
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateNowStr = sdf.format(d);
        for (Article article : articleList) {
//            article.setMonthlySales(articleMapper.selectSumByMonthlySales(article.getId(), dateNowStr));
//            Integer count = (Integer) RedisUtil.get(article.getId() + Common.KUCUN);
            if(redisService.get(article.getId() + Common.KUCUN)!=null){
                Integer count = Integer.parseInt(redisService.get(article.getId() + Common.KUCUN));
                if (count != null) {
                    article.setCurrentWorkingStock(count);
                }
            }
        }
        getArticleDiscount(currentShopId, articleList, show);
        return articleList;
    }

    public void getArticleDiscount(String shopId, List<Article> articles, String show) {
        Map<String, Article> articleMap = selectAllSupportArticle(shopId);
        for (Article a : articles) {
            if (a.getArticleType() == Article.ARTICLE_TYPE_SIGNLE) {//单品
                if (!StringUtils.isEmpty(a.getHasUnit())) {
                    List<ArticlePrice> prices = articlePriceServer.selectByArticleId(a.getId());
                    for (ArticlePrice price : prices) {
//                        Integer ck = (Integer) RedisUtil.get(price.getId() + Common.KUCUN);
                        if(redisService.get(price.getId() + Common.KUCUN)!=null){
                            Integer ck = Integer.parseInt(redisService.get(price.getId() + Common.KUCUN));
                            if (ck != null) {
                                price.setCurrentWorkingStock(ck);
                            }
                        }
                    }
                    a.setArticlePrices(prices);
                }
            } else if (a.getArticleType() == Article.ARTICLE_TYPE_MEALS) {//套餐
                List<MealAttr> mealAttrs = mealAttrService.selectFullByArticleId(a.getId(), show);
                a.setMealAttrs(mealAttrs);
            }
            if (!articleMap.containsKey(a.getId())) {
                a.setIsEmpty(true);
            } else {
                //设置菜品的折扣百分比
                a.setDiscount(articleMap.get(a.getId()).getDiscount());
            }
        }
    }

    public void addLikes(String articleId) {
        articleMapper.addLikes(articleId);
    }

    private Map<String, Article> selectAllSupportArticle(String currentShopId) {
        List<SupportTime> supportTime = supportTimeService.selectNowSopport(currentShopId);
        if (supportTime.isEmpty()) {
            return new HashMap<>();
        }
        List<Integer> list = new ArrayList<>(ApplicationUtils.convertCollectionToMap(Integer.class, supportTime).keySet());
        List<Article> article = articleMapper.selectBySupportTimeId(list, currentShopId);
        Map<String, Article> articleMap = ApplicationUtils.convertCollectionToMap(String.class, article);
        return articleMap;
    }

    public void updateInitialsById(String initials, String articleId) {
        articleMapper.updateInitialsById(initials, articleId);
    }

    public List<Article> selectArticleList() {
        return articleMapper.selectArticleList();
    }

    public Boolean setEmpty(String articleId) {
        return articleMapper.setEmpty(articleId);
    }

    public Boolean setEmptyFail(String articleId) {
        return articleMapper.setEmptyFail(articleId);
    }

    public void updatePhotoSquare(@Param("id") String id, @Param("photoSquare") String photoSquare) {
        articleMapper.updatePhotoSquare(id, photoSquare);
    }

    public List<Article> selectListByShopIdRecommendCategory(String currentShopId, String recommendCcategoryId, String show) {
        List<Article> articleList = articleMapper.selectListByShopIdRecommendCategory(currentShopId, recommendCcategoryId);
        //当前时间的年月
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateNowStr = sdf.format(d);
        for (Article article : articleList) {
            if(redisService.get(article.getId() + Common.KUCUN)!=null){
                Integer count = Integer.parseInt(redisService.get(article.getId() + Common.KUCUN));
                if (count != null) {
                    article.setCurrentWorkingStock(count);
                }
            }
            article.setMonthlySales(articleMapper.selectSumByMonthlySales(article.getId(), dateNowStr));
            article.setRecommendCategoryId(recommendCcategoryId);
        }
        getArticleDiscount(currentShopId, articleList, show);
        RecommendCategory recommendCategory=recommendCategoryService.selectById(recommendCcategoryId);
        if(recommendCategory!=null){
            if(recommendCategory.getType()==0){
                Collections.sort(articleList, new Comparator<Article>() {
                    public int compare(Article o1, Article o2) {
                        Integer a = o1.getMonthlySales();
                        Integer b = o2.getMonthlySales();
                        // 升序
                        //return a.compareTo(b);
                        // 降序
                        return b.compareTo(a);
                    }
                });
            }
        }
        return articleList;
    }
}
