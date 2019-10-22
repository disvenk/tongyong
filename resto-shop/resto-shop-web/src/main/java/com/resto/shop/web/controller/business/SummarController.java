package com.resto.shop.web.controller.business;

import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.dto.Summarry;
import com.resto.shop.web.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("summar")
/**
* yz 2017/07/27
* 专用来查询 用户消费笔数 折扣比率等相关信息
* 目前 为 鲁肉范--田林路店
*	获取数据时用 advanced Rest Client工具测试
*	注意需要：1.先登入 2.传beginDate 和 endDate
*	(不推荐 在shiro把这个权限放开直接传shopId)
*
*/
public class SummarController extends GenericController{

	@Resource
	private OrderService orderService;

	@RequestMapping("getShopData")
	@ResponseBody
	public Summarry getShopDataByTime(String beginDate,String endDate){
		Summarry s = orderService.selctSummaryShopData(beginDate,endDate,getCurrentShopId());
		return s;
	}

	@RequestMapping("getBrandData")
	@ResponseBody
	public Summarry getBrandDataByTime(String beginDate,String endDate){
		Summarry s = orderService.selctSummaryBrandData(beginDate,endDate,getCurrentBrandId());
		return s;
	}
}
