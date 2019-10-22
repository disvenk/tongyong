 package com.resto.shop.web.controller.scm;

 import com.alibaba.dubbo.config.annotation.Reference;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.BrandSetting;
 import com.resto.brand.web.service.BrandSettingService;
 import com.resto.shop.web.constant.Common;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.model.DocReturnHeader;
 import com.resto.shop.web.service.DocReturnHeaderService;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.validation.Valid;

@Controller
@RequestMapping("scmDocReturnHeader")
public class ScmDocReturnHeaderController extends GenericController{

	@Resource
	DocReturnHeaderService docReturnHeaderService;
    @Resource
    private BrandSettingService brandSettingService;

    @RequestMapping("/list")
    public String list() {
           BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
           if (brandSetting.getIsOpenScm().equals(Common.YES)) {
               return "scmDocReturnHeader/list";
           } else {
                return "notopen";
           }
       }
	
	@RequestMapping("/list_all")
	@ResponseBody
	public Result listData(){
		return getSuccessResult(docReturnHeaderService.queryJoin4Page(getCurrentShopId(),getCurrentShopName()));
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		DocReturnHeader docReturnHeader = docReturnHeaderService.selectById(id);
		return getSuccessResult(docReturnHeader);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid @RequestBody DocReturnHeader docReturnHeader){
		docReturnHeaderService.insert(docReturnHeader);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid @RequestBody DocReturnHeader docReturnHeader){
		docReturnHeaderService.update(docReturnHeader);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		docReturnHeaderService.deleteById(id);
		return Result.getSuccess();
	}
	@RequestMapping("approve")
	@ResponseBody
	public Result approve(Long id,String status){
		docReturnHeaderService.updateDocReturnHeaderStatus(id,status);
		return Result.getSuccess();
	}

}
