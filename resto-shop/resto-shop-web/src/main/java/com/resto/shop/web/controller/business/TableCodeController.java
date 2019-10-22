package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.TableCode;
import com.resto.shop.web.service.TableCodeService;

@Controller
@RequestMapping("tablecode")
public class TableCodeController extends GenericController{

	@Resource
	TableCodeService tablecodeService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<TableCode> listData(){
		return tablecodeService.selectListByShopId(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		TableCode tablecode = tablecodeService.selectById(id);
		return getSuccessResult(tablecode);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid TableCode tablecode){
		tablecodeService.insertTableCode(tablecode,getCurrentBrandId(),getCurrentShopId());
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid TableCode tablecode){
		tablecodeService.updateTableCode(tablecode);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		tablecodeService.delete(id);
		return Result.getSuccess();
	}

	//判断名字的唯一
    @RequestMapping("checkName")
    @ResponseBody
    public  Result checkedName(String name){
        Result result = new Result();
        result.setSuccess(true);
        TableCode tableCode = tablecodeService.selectByName(name,getCurrentShopId());
        if(tableCode!=null){
            result.setSuccess(false);
            result.setMessage("名字重复了请重新填写");
        }
        return  result;
    }

    //判断桌位号的唯一
    @RequestMapping("checkCodeNumber")
    @ResponseBody
    public  Result checkCodeNumber(String codeNumber){
        Result result = new Result();
        result.setSuccess(true);
        TableCode tableCode = tablecodeService.selectByCodeNumber(codeNumber,getCurrentShopId());
        if(tableCode!=null){
            result.setSuccess(false);
            result.setMessage("桌位号重复了请重新填写");
        }
        return  result;
    }


}
