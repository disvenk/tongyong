 package com.resto.shop.web.controller.scm;


 import com.alibaba.dubbo.config.annotation.Reference;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.BrandSetting;
 import com.resto.brand.web.service.BrandSettingService;
 import com.resto.shop.web.constant.Common;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.dto.MdRulArticleBomHeadDo;
 import com.resto.shop.web.model.MdRulArticleBomHead;
 import com.resto.shop.web.service.ArticleBomHeadService;
 import com.resto.shop.web.util.ListUtil;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;
 import javax.annotation.Resource;
 import javax.validation.Valid;
 import java.util.List;

 @Controller
@RequestMapping("scmBom")
public class ArticleBomController extends GenericController{

	@Resource
	ArticleBomHeadService articlebomService;

	@Resource
	BrandSettingService brandSettingService;


	@RequestMapping("/list")
    public String list(){
		BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
		if (brandSetting.getIsOpenScm().equals(Common.YES)){
			return "scmBom/list";
		}else {
			return "notopen";
		}
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public Result listData(){
		return  getSuccessResult(articlebomService.queryJoin4Page(getCurrentShopId()));
	}

	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		MdRulArticleBomHead articlebom = articlebomService.queryById(id);
		return getSuccessResult(articlebom);
	}
	@RequestMapping("effectiveBomHead")
	@ResponseBody
	public Result effectiveBomHead(String articleId,Integer state){
		List<MdRulArticleBomHead> articleboms = articlebomService.findBomHeadByState(getCurrentShopId(),articleId,state);

		if(ListUtil.isNotEmpty(articleboms)){
			return new Result("该菜品存在BOM清单："+articleboms.get(0).getBomCode(), 0, true);
		}
		Result result =getSuccessResult(articleboms);
		if(StringUtils.isEmpty(result.getMessage())){
			result.setMessage("");
		}
		return result;
	}


	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid @RequestBody MdRulArticleBomHeadDo articlebom){
		try {
			articlebom.setShopDetailId(this.getCurrentShopId());
			articlebom.setCreaterId(this.getCurrentUserId());
			articlebom.setCreaterName(getName());
			articlebomService.addArticleBomHead(articlebom);
			return Result.getSuccess();
		}catch (Exception e){
			return new Result("保存失败", 5000,false);
		}

	}


	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid @RequestBody MdRulArticleBomHeadDo articlebom){
		articlebom.setShopDetailId(this.getCurrentShopId());
		articlebom.setCreaterId(this.getCurrentUserId());
		articlebom.setCreaterName(getName());
  		articlebomService.updateRulArticleBomHead(articlebom);
		return Result.getSuccess();
	}


	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		articlebomService.deleteById(id);
		return Result.getSuccess();
	}
}
