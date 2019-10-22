package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by KONATA on 2016/10/23.
 */
@Controller
@RequestMapping("articleInventory")
public class ArticleInventoryController {


    @Autowired
    private ArticleService articleService;

    @RequestMapping("/list")
    public void list(){
    }



    

    @RequestMapping("save")
    @ResponseBody
    public Result create(String [] shopList,String [] articleList) {

        articleService.assignArticle(shopList, articleList);
        articleService.assignTotal(shopList, articleList);
        return new Result(true);

    }
}
