package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.resto.api.article.service.NewArticleService;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.dto.ArticleSellDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.RedisService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.ArticleType;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.dao.*;
import com.resto.shop.web.dto.*;
import com.resto.shop.web.model.*;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.report.ArticleMapperReport;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 *
 */
@Component
@Service
public class ArticleServiceImpl extends GenericServiceImpl<Article, String> implements ArticleService {


    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private NewArticleService newArticleService;

    @Resource
    private ArticleMapperReport articleMapperReport;

    @Resource
    private BrandService brandService;

    @Resource
    private ArticlePriceService articlePriceServer;

    @Resource
    private SupportTimeService supportTimeService;

    @Resource
    private KitchenService kitchenService;

    @Resource
    private BrandSettingService brandSettingService;

    @Resource
    private FreeDayMapper freedayMapper;

    @Autowired
    private ArticleFamilyService articleFamilyService;

    @Autowired
    private MealAttrService mealAttrService;

    @Autowired
    private MealItemService mealItemService;

    @Autowired
    private ArticleAttrService articleAttrService;

    @Autowired
    private ArticleUnitService articleUnitService;

    @Autowired
    private ShopDetailService shopDetailService;

    @Resource
    private RecommendCategoryService recommendCategoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderBeforeService orderBeforeService;

    @Autowired
    private PosService posService;

    @Autowired
    private KitchenPrinterDtoMapper kitchenPrinterDtoMapper;

    @Autowired
    private AritclekitchenPrinterDtoMapper aritclekitchenPrinterDtoMapper;

    @Autowired
    private KitchenGroupMapper kitchenGroupMapper;
    @Autowired
    RedisService redisService;

    @Autowired
    MenuArticleService menuArticleService;

    @Autowired
    RecommendCategoryArticleService recommendCategoryArticleService;

    @Autowired
    private MenuShopService menuShopService;


    @Override
    public GenericDao<Article, String> getDao() {
        return articleMapper;
    }

    @Override
    public List<Article> selectList(String currentShopId) {
        Map<String, Article> discountMap = selectAllSupportArticle(currentShopId);
        List<Article> articleList = articleMapper.selectList(currentShopId);
        for (Article article : articleList) {
            if (discountMap.containsKey(article.getId())) {
                article.setDiscount(discountMap.get(article.getId()).getDiscount());
            }
        }
        return articleList;
    }

    @Override
    public List<Article> selectList(String brandId, String currentShopId) {
        ShopDetail shopDetail = shopDetailService.selectShopByStatus(brandId, currentShopId);
        Map<String, Article> discountMap = selectAllSupportArticle(currentShopId);
        List<Article> articleList = articleMapper.selectList(currentShopId);
        for (Article article : articleList) {
            if (discountMap.containsKey(article.getId())) {
                article.setDiscount(discountMap.get(article.getId()).getDiscount());
            }
            if (shopDetail.getEnableDuoDongXian()!=1){
                List<KitchenGroup> kitchenGroups = articleMapper.selectByArticleAndShopId(article.getId(), currentShopId);
                article.setKitchenGroups(kitchenGroups);
            }
        }
        return articleList;
    }

    @Override
    public Article save(Article article,String brandId,String shopId) {
        article.setId(ApplicationUtils.randomUUID());
        this.insert(article);
        ShopDetail shopDetail = shopDetailService.selectShopByStatus(brandId,article.getShopDetailId());
        if (shopDetail.getEnableDuoDongXian()!=1){
            if(article.getKitchenGroupIdList() != null){
                this.saveArticleAndkitchenAndPrinter(article.getId(),brandId,article.getShopDetailId(),article.getKitchenGroupIdList());
            }
        }else {
            kitchenService.saveArticleKitchen(article.getId(), article.getKitchenList(),brandId,shopId);
        }
        if (article.getArticleType() == Article.ARTICLE_TYPE_SIGNLE) {
            articlePriceServer.saveArticlePrices(article.getId(), article.getArticlePrices(),brandId,shopId);
        } else if (article.getArticleType() == Article.ARTICLE_TYPE_MEALS) {
            mealAttrService.insertBatch(article.getMealAttrs(), article.getId(),brandId,shopId);
            article.getMealAttrs().forEach(s -> {
                //消息通知newpos后台发生变化
                ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
                shopMsgChangeDto.setBrandId(brandId);
                shopMsgChangeDto.setShopId(shopId);
                shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBMEALATTR);
                shopMsgChangeDto.setType("add");
                shopMsgChangeDto.setId(s.getId().toString());
                try{
                    posService.shopMsgChange(shopMsgChangeDto);
                }catch(Exception e){
                    e.printStackTrace();
                }
            });
        }
        supportTimeService.saveSupportTimes(article.getId(), article.getSupportTimes(),brandId,shopId);
        return article;
    }

    @Override
    public void saveArticleAndkitchenAndPrinter(String articleId,String brandId,String shopId,List<Integer> kitchenGroupIds) {
        AritclekitchenPrinterDto aritclekitchenPrinterDto = new AritclekitchenPrinterDto();
        aritclekitchenPrinterDto.setArticleId(articleId);
        aritclekitchenPrinterDto.setShopId(shopId);
        aritclekitchenPrinterDto.setBrandId(brandId);
        kitchenGroupIds.forEach(kitchenGroupId -> {
              aritclekitchenPrinterDto.setKitchenGroupId(kitchenGroupId);
            aritclekitchenPrinterDtoMapper.insertSelective(aritclekitchenPrinterDto);
        });
    }
    @Override
    public void upDateArticleAndkitchenAndPrinter(String articleId,String brandId,String shopId,List<Integer> kitchenGroupIds) {
        aritclekitchenPrinterDtoMapper.deleteByArticleId(articleId,shopId);
        this.saveArticleAndkitchenAndPrinter(articleId,brandId,shopId,kitchenGroupIds);
    }

    @Override
    public int update(Article article,String brandId,String shopId) {
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(article.getShopDetailId());
        if (shopDetail.getEnableDuoDongXian()!=1 ){
            if (article.getKitchenGroupIdList()!=null){
                this.upDateArticleAndkitchenAndPrinter(article.getId(),brandId,article.getShopDetailId(),article.getKitchenGroupIdList());
            }
        }else{
            kitchenService.saveArticleKitchen(article.getId(), article.getKitchenList(),brandId,shopId);
        }
        if (article.getArticleType() == Article.ARTICLE_TYPE_SIGNLE) {
            articlePriceServer.saveArticlePrices(article.getId(), article.getArticlePrices(),brandId,shopId);
        } else if (article.getArticleType() == Article.ARTICLE_TYPE_MEALS) {
            mealAttrService.insertBatch(article.getMealAttrs(), article.getId(),brandId,shopId);
            article.getMealAttrs().forEach(s -> {
                //消息通知newpos后台发生变化
                ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
                shopMsgChangeDto.setBrandId(brandId);
                shopMsgChangeDto.setShopId(shopId);
                shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBMEALATTR);
                shopMsgChangeDto.setType("modify");
                shopMsgChangeDto.setId(s.getId().toString());
                try{
                    posService.shopMsgChange(shopMsgChangeDto);
                }catch(Exception e){
                    e.printStackTrace();
                }
            });
        }
        supportTimeService.saveSupportTimes(article.getId(), article.getSupportTimes(),brandId,shopId);
        return super.update(article);
    }

    @Override
    public Article selectFullById(String id, String show) {
        Article article = selectById(id);
        List<Integer> kitchenList = kitchenService.selectIdsByArticleId(id);
        article.setKitchenList(kitchenList.toArray(new Integer[0]));
        if (article.getArticleType() == Article.ARTICLE_TYPE_SIGNLE) {
            List<ArticlePrice> prices = articlePriceServer.selectByArticleId(id);
            article.setArticlePrices(prices);
        } else {
            List<MealAttr> mealAttrs = mealAttrService.selectFullByArticleId(id, show,null);
            article.setMealAttrs(mealAttrs);
        }
        List<Integer> supportTimesIds = supportTimeService.selectByIdsArticleId(id);
        article.setSupportTimes(supportTimesIds.toArray(new Integer[0]));
        return article;
    }
    @Override
    public Article selectFullById(String id, String show, String brandId, String shopId) {
        Article article = selectById(id);
        List<Integer> kitchenList = kitchenService.selectIdsByArticleId(id);
        if(kitchenList != null){
            article.setKitchenList(kitchenList.toArray(new Integer[0]));
        }
        if (article.getArticleType() == Article.ARTICLE_TYPE_SIGNLE) {
            List<ArticlePrice> prices = articlePriceServer.selectByArticleId(id);
            article.setArticlePrices(prices);
        } else {
            List<MealAttr> mealAttrs = mealAttrService.selectFullByArticleId(id, show,null);
            article.setMealAttrs(mealAttrs);
        }
        List<Integer> supportTimesIds = supportTimeService.selectByIdsArticleId(id);
        article.setSupportTimes(supportTimesIds.toArray(new Integer[0]));
        return getArticle(brandId, shopId, article);
    }
    @Override
    public List<Article> selectListFull(String currentShopId, Integer distributionModeId, String show) {
        List<Article> articleList = articleMapper.selectListByShopIdAndDistributionId(currentShopId, distributionModeId);
        //当前时间的年月
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateNowStr = sdf.format(d);
        for (Article article : articleList) {
//            article.setMonthlySales(articleMapper.selectSumByMonthlySales(article.getId(), dateNowStr));
//            Integer count = (Integer) redisService.get(article.getId() + Common.KUCUN);
            Integer count = (Integer) redisService.get(article.getId() + Common.KUCUN);
            if (count != null) {
                article.setCurrentWorkingStock(count);
            }
            //套餐子品取redis库存   套餐不拿redis库存
            if (article.getArticleType() == 2){
                article.setCurrentWorkingStock(article.getCurrentWorkingStock());
                article.setIsEmpty(false);
            }
            if("".equals(article.getRecommendId())){
                article.setRecommendId(null);
                article.setRecommendCount(0);
            }
        }
        getArticleDiscount(currentShopId, articleList, show);
        return articleList;
    }

    @Override
    public List<Article> selectListByShopIdRecommendCategory(String currentShopId, String recommendCcategoryId, String show) {
        List<Article> articleList = articleMapper.selectListByShopIdRecommendCategory(currentShopId, recommendCcategoryId);
        //当前时间的年月
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateNowStr = sdf.format(d);
        for (Article article : articleList) {
            Integer count = (Integer) redisService.get(article.getId() + Common.KUCUN);
            if (count != null) {
                article.setCurrentWorkingStock(count);
            }
            article.setMonthlySales(articleMapper.selectSumByMonthlySales(article.getId(), dateNowStr));
            article.setRecommendCategoryId(recommendCcategoryId);
            if("".equals(article.getRecommendId())){
                article.setRecommendId(null);
                article.setRecommendCount(0);
            }
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

    @Override
    public List<Article> getArticleListByFamily(String shopId, String articleFamilyId, Integer currentPage, Integer showCount) {
        List<SupportTime> supportTime = supportTimeService.selectNowSopport(shopId);
        if (supportTime.isEmpty()) {
            return null;
        }
        List<Integer> list = new ArrayList<>(ApplicationUtils.convertCollectionToMap(Integer.class, supportTime).keySet());
        List<Article> articles = articleMapper.getArticleListByFamily(list, shopId, articleFamilyId, currentPage, showCount);
        getArticleDiscount(shopId, articles, "wechat");
        return articles;
    }

    @Override
    public void updateInitialsById(String initials, String articleId) {
        articleMapper.updateInitialsById(initials, articleId);
    }

    @Override
    public List<Article> selectArticleList() {
        return articleMapper.selectArticleList();
    }

    public void getArticleDiscount(String shopId, List<Article> articles, String show) {
        Map<String, Article> articleMap = selectAllSupportArticle(shopId);
        for (Article a : articles) {
            if (a.getArticleType() == Article.ARTICLE_TYPE_SIGNLE) {//单品
                if (!StringUtils.isEmpty(a.getHasUnit())) {
                    List<ArticlePrice> prices = articlePriceServer.selectByArticleId(a.getId());
                    for (ArticlePrice price : prices) {
//                        Integer ck = (Integer) redisService.get(price.getId() + Common.KUCUN);
                        Integer ck = (Integer) redisService.get(price.getId() + Common.KUCUN);
                        if (ck != null) {
                            price.setCurrentWorkingStock(ck);
                        }
                    }
                    a.setArticlePrices(prices);
                }
            } else if (a.getArticleType() == Article.ARTICLE_TYPE_MEALS) {//套餐
                List<MealAttr> mealAttrs = mealAttrService.selectFullByArticleId(a.getId(), show, articleMap);
                if(mealAttrs.size() != 0){
                    for (MealAttr mealAttr : mealAttrs) {
                        if(mealAttr != null){
                            if (mealAttr.getMealItems()!=null&&mealAttr.getChoiceType()!=null&&mealAttr.getMealItems().size()==0&&mealAttr.getChoiceType()==0){
                                a.setIsEmpty(true);
                            }
                        }
                    }
                    a.setMealAttrs(mealAttrs);
                }
            }
            if (!articleMap.containsKey(a.getId())) {
                a.setIsEmpty(true);
            } else {
                //设置菜品的折扣百分比
                a.setDiscount(articleMap.get(a.getId()).getDiscount());
            }
        }
    }

    @Override
    public int delete(String id) {
        Article article = selectById(id);
        article.setState(false);
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(article.getShopDetailId());
        // 判断是否开启多动线
        if (shopDetail.getEnableDuoDongXian()!=1){
            aritclekitchenPrinterDtoMapper.deleteByArticleId(id,article.getShopDetailId());
        }
        return update(article);
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

    @Override
    public List<Article> selectListByIsEmpty(Integer isEmpty, String shopId) {
        return articleMapper.selectListByIsEmpty(isEmpty, shopId);
    }

    @Override
    public void changeEmpty(Integer isEmpty, String articleId) {
        articleMapper.changeEmpty(isEmpty, articleId);
    }

    @Override
    public void addLikes(String articleId) {
        articleMapper.addLikes(articleId);
    }

    @Override
    public void updateLikes(String articleId, Long likes) {
        articleMapper.updateLikes(articleId, likes);
    }


    @Override
    public void initStock() {
        /**
         * 餐品套餐库存 默认为 最低的单品
         */
        //多规格商品 库存之和 等于该品库存
//        articleMapper.initSuitStock();
        articleMapper.initSize();
    }

    @Override
    public List<ArticleStock> getStock(String shopId, String familyId, Integer empty, Integer activated) {
        FreeDay day = freedayMapper.selectByDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), shopId);
        int freeDay = 0;
        if (day == null) {
            freeDay = 1;
        }
        List<ArticleStock> result = articleMapper.getStock(shopId, familyId, empty, freeDay, activated);
        List<ArticleStock> array = new ArrayList<>();
        for (ArticleStock articleStock : result) {

//            Integer ck = (Integer) RedisUtil.get(articleStock.getId() + Common.KUCUN);
            Integer ck = (Integer) redisService.get(articleStock.getId() + Common.KUCUN);
            if (ck != null) {
                articleStock.setCurrentStock(ck);
            }
            if (empty != null && empty == Common.YES) {
                //售罄
                if (articleStock.getCurrentStock() == 0) {
                    array.add(articleStock);
                }
            } else {
                array.add(articleStock);
            }

        }
        return array;
    }

    @Override
    public Boolean clearStock(String articleId, String shopId) {
        Article article = null;
        ShopDetail shopDetail = shopDetailService.selectById(shopId);
        Brand brand = brandService.selectById(shopDetail.getBrandId());
        String emptyRemark = "【手动沽清】";
//        redisService.set(articleId + Common.KUCUN, 0);
        redisService.set(articleId + Common.KUCUN, 0);
        String baseArticleId = articleId;
        if (articleId.indexOf("@") > -1) {
            String aid = articleId.substring(0, articleId.indexOf("@"));
            article = articleMapper.selectByPrimaryKey(aid);
            articlePriceServer.clearPriceStock(articleId, emptyRemark);
            baseArticleId = aid;

        } else {
            article = articleMapper.selectByPrimaryKey(articleId);
            articleMapper.clearStock(articleId, emptyRemark);
            articlePriceServer.clearPriceTotal(articleId, emptyRemark);
        }

//        List<ArticlePrice> articlePrices = articlePriceServer.selectByArticleId(baseArticleId);
//        int sum = 0;
//        if (!CollectionUtils.isEmpty(articlePrices)) {
//            for (ArticlePrice articlePrice : articlePrices) {
//                Integer ck = (Integer) redisService.get(articlePrice.getId() + Common.KUCUN);
//                if (ck != null) {
//                    sum += ck;
//                } else {
//                    sum += articlePrice.getCurrentWorkingStock();
//                }
//            }
//            redisService.set(baseArticleId + Common.KUCUN, sum);
//            if (sum == 0) {
//                articleMapper.setEmpty(baseArticleId);
//            } else {
//                articleMapper.setEmptyFail(baseArticleId);
//            }
//        }


//        List<Article> taocan = orderMapper.getStockBySuit(shopDetail.getId());
//        for(Article tc : taocan){
//            Integer suit = (Integer) redisService.get(tc.getId()+Common.KUCUN);
//            if(suit != null){
//                if(suit == 0 && tc.getCount() > 0){
//                    orderMapper.setEmptyFail(tc.getId());
//                }
//                redisService.set(tc.getId()+Common.KUCUN,tc.getCount());
//            }else{
//                if(tc.getIsEmpty() && tc.getCount() > 0){
//                    orderMapper.setEmptyFail(tc.getId());
//                }
//                redisService.set(tc.getId()+Common.KUCUN,tc.getCount());
//            }
//        }


//        articleMapper.cleanPriceAll(articleId,emptyRemark);//方法重复
        //如果有规格的

//        orderMapper.setStockBySuit(shopId);
//        articleMapper.initSizeCurrent();
//        articleMapper.clearMain(articleId, emptyRemark);

        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "店铺:" + shopDetail.getName() + "在pos端沽清了菜品(" + article.getName() + ")Id为:" + articleId + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        return true;
    }

    @Override
    public Boolean editStock(String articleId, Integer count, String shopId) {
        Article article = null;
        String baseArticleId = articleId;
        Boolean moreType = false;
        String aid = articleId;
        if (articleId.indexOf("@") > -1) {
            moreType = true;
            aid = articleId.substring(0, articleId.indexOf("@"));
            article = articleMapper.selectByPrimaryKey(aid);
        } else {
            article = articleMapper.selectByPrimaryKey(articleId);
        }
        ShopDetail shopDetail = shopDetailService.selectById(shopId);
        Brand brand = brandService.selectById(shopDetail.getBrandId());
        String emptyRemark = count <= 0 ? "【手动沽清】" : null;
//        redisService.set(articleId + Common.KUCUN, count);
            redisService.set(articleId + Common.KUCUN, count);
        if (article.getIsEmpty()) {
            if (moreType && count > 0) {
                articlePriceServer.setArticlePriceEmptyFail(baseArticleId);
            } else if (!moreType && count > 0) {
                articleMapper.setEmptyFail(articleId);
            }
        } else {
            if (moreType && count == 0) {
                articlePriceServer.setArticlePriceEmpty(baseArticleId);
            } else if (!moreType && count == 0) {
                articleMapper.setEmpty(articleId);
            } else if (moreType && count > 0) {
                articlePriceServer.setArticlePriceEmptyFail(baseArticleId);
            } else if (!moreType && count > 0) {
                articleMapper.setEmptyFail(articleId);
            }
        }

        List<ArticlePrice> articlePrices = articlePriceServer.selectByArticleId(aid);
        int sum = 0;
        if (!CollectionUtils.isEmpty(articlePrices)) {
            for (ArticlePrice articlePrice : articlePrices) {
                Integer ck = (Integer) redisService.get(articlePrice.getId() + Common.KUCUN);
                if (ck != null) {
                    sum += ck;
                } else {
                    sum += articlePrice.getCurrentWorkingStock();
                }
            }
            redisService.set(aid + Common.KUCUN, sum);
            if (sum == 0) {
                articleMapper.setEmpty(aid);
            } else {
                articleMapper.setEmptyFail(aid);
            }
        }


//        List<Article> taocan = orderMapper.getStockBySuit(shopDetail.getId());
//        for(Article tc : taocan){
//            Integer suit = (Integer) redisService.get(tc.getId()+Common.KUCUN);
//            if(suit != null){
//                if(suit == 0 && tc.getCount() > 0){
//                    orderMapper.setEmptyFail(tc.getId());
//                }
//                redisService.set(tc.getId()+Common.KUCUN,tc.getCount());
//            }else{
//                if(tc.getIsEmpty() && tc.getCount() > 0){
//                    orderMapper.setEmptyFail(tc.getId());
//                }
//                redisService.set(tc.getId()+Common.KUCUN,tc.getCount());
//            }
//        }
//        articleMapper.editStock(articleId, count, emptyRemark);
//        articleMapper.editPriceStock(articleId, count, emptyRemark);
//        orderMapper.setStockBySuit(shopId);
//        articleMapper.initSizeCurrent();

//        articleMapper.initEmpty();
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        map.put("content", "店铺:" + shopDetail.getName() + "修改菜品(" + article.getName() + ")Id为:" + articleId + "的库存为:" + count + ",请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        return true;
    }

    @Override
    public Boolean setActivated(String articleId, Integer activated) {
        Article article = articleMapper.selectByPrimaryKey(articleId);
        ShopDetail shopDetail = shopDetailService.selectById(article.getShopDetailId());
        Brand brand = brandService.selectById(shopDetail.getBrandId());
        int row = articleMapper.setActivate(articleId, activated);
        Map map = new HashMap(4);
        map.put("brandName", brand.getBrandName());
        map.put("fileName", shopDetail.getName());
        map.put("type", "posAction");
        if (activated.equals(0)) {
            map.put("content", "店铺:" + shopDetail.getName() + "在pos端下架了菜品(" + article.getName() + ")Id为:" + articleId + ",请求服务器地址为:" + MQSetting.getLocalIP());
        } else {
            map.put("content", "店铺:" + shopDetail.getName() + "在pos端上架了菜品(" + article.getName() + ")Id为:" + articleId + ",请求服务器地址为:" + MQSetting.getLocalIP());
        }
        doPostAnsc(map);
        return row > 0 ? true : false;
    }

    @Override
    public List<Article> getSingoArticle(String shopId) {
        return articleMapper.getSingoArticle(shopId);
    }

    @Override
    public List<Article> getSingoArticleAll(String shopId) {
        return articleMapper.getSingoArticleAll(shopId);
    }

    @Override
    public void deleteRecommendId(String recommendId) {
        articleMapper.deleteRecommendId(recommendId);
    }


    @Override
    public void assignArticle(String[] shopList, String[] articleList) {
        for (String articleId : articleList) { //遍历要复制的菜品
            Article article = articleMapper.selectByPrimaryKey(articleId); //得到要复制的菜品

            //得到要复制的菜品分类
            ArticleFamily articleFamily = articleFamilyService.selectById(article.getArticleFamilyId());
            //循环店铺
            for (String shopId : shopList) {
                //将菜品的店铺设置为要复制的店铺
                article.setId(ApplicationUtils.randomUUID());
                article.setShopDetailId(shopId);

                //判断该店铺下是否已有菜品分类
                ArticleFamily family = articleFamilyService.checkSame(shopId, articleFamily.getName());
                if (family == null) {
                    //如果没有,那么生成
                    String familyId = ApplicationUtils.randomUUID();
                    articleFamily.setId(familyId);
                    articleFamily.setShopDetailId(shopId);
                    articleFamilyService.copyBrandArticleFamily(articleFamily);
                    article.setArticleFamilyId(familyId);
                } else {
                    //如果有,那么覆盖
                    //todo
                    articleFamily.setId(family.getId());
                    articleFamily.setShopDetailId(shopId);
                    articleFamilyService.update(articleFamily);
                    article.setArticleFamilyId(family.getId());
                }

                //判断下是不是有规格单品
                List<ArticleAttr> articleAttrs = articleAttrService.selectListByArticleId(articleId);
                StringBuilder hasUnit = new StringBuilder();

                if (!CollectionUtils.isEmpty(articleAttrs)) {
                    for (ArticleAttr articleAttr : articleAttrs) {
                        //得到要复制的规格
                        List<ArticleUnit> articleUnits = articleUnitService.selectListByAttrId(articleAttr.getId());
                        ArticleAttr same = articleAttrService.selectSame(articleAttr.getName(), shopId);
                        //不存在相同规格属性
                        if (same == null) {
                            articleAttr.setId(null);
                            articleAttr.setShopDetailId(shopId);
                            articleAttrService.insertByAuto(articleAttr);
                        } else {
                            //存在相同规格属性,覆盖
                            articleAttr.setId(same.getId());
                            articleAttr.setShopDetailId(shopId);
                            articleAttrService.update(articleAttr);
                        }
                        for (ArticleUnit articleUnit : articleUnits) {
                            ArticlePrice articlePrice = articlePriceServer.selectByArticle(articleId, articleUnit.getId());
                            if (articlePrice == null) {
                                continue;
                            }
                            ArticleUnit sameUnit = articleUnitService.selectSame(articleUnit.getName(), articleAttr.getId().toString());
                            if (sameUnit == null) {
                                articleUnit.setTbArticleAttrId(articleAttr.getId());
                                articleUnit.setId(null);
                                articleUnitService.insertByAuto(articleUnit);
                            } else {
                                articleUnit.setId(sameUnit.getId());
                                articleUnit.setTbArticleAttrId(articleAttr.getId());
                                articleUnitService.update(articleUnit);
                            }
                            hasUnit.append(articleUnit.getId()).append(",");

                            ArticlePrice copy = articlePriceServer.selectById(article.getId() + "@" + articlePrice.getUnitIds());
                            if (copy != null) {
                                articlePrice.setArticleId(article.getId());
                                articlePrice.setUnitIds(articleUnit.getId().toString());
                                articlePrice.setId(article.getId() + "@" + articlePrice.getUnitIds());
                                articlePriceServer.update(articlePrice);
                            } else {
                                articlePrice.setArticleId(article.getId());
                                articlePrice.setUnitIds(articleUnit.getId().toString());
                                articlePrice.setId(article.getId() + "@" + articlePrice.getUnitIds());
                                articlePriceServer.insert(articlePrice);
                            }
                        }
                    }
                }
                if (!StringUtils.isEmpty(hasUnit.toString())) {
                    article.setHasUnit(hasUnit.toString().substring(0, hasUnit.length() - 1));
                }
                article.setpId(articleId);
                //判断要复制的菜品是否已经在该店铺下生成过
                Article copy = articleMapper.selectByPid(articleId, shopId);
                if (copy != null) {
                    //如果生成过,那么覆盖
                    //todo
                    article.setId(copy.getId());
                    articleMapper.updateByPrimaryKeySelective(article);
                } else {
                    article.setVirtualId(null);
                    articleMapper.insert(article);
                }
            }
        }
    }

    @Override
    public void assignTotal(String[] shopList, String[] articleList) {
        for (String articleId : articleList) { //遍历菜品
            Article article = articleMapper.selectByPrimaryKey(articleId); //得到菜品
            //循环店铺
            for (String shopId : shopList) {
                if (article.getArticleType().equals(ArticleType.TOTAL_ARTICLE)) { //套餐
                    //如果是套餐的话，先获取套餐下的全部单品
                    List<Article> articles = articleMapper.getArticleByMeal(articleId);
                    for (Article art : articles) {
                        art.setShopDetailId(shopId);
                        art.setpId(art.getId());
                        //得到菜品分类
                        ArticleFamily family = articleFamilyService.selectById(art.getArticleFamilyId());
                        ArticleFamily articleFamily = articleFamilyService.checkSame(shopId, family.getName());
                        if (articleFamily == null) {
                            //每个店铺生成自己的菜品分类
                            String familyId = ApplicationUtils.randomUUID();
                            family.setId(familyId);
                            family.setShopDetailId(shopId);
                            articleFamilyService.copyBrandArticleFamily(family);
                            art.setArticleFamilyId(familyId);
                        } else {
                            articleFamily.setId(articleFamily.getId());
                            articleFamily.setShopDetailId(shopId);
                            articleFamilyService.update(articleFamily);
                            art.setArticleFamilyId(articleFamily.getId());
                        }

                        //判断每个单品是不是全部已经引入
                        Article copy = articleMapper.selectByPid(art.getId(), shopId);
                        if (copy != null) {
                            //如果生成过,那么覆盖
                            //todo
                            art.setId(copy.getId());
                            articleMapper.updateByPrimaryKeySelective(art);

                        } else {
                            art.setId(ApplicationUtils.randomUUID());
                            article.setVirtualId(null);
                            articleMapper.insert(art);
                        }
                    }
//                    //得到要复制的套餐属性
                    List<MealAttr> attrs = mealAttrService.selectList(articleId);
                    for (MealAttr attr : attrs) {
                        //循环旧的套餐属性
                        List<MealItem> mealItems = mealItemService.selectByAttrId(attr.getId());
                        attr.setId(null);
                        attr.setArticleId(articleMapper.selectByPid(article.getId(), shopId).getId());
                        mealAttrService.insert(attr);
                        for (MealItem mealItem : mealItems) {
                            mealItem.setMealAttrId(attr.getId());
                            mealItem.setArticleId(articleMapper.selectByPid(mealItem.getArticleId(), shopId).getId());
                            mealItem.setId(null);
                            mealItemService.insert(mealItem);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<Article> delCheckArticle(String id) {
        return articleMapper.delCheckArticle(id);
    }

    @Override
    public void updatePhotoSquare(@Param("id") String id, @Param("photoSquare") String photoSquare) {
        articleMapper.updatePhotoSquare(id, photoSquare);
    }

    @Override
    public void updateArticleImg(Article article) {
        articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public void addArticleLikes(String articleId) {
        articleMapper.addArticleLikes(articleId);
    }

    @Override
    public List<Article> selectsingleItem(String shopId) {
        return articleMapper.selectsingleItem(shopId);
    }

    @Override
    public List<ArticleSellDto> callOrderArtcile(Map<String, Object> selectMap) {
        return articleMapperReport.queryOrderArtcile(selectMap);
    }

    @Override
    public Article selectByPrimaryKey(String articleId) {
        return articleMapper.selectByPrimaryKey(articleId);
    }

    @Override
    public List<ArticleSellDto> selectArticleByType(Map<String, Object> selectMap) {
        return articleMapperReport.selectArticleByType(selectMap);
    }

    @Override
    public Map<String, Object> callArticleOrderCount(Map<String, Object> selectMap) {
        return articleMapperReport.selectArticleOrderCount(selectMap);
    }

    @Override
    public List<String> selectArticleSort(Map<String, Object> selectMap) {
        return articleMapper.selectArticleSort(selectMap);
    }

    @Override
    public List<Article> selectnewPosListByFamillyId(String shopId, Integer page, Integer size, String familyId) {
        List<SupportTime> supportTime = supportTimeService.selectNowSopport(shopId);
        if (supportTime.isEmpty()) {
            return null;
        }
        List<Integer> list = new ArrayList<>(ApplicationUtils.convertCollectionToMap(Integer.class, supportTime).keySet());
        PageHelper.startPage(page, size);
        List<Article> articleList = articleMapper.selectnewPosListByFamillyId(list, shopId, familyId);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return pageInfo.getList();

    }

    @Override
    public List<Article> selectHasResourcePhotoList(String currentBrandId) {
        return articleMapper.selectHasResourcePhotoList(currentBrandId);
    }

    @Override
    public Boolean setEmpty(String articleId) {
        return articleMapper.setEmpty(articleId);
    }

    @Override
    public Boolean setEmptyFail(String articleId) {
        return articleMapper.setEmptyFail(articleId);
    }
    @Override
    public List<Article> selectArticleByShopId(String shopId) {
        return articleMapper.selectArticleByShopId(shopId);
    }


    @Override
    public List<ArticleSellCountDto> findArticleByLastCountTime(String shopId,String lastCountTime) {
        return articleMapper.findArticleByLastCountTime(shopId,lastCountTime);
    }

    @Override
    public List<com.resto.api.article.entity.Article> getArticleBefore(String shopId,String tableNumber,String customerId) {
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        List<com.resto.api.article.entity.Article> articleList = newArticleService.getArticleBefore(shopDetail.getBrandId(),shopId);
        Map<String, com.resto.api.article.entity.Article> discountMap = newArticleService.selectAllSupportArticle(shopDetail.getBrandId(),shopId);
        for (com.resto.api.article.entity.Article article : articleList) {
            if (discountMap.containsKey(article.getId())) {
                article.setDiscount(discountMap.get(article.getId()).getDiscount());
            }
        }
        BrandSetting brandSetting = brandSettingService.selectByBrandId(shopDetail.getBrandId());
        //判断是否开启预点餐功能
        if(shopDetail.getOrderBefore() == Common.NO  || brandSetting.getOrderBefore() == Common.NO){
            return null;
        }
        if(tableNumber == null){
            return articleList;
        }else {



            //然后判断能不能加菜
            if(shopDetail.getOpenManyCustomerOrder() == Common.NO || brandSetting.getOpenManyCustomerOrder() == Common.NO){
                //先去判断,这个桌子上存不存在未支付的预点餐商品
                OrderBefore orderBefore = orderBeforeService.getOrderNoPay(tableNumber,shopId,customerId);
                if(orderBefore != null){
                    //存在未支付的预点餐餐品
                    return null;
                }
                Order order = orderService.lastOrderByCustomer(customerId,shopId, null);
                if(order == null){
                    return articleList;
                }else if (!order.getAllowContinueOrder()){
                    return articleList;
                }else{
                    return null;
                }
            }else{
                //先去判断,这个桌子上存不存在未支付的预点餐商品
                OrderBefore orderBefore = orderBeforeService.getOrderNoPay(tableNumber,shopId,null);
                if(orderBefore != null){
                    //存在未支付的预点餐餐品
                    return null;
                }
                //如果开启了多人点餐，先去找这个人是不是在组里
                //先判断用户是否在一个已支付的组里了
                TableGroup groupList = tableGroupService.getTableGroupByState(shopId,customerId, tableNumber, 1);
                if(groupList != null){
                    //只有用户在已支付的组内才有可能可以加菜
                    Order order = orderService.lastOrderByCustomerGroupId(customerId,shopId,groupList.getGroupId(),null);
                    if(order == null){
                        return articleList;
                    }else if (!order.getAllowContinueOrder()){
                        return articleList;
                    }else{
                        return null;
                    }

                }

            }

        }
        return articleList;
    }

    @Override
    public List<KitchenDto> selectTree(String brandId,String shopId) {
        List<Kitchen> kitchens = kitchenService.selectListByShopIdByStatus(brandId,shopId);
        List<KitchenDto> kitchenDtos = new ArrayList<>();
        if(kitchens!=null){
            for (Kitchen kitchen:kitchens){
                KitchenDto kitchenDto = new KitchenDto();
                kitchenDto.setJsonDtoId(kitchen.getId());
                kitchenDto.setName(kitchen.getName());
                //查询已开启状态的厨房
                List<kitchenPrinterDto> kitchenPrinterDtos = kitchenPrinterDtoMapper.selectByKitchenAndShopIdAndStatus(kitchen.getId(), shopId);
                ArrayList<PrinterDto> printerDtos = new ArrayList<>();
                if(kitchenPrinterDtos!=null){
                    for (kitchenPrinterDto kitchenPrinterDto:kitchenPrinterDtos) {
                        PrinterDto printerDto = new PrinterDto();
                        printerDto.setJsonDtoId(kitchenPrinterDto.getPrinterId());
                        printerDto.setName(kitchenPrinterDto.getPrinterName());
                        printerDto.setKitchenName(kitchen.getName());
                        printerDto.setKitchenId(kitchenPrinterDto.getKitchenId());
                        printerDtos.add(printerDto);
                    }
                }
                kitchenDto.setChildren(printerDtos);
                kitchenDtos.add(kitchenDto);
            }
        }
        return kitchenDtos;
    }



    @Override
    public void updateCurrentWorkingStock(){
        articleMapper.updateCurrentWorkingStock();//恢复菜品当前库存为工作日库存
        articleMapper.updateArticlePriceCurrentWorkingStock();//初始化有规格餐品库存
        articleMapper.updatePackageCurrentWorkingStock();//初始化套餐库存
        articleMapper.updateBigCurrentWorkingStock();//初始化有规格餐品主品库存(等于其子品库存之和)
    }


    @Override
    public List<Article> getArticleList(Integer virtualId,String shopId) {

        return articleMapper.getArticleList(virtualId,shopId);
    }

    @Override
    public int updateArticle(String articleId,String shopId) {

        return articleMapper.updateArticle(articleId,shopId);
    }

    @Override
    public void insertMenuArticle(Long menuId, String shopDetailId, Integer operation) {//消息通知newpos后台发生变化
        if (operation.equals(Common.YES)) {
            //从菜品库添加菜品
            addArticleLibraryToShop(menuId, shopDetailId);
        } else {
            //从菜品表删除菜品来源为菜品库的菜品
            deleteArticleLibraryFromShop(menuId, shopDetailId);
        }
        //向NewPos发送推送消息
        pushMsgToShop(menuId, shopDetailId, operation);
    }

    @Override
    public List<ArticleSellDto> callOrderArtcileNew(Map<String, Object> selectMap) {
        return articleMapperReport.queryOrderArtcileNew(selectMap);
    }

    @Override
    public List<ArticleSellDto> selectArticleByTypeNew(Map<String, Object> selectMap) {
        return articleMapperReport.selectArticleByTypeNew(selectMap);
    }

    @Override
    public Map<String, Object> callArticleOrderCountNew(Map<String, Object> selectMap) {
        return articleMapperReport.selectArticleOrderCountNew(selectMap);
    }

    /**
     * 通过菜单Id想对应店铺添加菜单里的菜品
     * @param menuId
     * @param shopDetailId
     */
    private void addArticleLibraryToShop(Long menuId, String shopDetailId) {
        try {
            //查询出菜单所对应的菜品信息
            List<MenuArticle> menuArticleList = menuArticleService.selectListMenuId(menuId.toString());
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("id");
            filter.getExcludes().add("units");
            filter.getExcludes().add("mealAttrs");
            String menuJson = JSON.toJSONString(menuArticleList, filter);
            List<Article> articleList = JSON.parseObject(menuJson, new TypeReference<List<Article>>(){});
            //补全部分article独有字段
            for (Article article : articleList) {
                article.setId(ApplicationUtils.randomUUID());
                //得到菜单上为门店设置的价格
                BigDecimal shopPrice = menuArticleList.stream().filter(menuArticle -> menuArticle.getArticleId().equalsIgnoreCase(article.getArticleId()))
                        .findAny().get().getShopPrice();
                if (shopPrice != null && shopPrice.compareTo(BigDecimal.ZERO) > 0) {
                    article.setPrice(shopPrice);
                }
                if (article.getPrice() == null) {
                    article.setPrice(BigDecimal.ZERO);
                }
                article.setShopDetailId(shopDetailId);
                //标识当前菜品来源为菜品库
                article.setOpenArticleLibrary(true);
                //插入菜品信息
                articleMapper.insertSelective(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过菜单Id插入菜品信息出错，错误信息：" + e.getMessage());
        }
    }

    /**
     * 删除店铺下面菜品来源为菜品库的菜品
     * @param menuId
     * @param shopDetailId
     */
    private void deleteArticleLibraryFromShop(Long menuId, String shopDetailId) {
        try {
            articleMapper.deleteByMenuIdAndShopId(menuId, shopDetailId);
            //删除菜品是删除推荐热销菜品里的菜品信息
            recommendCategoryArticleService.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据菜单Id、店铺Id删除菜品信息出错");
        }
    }

    /**
     * 向NewPos发送通知
     * @param menuId
     * @param shopDetailId
     * @param operation
     */
    private void pushMsgToShop(Long menuId, String shopDetailId, Integer operation){
        //待发送集合
        List<ShopMsgChangeDto> shopMsgChangeDtoList = new ArrayList<>();
        if (operation.equals(Common.YES)) {
            setShopMsgChangeDto(shopDetailId, operation, shopMsgChangeDtoList);
        } else {
            if (StringUtils.isEmpty(shopDetailId)) {
                //查询到当前菜单下所有启用的门店
                List<MenuShop> menuShopList = menuShopService.selectByMenuId(menuId.toString());
                for (MenuShop menuShop : menuShopList) {
                    setShopMsgChangeDto(menuShop.getShopDetailId(), operation, shopMsgChangeDtoList);
                }
            } else {
                setShopMsgChangeDto(shopDetailId, operation, shopMsgChangeDtoList);
            }
        }
        //向NewPos发送更新通知
        shopMsgChangeDtoList.forEach(shopMsgChangeDto -> posService.shopMsgChange(shopMsgChangeDto));
    }

    /**
     * 通过店铺Id封装店铺品牌信息
     * @param shopDetailId
     */
    private void setShopMsgChangeDto(String shopDetailId, Integer operation, List<ShopMsgChangeDto> shopMsgChangeDtoList) {
        ShopDetail shopDetail  = shopDetailService.selectByPrimaryKey(shopDetailId);
        Brand brand = brandService.selectByPrimaryKey(shopDetail.getBrandId());
        ShopMsgChangeDto tbArticleNew = new ShopMsgChangeDto();
        tbArticleNew.setTableName(NewPosTransmissionUtils.TBARTICLE);
        tbArticleNew.setBrandId(brand.getId());
        tbArticleNew.setShopId(shopDetail.getId());
        if (operation.equals(Common.YES)) {
            tbArticleNew.setType("add");
        } else {
            tbArticleNew.setType("delete");
        }
        shopMsgChangeDtoList.add(tbArticleNew);
    }


    @Override
    public Article selectArticleByArticleId(String articleId, String brandId, String shopId) {
        Article article = articleMapper.selectArticleByArticleId(articleId);
        //查询当前菜品所对应厨房组
        List<Integer> kitchenList = kitchenService.selectIdsByArticleId(article.getId());
        if(kitchenList != null){
            article.setKitchenList(kitchenList.toArray(new Integer[0]));
        }
        //查询套餐下面的子品
        if (article.getArticleType().equals(ArticleType.TOTAL_ARTICLE)) {
            List<MealAttr> mealAttrs = mealAttrService.selectFullByArticleId(articleId, "",null);
            article.setMealAttrs(mealAttrs);
        }
        return getArticle(brandId, shopId, article);
    }

    private Article getArticle(String brandId, String shopId, Article article) {
        ShopDetail shopDetail = shopDetailService.selectShopByStatus(brandId, shopId);
        if (shopDetail.getEnableDuoDongXian()!=1){
            List<KitchenGroup> kitchenGroups = articleMapper.selectByArticleAndShopId(article.getId(), shopId);
            article.setKitchenGroups(kitchenGroups);
        }
        return article;
    }
}
