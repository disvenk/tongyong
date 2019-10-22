 package com.resto.shop.web.controller.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.service.BrandService;
import com.resto.shop.web.dto.SvipActReportDto;
import com.resto.shop.web.dto.SvipActivityDto;
import com.resto.shop.web.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.Svip;
import com.resto.shop.web.service.SvipService;

@Controller
@RequestMapping("svip")
public class SvipReportController extends GenericController{

	@Autowired
	BrandService brandService;

	@Resource
	SvipService svipService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/activityDetails")
	public String activityDetails(){
		return "svipreport/activityDetails";
	}

	@RequestMapping("/list_all")
	@ResponseBody
	public List<SvipActivityDto> listData(String beginDate,String endDate){
		List<SvipActivityDto> actReport = svipService.getActReport(beginDate,endDate);
		return actReport;
	}

	@RequestMapping("selectListByActId")
	@ResponseBody
	public Object selectListByActId(String beginDate,String endDate,String activityId){
		return  svipService.selectListByActId(beginDate,endDate ,activityId );
	}

	@RequestMapping("exprotActExcel")
	@ResponseBody
	public Result exprotActExcel(@RequestParam("beginDate") String beginDate,
											@RequestParam("endDate") String endDate,
											HttpServletResponse response,
											HttpServletRequest request){
		List<SvipActivityDto> actReport = svipService.getActReport(beginDate,endDate);
		Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());

		Map<String, String> map = new HashMap<>();
		map.put("reportTitle", brand.getBrandName());// sheet的名字
		map.put("reportType", "店庆活动报表");// 表的头，第一行内容
		map.put("brandName", brand.getBrandName());
		if(beginDate.equals(endDate)){
			map.put("date", beginDate);
		}else {
			map.put("endDate", endDate);
			map.put("beginDate", beginDate);
		}
		map.put("num", "16");// 显示的位置
		map.put("timeType", "yyyy-MM-dd");

		String[][] header2 = {
				{"活动名称", "20"},
				{"付费会员数量", "23"},
				{"付费会员金额", "23"}
		};
		String[] column = {"activityName", "num", "money"};


		HSSFWorkbook workbook2 = new HSSFWorkbook();

		ExcelUtil<SvipActivityDto> excelUtil2 = new ExcelUtil<>();

		String str2 = brand.getBrandName()+"店庆活动报表.xls";

		try {
			excelUtil2.ExportExcel(str2,request,response,workbook2, header2, column, actReport, map);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result("下载失败",false);
		}
		return new Result("下载成功",true);
	}

	@RequestMapping("exprotActSvipExcel")
	@ResponseBody
	public Result exprotActSvipExcel(@RequestParam("beginDate") String beginDate,
									@RequestParam("endDate") String endDate,
									 @RequestParam("activityId") String activityId,
									HttpServletResponse response,
									HttpServletRequest request){
		List<SvipActReportDto> actReport = svipService.selectListByActId(beginDate,endDate,activityId);
		Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());
		Map<String, String> map = new HashMap<>();
		map.put("reportTitle", brand.getBrandName());// sheet的名字
		map.put("reportType", "付费会员数据报表");// 表的头，第一行内容
		map.put("brandName", brand.getBrandName());
		if(beginDate.equals(endDate)){
			map.put("date", beginDate);
		}else {
			map.put("endDate", endDate);
			map.put("beginDate", beginDate);
		}
		map.put("num", "16");// 显示的位置
		map.put("timeType", "yyyy-MM-dd HH:mm:ss"); //凡字段中带有时间均按照此格式转换

		String[][] header = {
				{"支付店铺", "23"},
				{"会员昵称", "23"},
				{"手机号码", "23"},
				{"支付金额", "23"},
				{"支付时间", "23"},

		};
		String[] column = {
				"shopName",
				"nickName",
				"tel",
				"money",
				"dataTime"
		};


		HSSFWorkbook workbook = new HSSFWorkbook();

		ExcelUtil<SvipActReportDto> excelUtil2 = new ExcelUtil<>();

		//文件的名字
		String str2 = "付费会员数据报表.xls";

		try {
			excelUtil2.ExportExcel(str2,request,response,workbook, header, column, actReport, map);
			return new Result("下载成功",true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result("下载失败",false);
	}
}
