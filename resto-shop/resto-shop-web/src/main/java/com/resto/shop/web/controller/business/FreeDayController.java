package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.FreeDay;
import com.resto.shop.web.service.FreedayService;

@Controller
@RequestMapping("freeday")
public class FreeDayController extends GenericController {

	@Resource
	FreedayService freedayService;

	@RequestMapping("/list")
	public void list() {
	}

	@RequestMapping("freeDayList")
	@ResponseBody
	public Result freeDayList(FreeDay day) throws Exception {
		day.setShopDetailId(getCurrentShopId());
		List<FreeDay> list = freedayService.list(day);
		return getSuccessResult(list);
	}

	@RequestMapping("addFreeDay")
	@ResponseBody
	public Result addFreeDay(FreeDay day) throws Exception {
		day.setShopDetailId(getCurrentShopId());
		freedayService.insert(day);
		return Result.getSuccess();
	}

	@RequestMapping("removeFreeDay")
	@ResponseBody
	public Result removeFreeDay(FreeDay day) throws Exception {
		day.setShopDetailId(getCurrentShopId());
		freedayService.delete(day);
		return Result.getSuccess();
	}

	// 设置这个月的周末为休息日
	@RequestMapping("setMonthWeekend")
	@ResponseBody
	public String setMonthWeekend(FreeDay day) throws Exception {
		day.setShopDetailId(getCurrentShopId());
		freedayService.setMonthWeekend(day);
		return "success";
	}

	// 设置这年的周末为休息日
	@RequestMapping("setYearWeekend")
	@ResponseBody
	public String setYearWeekend(FreeDay day) throws Exception {
		day.setShopDetailId(getCurrentShopId());
		freedayService.setYearWeekend(day);
		return "success";
	}

	/*
	 * @RequestMapping("/list_all")
	 * 
	 * @ResponseBody public List<Account> listData(){ return
	 * freedayService.selectList(); }
	 * 
	 * @RequestMapping("list_one")
	 * 
	 * @ResponseBody public Result list_one(String id){ Account account =
	 * freedayService.selectById(id); return getSuccessResult(account); }
	 * 
	 * @RequestMapping("create")
	 * 
	 * @ResponseBody public Result create(@Valid Account brand){
	 * freedayService.insert(brand); return Result.getSuccess(); }
	 * 
	 * @RequestMapping("modify")
	 * 
	 * @ResponseBody public Result modify(@Valid Account brand){
	 * freedayService.update(brand); return Result.getSuccess(); }
	 * 
	 * @RequestMapping("delete")
	 * 
	 * @ResponseBody public Result delete(String id){ freedayService.delete(id);
	 * return Result.getSuccess(); }
	 */
}
