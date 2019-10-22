package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.GoodTop;
import com.resto.shop.web.service.GoodTopService;

@Controller
@RequestMapping("goodtop")
public class GoodTopController extends GenericController{

	@Resource
	GoodTopService goodtopService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<GoodTop> listData(){
		return goodtopService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		GoodTop goodtop = goodtopService.selectById(id);
		return getSuccessResult(goodtop);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid GoodTop goodtop){
		goodtopService.insert(goodtop);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid GoodTop goodtop){
		goodtopService.update(goodtop);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		goodtopService.delete(id);
		return Result.getSuccess();
	}
}
