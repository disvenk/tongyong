package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.WaitPicture;
import com.resto.shop.web.service.WaitPictureService;

@Controller
@RequestMapping("waitpicture")
public class WaitPictureController extends GenericController{

	@Resource
	WaitPictureService waitpictureService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<WaitPicture> listData(){
		return waitpictureService.getWaitPictureList(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		WaitPicture waitpicture = waitpictureService.selectById(id);
		return getSuccessResult(waitpicture);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid WaitPicture waitpicture){
		waitpicture.setBrandId(getCurrentBrandId());
		waitpicture.setShopDetailId(getCurrentShopId());
		waitpictureService.insert(waitpicture);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid WaitPicture waitpicture){
		waitpictureService.update(waitpicture);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		WaitPicture waitPicture = waitpictureService.selectById(id);
		waitpictureService.updateStateById(waitPicture);
		return Result.getSuccess();
	}
}
