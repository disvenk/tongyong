package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.ShowPhoto;
import com.resto.brand.web.service.ShowPhotoService;

@Controller
@RequestMapping("showphoto")
public class ShowPhotoController extends GenericController{

	@Resource
	ShowPhotoService showphotoService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ShowPhoto> listData(){
		return showphotoService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		ShowPhoto showphoto = showphotoService.selectById(id);
		return getSuccessResult(showphoto);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid ShowPhoto brand){
		showphotoService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid ShowPhoto brand){
		showphotoService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		showphotoService.delete(id);
		return Result.getSuccess();
	}
}
