package com.resto.brand.web.controller.business;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.RefundRemark;
import com.resto.brand.web.service.RefundRemarkService;

@Controller
@RequestMapping("refundremark")
public class RefundRemarkController extends GenericController{

	@Resource
	RefundRemarkService refundremarkService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<RefundRemark> listData(){
		return refundremarkService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		RefundRemark refundremark = refundremarkService.selectById(id);
		return getSuccessResult(refundremark);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid RefundRemark brand){
		brand.setCreateTime(new Date());
		refundremarkService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid RefundRemark brand){
		refundremarkService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		RefundRemark refundRemark = refundremarkService.selectById(id);
		if(refundRemark.getState() == 1){
			refundRemark.setState(0);
		}else{
			refundRemark.setState(1);
		}
		refundremarkService.update(refundRemark);
		return Result.getSuccess();
	}
}
