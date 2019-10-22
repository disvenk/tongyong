package com.resto.shop.web.controller.business;


import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.PinyinUtil;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.PlatformService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.ArticleType;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.*;
import com.resto.shop.web.dto.KitchenDto;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.LogTemplateUtils;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("article")
public class ArticleController extends GenericController {

    @Resource
    FreedayService freedayService;

    @Resource
    ArticleService articleService;

    @Resource
    ArticlePriceService articlePriceService;

    @Resource
    BrandSettingService brandSettingService;

    @Resource
    BrandService brandService;

    @Resource
    ShopDetailService shopDetailService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private ArticleRecommendService articleRecommendService;

    @Autowired
    private MealItemService mealItemService;

    @Autowired
    private PosService posService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private KitchenGroupService kitchenGroupService;
    @Resource
    RecommendCategoryArticleService recommendCategoryArticleService;

    @Resource
    RecommendCategoryService recommendCategoryService;
    @Resource
    ToCollectPostDataService toCollectPostDataService;

    @Resource
    OrderService orderService;

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<Article> listData() {
        //遍历所有未上传数据，24号
//        List<Order> orderList = orderService.selectLaifuShi();
//        for (Order order : orderList) {
//            System.out.println(order.getId());
//            toCollectPostDataService.payCollectPostDataByOrderId(order.getId(), order.getPayMode(), "b4f6b93e1f744319834534f9bc13418d");
////            break;
//        }
        List<Article> articles = articleService.selectList(getCurrentShopId());
        return articles;
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(String id) {
        Article article = articleService.selectById(id);
        return getSuccessResult(article);
    }

    @RequestMapping("list_one_full")
    @ResponseBody
    public Result list_one_full(String id) {
        Article article = articleService.selectFullById(id, "",getCurrentBrandId(),getCurrentShopId());
        article.setUnits(unitService.getUnitByArticleid(id));
//        List<Platform> platforms =  platformService.selectByBrandId(getCurrentBrandId());
//        article.setPlatforms(platforms);
        return getSuccessResult(article);
    }


    @RequestMapping("save")
    @ResponseBody
    public Result create(@Valid @RequestBody Article article) {
        article.setShopDetailId(getCurrentShopId());
        article.setUpdateUserId(getCurrentUserId());
        article.setUpdateTime(new Date());
        String id = article.getId();
        if (StringUtils.isEmpty(article.getId())) {
            article.setCreateUserId(getCurrentUserId());
            article.setInitials(PinyinUtil.getPinYinHeadChar(article.getName()));
            id = articleService.save(article,getCurrentBrandId(),getCurrentShopId()).getId();
            //消息通知newpos后台发生变化
            ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
            shopMsgChangeDto.setBrandId(getCurrentBrandId());
            shopMsgChangeDto.setShopId(getCurrentShopId());
            shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLE);
            shopMsgChangeDto.setType("add");
            shopMsgChangeDto.setId(article.getId());
            try{
                posService.shopMsgChange(shopMsgChangeDto);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(article.getUnits()!=null){
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticle=new ShopMsgChangeDto();
                tbArticle.setBrandId(getCurrentBrandId());
                tbArticle.setShopId(getCurrentShopId());
                tbArticle.setTableName(NewPosTransmissionUtils.TBARTICLE);
                tbArticle.setType("add");
                try{
                    posService.shopMsgChange(tbArticle);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticleUnitNew=new ShopMsgChangeDto();
                tbArticleUnitNew.setBrandId(getCurrentBrandId());
                tbArticleUnitNew.setShopId(getCurrentShopId());
                tbArticleUnitNew.setTableName(NewPosTransmissionUtils.TBARTICLEUNITNEW);
                tbArticleUnitNew.setType("add");
                try{
                    posService.shopMsgChange(tbArticleUnitNew);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticleKitchen=new ShopMsgChangeDto();
                tbArticleKitchen.setBrandId(getCurrentBrandId());
                tbArticleKitchen.setShopId(getCurrentShopId());
                tbArticleKitchen.setTableName(NewPosTransmissionUtils.TBARTICLEKITCHEN);
                tbArticleKitchen.setType("add");
                try{
                    posService.shopMsgChange(tbArticleKitchen);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticleSupportTime=new ShopMsgChangeDto();
                tbArticleSupportTime.setBrandId(getCurrentBrandId());
                tbArticleSupportTime.setShopId(getCurrentShopId());
                tbArticleSupportTime.setTableName(NewPosTransmissionUtils.TBARTICLESUPPORTTIME);
                tbArticleSupportTime.setType("add");
                try{
                    posService.shopMsgChange(tbArticleSupportTime);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbUnit=new ShopMsgChangeDto();
                tbUnit.setBrandId(getCurrentBrandId());
                tbUnit.setShopId(getCurrentShopId());
                tbUnit.setTableName(NewPosTransmissionUtils.TBUNIT);
                tbUnit.setType("add");
                try{
                    posService.shopMsgChange(tbUnit);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbUnitDetail=new ShopMsgChangeDto();
                tbUnitDetail.setBrandId(getCurrentBrandId());
                tbUnitDetail.setShopId(getCurrentShopId());
                tbUnitDetail.setTableName(NewPosTransmissionUtils.TBUNITDETAIL);
                tbUnitDetail.setType("add");
                try{
                    posService.shopMsgChange(tbUnitDetail);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticleUnitDetail=new ShopMsgChangeDto();
                tbArticleUnitDetail.setBrandId(getCurrentBrandId());
                tbArticleUnitDetail.setShopId(getCurrentShopId());
                tbArticleUnitDetail.setTableName(NewPosTransmissionUtils.TBARTICLEUNITDETAIL);
                tbArticleUnitDetail.setType("add");
                try{
                    posService.shopMsgChange(tbArticleUnitDetail);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(article.getArticleType()==2){
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbMealAttr=new ShopMsgChangeDto();
                tbMealAttr.setBrandId(getCurrentBrandId());
                tbMealAttr.setShopId(getCurrentShopId());
                tbMealAttr.setTableName(NewPosTransmissionUtils.TBMEALATTR);
                tbMealAttr.setType("add");
                try{
                    posService.shopMsgChange(tbMealAttr);
                }catch(Exception e){
                    e.printStackTrace();
                }
                ShopMsgChangeDto tbMealItem=new ShopMsgChangeDto();
                tbMealItem.setBrandId(getCurrentBrandId());
                tbMealItem.setShopId(getCurrentShopId());
                tbMealItem.setTableName(NewPosTransmissionUtils.TBMEALITEM);
                tbMealItem.setType("add");
                try{
                    posService.shopMsgChange(tbMealItem);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            unitService.insertArticleRelation(id, article.getUnits(),getCurrentBrandId(),getCurrentShopId());
        } else {
            article.setInitials(PinyinUtil.getPinYinHeadChar(article.getName()));
            articleService.update(article,getCurrentBrandId(),getCurrentShopId());
            //消息通知newpos后台发生变化
            ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
            shopMsgChangeDto.setBrandId(getCurrentBrandId());
            shopMsgChangeDto.setShopId(getCurrentShopId());
            shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLE);
            shopMsgChangeDto.setType("modify");
            shopMsgChangeDto.setId(article.getId());
            try{
                posService.shopMsgChange(shopMsgChangeDto);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(article.getUnits()!=null){
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticle=new ShopMsgChangeDto();
                tbArticle.setBrandId(getCurrentBrandId());
                tbArticle.setShopId(getCurrentShopId());
                tbArticle.setTableName(NewPosTransmissionUtils.TBARTICLE);
                tbArticle.setType("modify");
                try{
                    posService.shopMsgChange(tbArticle);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticleUnitNew=new ShopMsgChangeDto();
                tbArticleUnitNew.setBrandId(getCurrentBrandId());
                tbArticleUnitNew.setShopId(getCurrentShopId());
                tbArticleUnitNew.setTableName(NewPosTransmissionUtils.TBARTICLEUNITNEW);
                tbArticleUnitNew.setType("modify");
                try{
                    posService.shopMsgChange(tbArticleUnitNew);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticleKitchen=new ShopMsgChangeDto();
                tbArticleKitchen.setBrandId(getCurrentBrandId());
                tbArticleKitchen.setShopId(getCurrentShopId());
                tbArticleKitchen.setTableName(NewPosTransmissionUtils.TBARTICLEKITCHEN);
                tbArticleKitchen.setType("modify");
                try{
                    posService.shopMsgChange(tbArticleKitchen);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticleSupportTime=new ShopMsgChangeDto();
                tbArticleSupportTime.setBrandId(getCurrentBrandId());
                tbArticleSupportTime.setShopId(getCurrentShopId());
                tbArticleSupportTime.setTableName(NewPosTransmissionUtils.TBARTICLESUPPORTTIME);
                tbArticleSupportTime.setType("modify");
                try{
                    posService.shopMsgChange(tbArticleSupportTime);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbUnit=new ShopMsgChangeDto();
                tbUnit.setBrandId(getCurrentBrandId());
                tbUnit.setShopId(getCurrentShopId());
                tbUnit.setTableName(NewPosTransmissionUtils.TBUNIT);
                tbUnit.setType("modify");
                try{
                    posService.shopMsgChange(tbUnit);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbUnitDetail=new ShopMsgChangeDto();
                tbUnitDetail.setBrandId(getCurrentBrandId());
                tbUnitDetail.setShopId(getCurrentShopId());
                tbUnitDetail.setTableName(NewPosTransmissionUtils.TBUNITDETAIL);
                tbUnitDetail.setType("modify");
                try{
                    posService.shopMsgChange(tbUnitDetail);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbArticleUnitDetail=new ShopMsgChangeDto();
                tbArticleUnitDetail.setBrandId(getCurrentBrandId());
                tbArticleUnitDetail.setShopId(getCurrentShopId());
                tbArticleUnitDetail.setTableName(NewPosTransmissionUtils.TBARTICLEUNITDETAIL);
                tbArticleUnitDetail.setType("modify");
                try{
                    posService.shopMsgChange(tbArticleUnitDetail);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(article.getArticleType()==2){
                //消息通知newpos后台发生变化
                ShopMsgChangeDto tbMealAttr=new ShopMsgChangeDto();
                tbMealAttr.setBrandId(getCurrentBrandId());
                tbMealAttr.setShopId(getCurrentShopId());
                tbMealAttr.setTableName(NewPosTransmissionUtils.TBMEALATTR);
                tbMealAttr.setType("modify");
                try{
                    posService.shopMsgChange(tbMealAttr);
                }catch(Exception e){
                    e.printStackTrace();
                }
                ShopMsgChangeDto tbMealItem=new ShopMsgChangeDto();
                tbMealItem.setBrandId(getCurrentBrandId());
                tbMealItem.setShopId(getCurrentShopId());
                tbMealItem.setTableName(NewPosTransmissionUtils.TBMEALITEM);
                tbMealItem.setType("modify");
                try{
                    posService.shopMsgChange(tbMealItem);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            //修改单品的时候如果存在推荐餐包 联动修改
            if (article.getArticleType() == ArticleType.SIMPLE_ARTICLE) {
                List<ArticleRecommendPrice> articleRecommendPrice = articleRecommendService.selectByRecommendArticleInfo(article.getId());
                for (ArticleRecommendPrice ar : articleRecommendPrice) {
                    articleRecommendService.updatePriceById(article.getFansPrice() != null ? article.getFansPrice() : article.getPrice(), ar.getId());
                }
                //联动修改套餐子品名称
                List<MealItem> mealItemList = mealItemService.selectByArticleId(article.getId());
                if(mealItemList.size() > 0){
                    for(MealItem mealItem : mealItemList){
                        mealItem.setArticleName(article.getName());
                        mealItemService.updateArticleNameById(article.getName(), mealItem.getId());
                        //消息通知newpos后台发生变化
                        ShopMsgChangeDto mealItemShopMsgChangeDto=new ShopMsgChangeDto();
                        mealItemShopMsgChangeDto.setBrandId(getCurrentBrandId());
                        mealItemShopMsgChangeDto.setShopId(getCurrentShopId());
                        mealItemShopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBMEALITEM);
                        mealItemShopMsgChangeDto.setType("modify");
                        mealItemShopMsgChangeDto.setId(mealItem.getId().toString());
                        try{
                            posService.shopMsgChange(mealItemShopMsgChangeDto);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            List<ArticlePrice> list = articlePriceService.selectByArticleId(article.getId());
            if (article.getIsEmpty() == true) {
                articleService.clearStock(article.getId(), getCurrentShopId());
            } else {
                if (article.getArticleType() == 1 && list.size() == 0) {
                    if (freedayService.selectExists(new Date(), article.getShopDetailId())) {
                        articleService.editStock(article.getId(), article.getStockWeekend(), getCurrentShopId());
                    } else {
                        articleService.editStock(article.getId(), article.getStockWorkingDay(), getCurrentShopId());
                    }
                } else if (article.getArticleType() == 1 && list.size() != 0) {
                    if (freedayService.selectExists(new Date(), article.getShopDetailId())) {
                        for (ArticlePrice ap : list) {
                            articleService.editStock(ap.getId(), ap.getStockWeekend(), getCurrentShopId());
                        }
                    } else {
                        for (ArticlePrice ap : list) {
                            articleService.editStock(ap.getId(), ap.getStockWorkingDay(), getCurrentShopId());
                        }
                    }
                }
            }
            if (article.getActivated() == true) {
                articleService.setActivated(article.getId(), 1);
            } else {
                articleService.setActivated(article.getId(), 0);
            }
            unitService.updateArticleRelation(id, article.getUnits(),getCurrentBrandId(),getCurrentShopId());
        }
        //articleService.initStock();
        Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
        LogTemplateUtils.articleEdit(brand.getBrandName(), shopDetail.getName(), getUserName());
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id) {
        Article article = articleService.selectById(id);
        if (article.getArticleType() == ArticleType.SIMPLE_ARTICLE) {
            //单品时校验
            List<Article> articles = articleService.delCheckArticle(id);
            if (articles.size() != 0) {
                StringBuffer mess = new StringBuffer();
                for (Article art : articles) {
                    mess.append(art.getName() + "，");
                }
                Result result = new Result();
                result.setSuccess(false);
                result.setMessage("删除失败，在" + mess.toString().substring(0, mess.toString().length() - 1) + "套餐存在！");
                result.setStatusCode(100);
                return result;
            }
        }
        RecommendCategoryArticle recommendCategoryArticle=recommendCategoryArticleService.selectByArticleId(id);
        if(recommendCategoryArticle!=null){
            RecommendCategory recommendCategory=recommendCategoryService.selectById(recommendCategoryArticle.getRecommendCategoryId());
            Result result = new Result();
            result.setSuccess(false);
            result.setMessage("删除失败，该菜品在推荐类别“" +recommendCategory.getName()+ "”中存在，请在推荐类别中删除后重试");
            result.setStatusCode(100);
            return result;
        }
        articleService.delete(id);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLE);
        shopMsgChangeDto.setType("delete");
        shopMsgChangeDto.setId(article.getId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        //联动删除在推荐餐品包中的id
        articleRecommendService.deleteRecommendByArticleId(id);
        Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
        LogTemplateUtils.articleEdit(brand.getBrandName(), shopDetail.getName(), getUserName());
        return Result.getSuccess();
    }

    public boolean IsFreeday(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int dayForWeek = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        }
        if (dayForWeek > 5) {
            return false;
        }
        return true;
    }

    @RequestMapping("singo_article")
    @ResponseBody
    public List<Article> getSingoList() {
        List<Article> result = articleService.getSingoArticle(getCurrentShopId());
        return result;
    }

    @RequestMapping("singo_article_all")
    @ResponseBody
    public List<Article> getSingoListAll() {
        List<Article> result = articleService.getSingoArticleAll(getCurrentShopId());
        return result;
    }

    @RequestMapping("/selectsingleItem")
    @ResponseBody
    public Result selectsingleItem() {
        List<Article> list = null;
        try {
            list = articleService.selectsingleItem(getCurrentShopId());
            return getSuccessResult(list);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new Result(false);
        }
    }
    @RequestMapping("/selectkitchenGroup")
    @ResponseBody
    public List<KitchenGroup> selectkitchenGroup() {

        List<KitchenGroup> kitchenGroups = kitchenGroupService.selectKitchenGroupByShopDetailId(getCurrentShopId());
        return kitchenGroups;
    }
    @RequestMapping("/selectTree")
    @ResponseBody
    public String selectTree() {
        List<KitchenDto> kitchenDtos= articleService.selectTree(getCurrentBrandId(),getCurrentShopId());
        JSONObject jsonObject = new JSONObject();
        String json = jsonObject.toJSONString(kitchenDtos);
        return json;
    }
    @RequestMapping("/selectShopStatus")
    @ResponseBody
    public Integer selectShopStatus(){
        Integer status=kitchenService.selectShopStatus(getCurrentBrandId(),getCurrentShopId());
        return status;
    }
}
