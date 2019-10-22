package com.resto.shop.web.controller.business;

import com.resto.shop.web.model.Article;
import com.resto.shop.web.model.ArticleFamily;
import com.resto.shop.web.service.ArticleFamilyService;
import com.resto.shop.web.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("articlelibrary")
@Slf4j
public class ArticleLibraryController extends GenericController{

	@Autowired
    ArticleService articleService;

	@Autowired
    ArticleFamilyService articleFamilyService;
	
	@RequestMapping("/list")
    public void list(){
    }

    /**
     * 查询菜品信息
     * @return
     */
	@RequestMapping("/list_all")
	@ResponseBody
	public Result listData(){
        try {
            //查询出当前店铺下所有菜品、菜品分类
            List<Article> articleAllList = articleService.selectList(getCurrentShopId());
            List<ArticleFamily> articleFamilyAllList = articleFamilyService.selectList("-1");
            //筛选出是由菜品库启用菜单插入的菜品
            List<Article> articleList = articleAllList.stream().filter(article -> article.getOpenArticleLibrary()).collect(Collectors.toList());
            articleList.forEach(article -> {
                String articleFamilyName = articleFamilyAllList.stream().filter(articleFamily -> articleFamily.getId().equalsIgnoreCase(article.getArticleFamilyId()))
                        .map(ArticleFamily::getName).findFirst().orElse("--");
                //当前菜品的菜品类型
                article.setArticleFamilyName(articleFamilyName);
                article.setArticleTypeName(article.getArticleType() == 1 ? "单品" : "套餐");
                article.setActivatedName(article.getActivated() ? "上架" : "下架");
            });
            return getSuccessResult(articleList);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("查询菜品信息出错, 错误信息：" + ex.getMessage());
            return new Result(false);
        }
	}


    /**
     * 根据菜品的articleId查询菜品的基本信息
     * @param articleId
     * @return
     */
    @RequestMapping("/selectArticleByArticleId")
    @ResponseBody
	public Result selectArticleByArticleId(String articleId) {
        try {
            Article article = articleService.selectArticleByArticleId(articleId, getCurrentBrandId(), getCurrentShopId());
            return getSuccessResult(article);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询菜品详情出错，错误信息：" + e.getMessage());
            return new Result(false);
        }
    }
	

}
