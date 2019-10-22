 package com.resto.shop.web.controller.scm;

 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.BrandSetting;
 import com.resto.brand.web.service.BrandSettingService;
 import com.resto.shop.web.baseScm.domain.PageResult;
 import com.resto.shop.web.constant.Common;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.model.MdUnit;
 import com.resto.shop.web.service.ScmUnitService;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;
 import javax.annotation.Resource;
 import javax.validation.Valid;
 import java.util.List;

 @Controller
 @RequestMapping("scmUnit")
 public class ScmUnitController extends GenericController {

	 @Resource
	 ScmUnitService scmUnitService;
	 @Resource
	 BrandSettingService brandSettingService;

	 @RequestMapping("/list")
	 public String list(){

		 BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
		 if (brandSetting.getIsOpenScm().equals(Common.YES)){
			 return "scmStockCount/list";
		 }else {
			 return "notopen";
		 }

	 }
	 @RequestMapping("/list_all")
	 @ResponseBody
	 public Result listData(@RequestBody MdUnit mdUnit){
		 PageResult<MdUnit> list = scmUnitService.query4Page(mdUnit);

		return getSuccessResult(list);
	 }
	 @RequestMapping("/list_type")
	 @ResponseBody
	 public Result list_type(Integer type){
		 List<MdUnit> list = scmUnitService.queryByType(type);
		 return getSuccessResult(list);
	 }


	 @RequestMapping("list_one")
	 @ResponseBody
	 public Result list_one(Long id){
		 MdUnit mdUnit = scmUnitService.selectById(id);
		 return getSuccessResult(mdUnit);
	 }

	 @RequestMapping("create")
	 @ResponseBody
	 public Result create(@Valid @RequestBody MdUnit brand){
		 int i = scmUnitService.addScmUnit(brand);
		 if(i>0){
			 return Result.getSuccess();
		 }
		 return new Result("保存失败", 5000,false);
	 }

	 @RequestMapping("modify")
	 @ResponseBody
	 public Result modify(@Valid @RequestBody MdUnit unit){
		 Integer row = scmUnitService.update(unit);
		 return Result.getSuccess();
	 }

	 @RequestMapping("delete")
	 @ResponseBody
	 public Result deleteById(Long id){
		 Integer row = scmUnitService.deleteById(id);
		 return Result.getSuccess();
	 }
 }
