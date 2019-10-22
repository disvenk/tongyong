 package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.web.form.BrandtemplateEditForm;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaEinvoiceMerchantCreatereqRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandTemplateEdit;
import com.resto.brand.web.service.BrandTemplateEditService;

@Controller
@RequestMapping("brandtemplateedit")
public class BrandTemplateEditController extends GenericController{

	@Resource
	BrandTemplateEditService brandtemplateeditService;

	@RequestMapping("distributionOrDelete")
	@ResponseBody
	public Result distributionOrDelete(@RequestBody BrandtemplateEditForm form){
	    System.out.println(form.id);
	    System.out.println(form.appId);
	    System.out.println(form.definedSelf);
		brandtemplateeditService.distributionOrDelete(form.id,form.appId,form.definedSelf);
		return Result.getSuccess();
	}

}
