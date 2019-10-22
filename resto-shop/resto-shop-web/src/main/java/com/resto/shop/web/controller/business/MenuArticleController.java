 package com.resto.shop.web.controller.business;

import java.util.List;

import com.resto.shop.web.model.MenuShop;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.MenuArticle;
import com.resto.shop.web.service.MenuArticleService;

@Controller
@RequestMapping("menuarticle")
public class MenuArticleController extends GenericController{

	@Autowired
	MenuArticleService menuarticleService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<MenuArticle> listData(){
		return menuarticleService.selectList();
	}

	@RequestMapping("/listMenuId")
	@ResponseBody
	public List<MenuArticle> listMenuId(String menuId){
		return menuarticleService.selectListMenuId(menuId);
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		MenuArticle menuarticle = menuarticleService.selectById(id);
		return getSuccessResult(menuarticle);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@RequestBody List<MenuArticle> menuarticles){
		//menuarticleService.deleteByMenuId(menuarticles.get(0).getMenuId().toString());
		menuarticles.forEach(s -> {
			menuarticleService.insertMenuArticle(s);
		});
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@RequestBody MenuArticle menuarticle){
		menuarticleService.updateMenuArticle(menuarticle);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		menuarticleService.delete(id);
		return Result.getSuccess();
	}

    @RequestMapping("deleteArticleId")
    @ResponseBody
    public Result deleteArticleId(String articleId){
        menuarticleService.deleteArticleId(articleId);
        return Result.getSuccess();
    }
}
