 package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.DayAppraiseMessage;
import com.resto.shop.web.service.DayAppraiseMessageService;

@Controller
@RequestMapping("dayappraisemessage")
public class DayAppraiseMessageController extends GenericController{

	@Resource
	DayAppraiseMessageService dayappraisemessageService;

	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<DayAppraiseMessage> listData(){
		return dayappraisemessageService.selectList();
	}

	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		DayAppraiseMessage dayappraisemessage = dayappraisemessageService.selectById(id);
		return getSuccessResult(dayappraisemessage);
	}

	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid DayAppraiseMessage dayappraisemessage){
		dayappraisemessageService.insert(dayappraisemessage);
		return Result.getSuccess();
	}

	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid DayAppraiseMessage dayappraisemessage){
		dayappraisemessageService.update(dayappraisemessage);
		return Result.getSuccess();
	}

	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		dayappraisemessageService.delete(id);
		return Result.getSuccess();
	}
}
