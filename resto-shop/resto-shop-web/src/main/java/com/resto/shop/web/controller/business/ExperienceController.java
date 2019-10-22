package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.web.model.ShowPhoto;
import com.resto.brand.web.service.ShowPhotoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.Experience;
import com.resto.shop.web.service.ExperienceService;

@Controller
@RequestMapping("experience")
public class ExperienceController extends GenericController{

	@Resource
	ExperienceService experienceService;

	@Resource
	ShowPhotoService showPhotoServiceBrand;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ShowPhoto> listData(){
		List<Experience> experiences = experienceService.selectListByShopId(getCurrentShopId());
		List<ShowPhoto> showPhotos= showPhotoServiceBrand.selectList();
		for(int i = 0; i < showPhotos.size(); i++){
			showPhotos.get(i).setChoiceIt(false);
			for(int j = 0; j < experiences.size(); j++){
				if(showPhotos.get(i).getTitle().equals(experiences.get(j).getTitle())){
					showPhotos.get(i).setChoiceIt(true);
				}
			}
		}
		return showPhotos;
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		Experience experience = experienceService.selectById(id);
		return getSuccessResult(experience);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(Experience experience){
		experience.setShopDetailId(getCurrentShopId());
		experienceService.insert(experience);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Experience experience){
		experienceService.update(experience);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String title){
		experienceService.deleteByTitle(title, getCurrentShopId());
		return Result.getSuccess();
	}
}
