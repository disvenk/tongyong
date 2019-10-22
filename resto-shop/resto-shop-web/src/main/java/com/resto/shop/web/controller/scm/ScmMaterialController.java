 package com.resto.shop.web.controller.scm;

 import com.alibaba.dubbo.config.annotation.Reference;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.BrandSetting;
 import com.resto.brand.web.service.BrandSettingService;
 import com.resto.shop.web.constant.Common;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.dto.MaterialDo;
 import com.resto.shop.web.dto.MdSupplierPriceHeadDo;
 import com.resto.shop.web.model.MdMaterial;
 import com.resto.shop.web.service.MaterialService;
 import com.resto.shop.web.service.SupplierMaterialPriceService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;
 import javax.annotation.Resource;
 import java.util.List;

 @Controller
 @RequestMapping("scmMaterial")
 public class ScmMaterialController extends GenericController {

	 @Resource
	 MaterialService materialService;

	 @Resource
	 SupplierMaterialPriceService supplierpriceService;


	 @Resource
	 BrandSettingService brandSettingService;

	 @RequestMapping("/list")
	 public String list(){

		 BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
		 if (brandSetting.getIsOpenScm().equals(Common.YES)){
			 return "scmMaterial/list";
		 }else {
			 return "notopen";
		 }

	 }

	 @RequestMapping("/list_all")
	 @ResponseBody
	 public Result listData(){
		 List<MaterialDo> list = materialService.queryJoin4Page(getCurrentShopId());
		return getSuccessResult(list);
	 }

	 @RequestMapping("list_one")
	 @ResponseBody
	 public Result list_one(Long id){
		 MdMaterial mdMaterial = materialService.queryById(id);
		 return getSuccessResult(mdMaterial);
	 }

	 /***
	  *
	  * @RequestBody 当时form表单提交的时候，不可以使用该注解
	  * @param mdMaterial  application/x-www-form-urlencoded;charset=UTF-8
	  * @return
	  */

	 @RequestMapping(value = "create")
	 @ResponseBody
	 public Result create(MdMaterial mdMaterial){
		 mdMaterial.setShopDetailId(getCurrentShopId());
		 mdMaterial.setCreaterId(mdMaterial.getCreaterId());
		 mdMaterial.setCreaterName(mdMaterial.getCreaterName());
		 int i = materialService.addMaterial(mdMaterial);
		 if(i>0){
			 return Result.getSuccess();
		 }
		 return new Result("保存失败", 5000,false);
	 }

	 @RequestMapping("modify")
	 @ResponseBody
	 public Result modify(MdMaterial mdMaterial){
		 Integer row = materialService.updateMaterial(mdMaterial);
		 return Result.getSuccess();
	 }

	 @RequestMapping("delete")
	 @ResponseBody
	 public Result delete(Long id){
		 MdSupplierPriceHeadDo mdSupplierPriceHeadDo = supplierPriceIsContainThisId(id);
		 if(mdSupplierPriceHeadDo !=null){
			  return new Result(mdSupplierPriceHeadDo.getPriceName()+"报价单，包含该原料,暂时不可以删除，如要删除请联系管理员！",  5000, false);
		  }
		   Integer row = materialService.deleteById(id);

		 return Result.getSuccess();
	 }

	 private MdSupplierPriceHeadDo supplierPriceIsContainThisId(Long id) {
	 	//查询该shop所有供应商报价单
		 List<MdSupplierPriceHeadDo> effectiveList = supplierpriceService.findEffectiveList(getCurrentShopId(), null);
		 for (MdSupplierPriceHeadDo effective:effectiveList) {
                    if(id.equals(effective.getId())){
                    	return effective;
					}
		 }

		 return null;
	 }
 }
