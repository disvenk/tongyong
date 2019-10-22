 package com.resto.shop.web.controller.scm;

 import com.alibaba.dubbo.config.annotation.Reference;
 import com.alibaba.fastjson.JSONObject;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.BrandSetting;
 import com.resto.brand.web.service.BrandSettingService;
 import com.resto.shop.web.constant.Common;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.dto.MdSupplierPriceHeadDo;
 import com.resto.shop.web.service.SupplierMaterialPriceService;
 import com.resto.shop.web.util.ListUtil;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;
 import javax.annotation.Resource;
 import javax.validation.Valid;
 import java.util.List;

@Controller
@RequestMapping("scmSupplerPrice")
public class SupplierPriceController extends GenericController{

	@Resource
	SupplierMaterialPriceService supplierpriceService;
	@Resource
	BrandSettingService brandSettingService;


	@RequestMapping("/list")
	public String list(){
		BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
		if (brandSetting.getIsOpenScm().equals(Common.YES)){
			return "scmSupplerPrice/list";
		}else {
			return "notopen";
		}
	}

	/***
	 *@return
	 */
	@RequestMapping("/list_all")
	@ResponseBody
	public Result listData(){
		List<MdSupplierPriceHeadDo> mdSupplierPriceHeadDos = supplierpriceService.queryJoin4Page(getCurrentShopId());
		return getSuccessResult(mdSupplierPriceHeadDos);
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		//MdSupplierPriceHeadDo supplierprice = supplierpriceService.updateMdSupplierPriceStatus()
		return getSuccessResult(null);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid @RequestBody MdSupplierPriceHeadDo supplierprice){
		supplierprice.setShopDetailId(getCurrentShopId());
		supplierpriceService.addMdSupplierPrice(supplierprice);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid @RequestBody MdSupplierPriceHeadDo supplierprice){
		supplierpriceService.updateMdSupplierPrice(supplierprice);
		return Result.getSuccess();
	}


	@RequestMapping("findEffectiveSupPriceIds")
	@ResponseBody
	public Result findEffectiveSupPriceIds(Long supplierId ){
		List<String> effectiveSupPriceIds = supplierpriceService.findEffectiveSupPriceIds(getCurrentShopId(), supplierId);
		if(ListUtil.isNotEmpty(effectiveSupPriceIds)){
			return new Result("该供应商存在有效报价单号："+JSONObject.toJSON(effectiveSupPriceIds),  0, true);
		}
		return Result.getSuccess();

	}

	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		supplierpriceService.deleteById(id);
		return Result.getSuccess();
	}

	@RequestMapping("approve")
	@ResponseBody
	public Result approve(Long id,String supStatus){
		supplierpriceService.updateMdSupplierPriceStatus(id,supStatus);
		return Result.getSuccess();
	}


}
