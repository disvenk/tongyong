package com.resto.pos.web.controller;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.constant.Common;
import com.resto.brand.web.service.RedisService;
import com.resto.shop.web.model.Article;
import com.resto.shop.web.model.ArticleFamily;
import com.resto.shop.web.model.ArticleStock;
import com.resto.shop.web.model.Unit;
import com.resto.shop.web.service.ArticleFamilyService;
import com.resto.shop.web.service.ArticleService;
import com.resto.shop.web.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleController extends GenericController {

    @Resource
    ArticleService articleService;

    @Resource
    ArticleFamilyService articleFamilyService;

    @Resource
    private UnitService unitService;

    @Autowired
    RedisService redisService;

    @RequestMapping("getArticleList")
    public Result getArticleList(Integer isEmpty) {
        return getSuccessResult(articleService.selectListByIsEmpty(isEmpty, getCurrentShopId()));
    }

    @RequestMapping("setArticleEmpty")
    public Result setArticleEmpty(Integer isEmpty, String articleId) {
        articleService.changeEmpty(isEmpty, articleId);
        if(redisService.get(getCurrentShopId()+"articles") != null){
            if(isEmpty == 1){
                redisService.set(getCurrentShopId()+"articles", 0);
            }else{
                redisService.remove(getCurrentShopId()+"articles");
            }
        }
        return getSuccessResult();
    }

    @RequestMapping("/getStock")
    public Result getStock(String familyId,Integer empty,@RequestParam(value="activated",required=false)Integer activated ) {
        List<ArticleStock> list = articleService.getStock(getCurrentShopId(), familyId,empty,activated);
        if (list!=null &&list.size()>0) {
            for (ArticleStock articleStock : list) {
                Integer count = (Integer) redisService.get(articleStock.getId() + Common.KUCUN);
                if (count != null) {
                    articleStock.setCurrentStock(count);
                }
            }
        }
        return getSuccessResult(list);
    }

    @RequestMapping("/clearStock")
    public Result clearStock(String articleId) {
        articleService.clearStock(articleId,getCurrentShopId());
        if(redisService.get(getCurrentShopId()+"articles") != null){
            redisService.remove(getCurrentShopId()+"articles");
        }
        return getSuccessResult();
    }


    @RequestMapping("/editStock")
    public Result clearStock(String articleId, Integer count) {
        articleService.editStock(articleId, count,getCurrentShopId());
        if(redisService.get(getCurrentShopId()+"articles") != null){
            redisService.remove(getCurrentShopId()+"articles");
        }
        return getSuccessResult();
    }

    @RequestMapping("/getFamily")
    public Result getFamily() {
        List<ArticleFamily> families = articleFamilyService.selectList(getCurrentShopId());
        for(ArticleFamily articleFamily:families){
            articleFamily.setInventoryWarningNum(0);
        }
        return getSuccessResult(families);
    }

    @RequestMapping("/getFamilySort")
    public Result getFamilySort(Integer currentPage, Integer showCount) {
        List<ArticleFamily> families = articleFamilyService.selectListBySort(getCurrentShopId(), currentPage, showCount);
        return getSuccessResult(families);
    }

    @RequestMapping("/setActivated")
    public Result setActivated(String articleId,Integer activated){
    	    boolean flag = articleService.setActivated(articleId, activated);
    	return new Result(flag);
    	
    }



    @RequestMapping("getArticleListByFamily")
    public Result getArticleListByFamily(String articleFamilyId, Integer currentPage, Integer showCount) {
        return getSuccessResult(articleService.getArticleListByFamily(getCurrentShopId(), articleFamilyId, currentPage, showCount));
    }

    @RequestMapping("getArticleListByDiscount")
    public Result getArticleListByDiscount() {
        List<Article> articles = articleService.selectListFull(getCurrentShopId(),1,"wechat");
        for(int i = 0; i < articles.size(); i++){
            List<Unit> units = unitService.getUnitByArticleid(articles.get(i).getId());
            if(units != null){
                articles.get(i).setUnits(units);
            }
        }
        return getSuccessResult(articles);
    }
}
