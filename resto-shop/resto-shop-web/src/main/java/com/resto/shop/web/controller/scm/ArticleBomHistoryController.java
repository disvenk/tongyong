 package com.resto.shop.web.controller.scm;

 import com.alibaba.dubbo.config.annotation.Reference;
 import com.resto.brand.core.entity.Result;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.dto.MdRulArticleBomHeadDo;
 import com.resto.shop.web.service.ArticleBomHeadHistoryService;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.validation.Valid;

 @Controller
 @RequestMapping("scmBomHistory")
 public class ArticleBomHistoryController extends GenericController{

     @Resource
     ArticleBomHeadHistoryService bomHeadHistoryService;

     @RequestMapping("/list")
     public void list(String articleId){
         getRequest().getSession().setAttribute("articleId",articleId);
     }

     @RequestMapping("/list_all")
     @ResponseBody
     public Result listData(){
         String  articleId =(String) getRequest().getSession().getAttribute("articleId");
         return  getSuccessResult(bomHeadHistoryService.queryJoin4Page(getCurrentShopId(),articleId));
     }

     @RequestMapping("modify")
     @ResponseBody
     public Result modify(@Valid @RequestBody MdRulArticleBomHeadDo articlebom){
         articlebom.setShopDetailId(this.getCurrentShopId());
         articlebom.setCreaterId(this.getCurrentUserId());
         articlebom.setCreaterName(getName());
           bomHeadHistoryService.updateRulArticleBomHead(articlebom);
         return Result.getSuccess();
     }

 }
