 package com.resto.brand.web.controller.business;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.web.dto.BrandTemplateRootDto;
import com.resto.brand.web.model.BrandTemplateEdit;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandTemplateRoot;
import com.resto.brand.web.service.BrandTemplateRootService;

@Controller
@RequestMapping("brandtemplateroot")
public class BrandTemplateRootController extends GenericController{

	@Resource
	BrandTemplateRootService brandtemplaterootService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<BrandTemplateRootDto> listData() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		List<BrandTemplateRoot> brandTemplateRoots = brandtemplaterootService.selectList();
		int sign=0;
		List<BrandTemplateRootDto> brandTemplateRootDtos = new ArrayList<>();
		for(BrandTemplateRoot brandTemplateRoot : brandTemplateRoots){
			BrandTemplateRootDto brandTemplateRootDto = new BrandTemplateRootDto();
			BeanUtils.copyProperties(brandTemplateRoot,brandTemplateRootDto);
			StringBuilder content = new StringBuilder();
			content.append(brandTemplateRoot.getStartSign()+","+brandTemplateRoot.getContent()+","+brandTemplateRoot.getEndSign());
			brandTemplateRootDto.setContentStr(content.toString());
			brandTemplateRootDtos.add(brandTemplateRootDto);
		}
		return brandTemplateRootDtos;
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		BrandTemplateRoot brandtemplateroot = brandtemplaterootService.selectById(id);
		return getSuccessResult(brandtemplateroot);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid BrandTemplateRoot brand){
		brandtemplaterootService.insert(brand);
		brandtemplaterootService.addToDoUpateRoot(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid BrandTemplateRoot brandTemplateRoot){
		brandtemplaterootService.update(brandTemplateRoot);
		brandtemplaterootService.updateToDoUpdataRoot(brandTemplateRoot);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		brandtemplaterootService.deleteToDoUpdataRoot(id);
		brandtemplaterootService.delete(id);

		return Result.getSuccess();
	}
}
