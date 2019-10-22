 package com.resto.shop.web.controller.scm;

 import com.alibaba.dubbo.config.annotation.Reference;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.BrandUser;
 import com.resto.brand.web.model.ShopDetail;
 import com.resto.brand.web.service.ShopDetailService;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.dto.DocPmsPoHeaderDetailDo;
 import com.resto.shop.web.model.DocPmsPoHeader;
 import com.resto.shop.web.service.DocPmsPoHeaderService;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;
 import javax.annotation.Resource;
 import javax.validation.Valid;

@Controller
@RequestMapping("scmDocPmsPoHeader")
public class ScmDocPmsPoHeaderController extends GenericController{

	@Resource
	DocPmsPoHeaderService docPmsPoHeaderService;

	@Resource
	private ShopDetailService shopDetailService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public Result listData(String shopId){
    	String shopDetailId =StringUtils.isEmpty(shopId)?getCurrentShopId():shopId;
		String currentShopName = null;
		if(StringUtils.isEmpty(shopDetailId)){
			ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopDetailId);
			currentShopName = shopDetail.getName();

		}else{
			currentShopName =getCurrentShopName();
		}

		return getSuccessResult(docPmsPoHeaderService.queryJoin4Page(shopDetailId,currentShopName));
	}

    @RequestMapping("/docPmsPoDetailDos")
    @ResponseBody
    public Result docPmsPoDetailDos(String scmDocPmsPoHeaderId){
        return getSuccessResult(docPmsPoHeaderService.queryDocPmsPoDetailDos(scmDocPmsPoHeaderId));
    }
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		DocPmsPoHeader docPmsPoHeader = docPmsPoHeaderService.selectById(id);
		return getSuccessResult(docPmsPoHeader);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid @RequestBody DocPmsPoHeaderDetailDo docPmsPoHeaderDetailDo){
		docPmsPoHeaderService.createPmsPoHeaderDetailDo(docPmsPoHeaderDetailDo);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid @RequestBody DocPmsPoHeader docPmsPoHeader){
		docPmsPoHeaderService.update(docPmsPoHeader);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		docPmsPoHeaderService.deleteById(id);
		return Result.getSuccess();
	}

	@RequestMapping("approve")
	@ResponseBody
	public Result approve(Long id,Integer orderStatus){
		docPmsPoHeaderService.updateStateById(id,orderStatus,getUserName());
		return Result.getSuccess();
	}

}
