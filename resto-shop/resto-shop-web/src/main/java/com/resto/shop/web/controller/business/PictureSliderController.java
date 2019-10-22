package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.PictureSlider;
import com.resto.shop.web.service.PictureSliderService;

@Controller
@RequestMapping("pictureslider")
public class PictureSliderController extends GenericController{

	@Resource
	PictureSliderService picturesliderService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<PictureSlider> listData(){
		return picturesliderService.selectListByShopId(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		PictureSlider pictureslider = picturesliderService.selectById(id);
		return getSuccessResult(pictureslider);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid PictureSlider pictureSlider){
		pictureSlider.setShopDetailId(getCurrentShopId());
		pictureSlider.setState((byte)1);
		picturesliderService.insert(pictureSlider);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid PictureSlider brand){
		picturesliderService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		picturesliderService.delete(id);
		return Result.getSuccess();
	}
}
