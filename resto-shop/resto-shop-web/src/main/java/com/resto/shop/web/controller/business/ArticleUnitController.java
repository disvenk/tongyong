 package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.shop.web.service.PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.ArticleUnit;
import com.resto.shop.web.service.ArticleUnitService;

@Controller
@RequestMapping("articleunit")
public class ArticleUnitController extends GenericController{

	@Resource
	ArticleUnitService articleunitService;

	@Autowired
	PosService posService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ArticleUnit> listData(){
		return articleunitService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		ArticleUnit articleunit = articleunitService.selectById(id);
		return getSuccessResult(articleunit);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid ArticleUnit brand){
		articleunitService.insert(brand);
		//posService.shopMsgChange(getCurrentShopId());
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid ArticleUnit brand){
		articleunitService.update(brand);
		//posService.shopMsgChange(getCurrentShopId());
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		articleunitService.delete(id);
		//posService.shopMsgChange(getCurrentShopId());
		return Result.getSuccess();
	}
}
