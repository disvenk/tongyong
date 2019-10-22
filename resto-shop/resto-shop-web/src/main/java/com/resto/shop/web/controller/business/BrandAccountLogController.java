 package com.resto.shop.web.controller.business;

 import com.resto.brand.core.entity.Result;
 import com.resto.brand.core.util.ExcelUtil;
 import com.resto.brand.web.model.BrandAccountLog;
 import com.resto.brand.web.model.ShopDetail;
 import com.resto.brand.web.service.BrandAccountLogService;
 import com.resto.brand.web.service.ShopDetailService;
 import com.resto.shop.web.controller.GenericController;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.swing.*;
 import javax.validation.Valid;
 import java.io.FileOutputStream;
 import java.io.OutputStream;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 @Controller
 @RequestMapping("brandaccountlog")
 public class BrandAccountLogController extends GenericController {

	 @Resource
	 BrandAccountLogService brandaccountlogService;

	 @Resource
	 ShopDetailService shopDetailService;

	 @RequestMapping("/list")
	 public void list(){
	 }

	 @RequestMapping("/list_all")
	 @ResponseBody
	 public Result listData(@RequestParam("beginDate")String beginDate,@RequestParam("endDate")String endDate){
	 	 List<BrandAccountLog> list = brandaccountlogService.selectListByBrandIdAndTime(beginDate,endDate,getCurrentBrandId());

	 	 return getSuccessResult(list);

	 }

	 @RequestMapping("list_one")
	 @ResponseBody
	 public Result list_one(Long id){
		 BrandAccountLog brandaccountlog = brandaccountlogService.selectById(id);
		 return getSuccessResult(brandaccountlog);
	 }

	 @RequestMapping("create")
	 @ResponseBody
	 public Result create(@Valid BrandAccountLog brand){
		 brandaccountlogService.insert(brand);
		 return Result.getSuccess();
	 }

	 @RequestMapping("modify")
	 @ResponseBody
	 public Result modify(@Valid BrandAccountLog brand){
		 brandaccountlogService.update(brand);
		 return Result.getSuccess();
	 }

	 @RequestMapping("delete")
	 @ResponseBody
	 public Result delete(Long id){
		 brandaccountlogService.delete(id);
		 return Result.getSuccess();
	 }

	 //生成品牌 账户流水报表
	 @SuppressWarnings("unchecked")
	 @RequestMapping("create_accountLog_excel")
	 @ResponseBody
	 public Result createAccountLogExcel(String beginDate,String endDate,HttpServletRequest request){
		 List<ShopDetail> shopDetailList = getCurrentShopDetails();
		 if(shopDetailList==null){
			 shopDetailList = shopDetailService.selectByBrandId(getCurrentBrandId());
		 }
		 //导出文件名
		 String fileName = "品牌账户流水表"+beginDate+"至"+endDate+".xls";
		 //定义读取文件的路径
		 String path = request.getSession().getServletContext().getRealPath(fileName);
		 //定义列
		 String[]columns={"createTime","groupName","behaviorName","serialNumber","detailName","foundChange","remain"};
		 //定义数据
		 List<BrandAccountLog> result = getBrandAccountLogByTime(beginDate,endDate,getCurrentBrandId());
		 String shopName="";
		 for (ShopDetail shopDetail : shopDetailList) {
			 shopName += shopDetail.getName()+",";
		 }
		 shopName = shopName.substring(0, shopName.length() - 1);
		 Map<String,String> map = new HashMap<>();
		 map.put("brandName", getBrandName());
		 map.put("shops", shopName);
		 map.put("beginDate", beginDate);
		 map.put("reportType", "品牌账户流水报表");//表的头，第一行内容
		 map.put("endDate", endDate);
		 map.put("num", "6");//显示的位置
		 map.put("reportTitle", "品牌账户流水");//表的名字
		 map.put("timeType", "yyyy-MM-dd HH:mm:ss");

		 String[][] headers = {{"时间","25"},{"主体","25"},{"行为","25"},{"流水号","25"},{"详情","25"},{"资金变动","25"},{"余额","25"}};
		 //定义excel工具类对象
		 ExcelUtil<BrandAccountLog> excelUtil=new ExcelUtil<>();
		 try{
			 OutputStream out = new FileOutputStream(path);
			 excelUtil.ExportExcel(headers, columns, result, out, map);
			 out.close();
		 }catch(Exception e){
			 log.error("生成品牌账户流水报表出错！");
			 e.printStackTrace();
			 return new Result(false);
		 }
		 return getSuccessResult(path);
	 }

	 /**
	  * 获取时间段内的数据
	  * @param beginDate
	  * @param endDate
	  * @return
	  */
	 private List<BrandAccountLog> getBrandAccountLogByTime(String beginDate, String endDate,String brandId) {

	 	List<BrandAccountLog> list = brandaccountlogService.selectListByBrandIdAndTime(beginDate,endDate,brandId);
	 	if(list!=null&&!list.isEmpty()){
	 		for(BrandAccountLog blog:list){
				String bevaiorName="未知";
				switch (blog.getBehavior()){
					case 10:
						bevaiorName = "注册";
						break;
					case 20:
						bevaiorName = "消费";
						break;
					case 30:
						bevaiorName = "短信";
						break;
					case 40:
						bevaiorName = "充值";
						break;
					default:
						break;
				}
				blog.setBehaviorName(bevaiorName);

				String detailName = "未知";
				switch (blog.getDetail()){
					case 10:
						detailName = "新用户注册";
						break;
					case 20:
						detailName = "消费订单抽成";
						break;
					case 21:
						detailName = "消费订单实付抽成";
						break;
					case 22:
						detailName = "回头用户消费订单抽成";
						break;
					case 23:
						detailName = "回头用户消费实付订单抽成";
						break;
					case 24:
						detailName = "R+外卖订单抽成";
						break;
					case 25:
						detailName = "R+外卖实付抽成";
						break;
					case 26:
						detailName = "第三方外卖订单抽成";
						break;
					case 27:
						detailName = "第三方外卖订单实付抽成";
						break;

					case 30:
						detailName = "注册验证码";
						break;
					case 31:
						detailName = "结店短信";
						break;
					case 40:
						detailName = "账户充值";
						break;
					default:
						break;

				}
				blog.setDetailName(detailName);
			}
		}
	 	return list;

	 }

	 @RequestMapping("downloadAccountLogExcel")
	 public void downloadAccountLogExcel(String path, HttpServletResponse response){
		 //定义excel工具类对象
		 ExcelUtil<BrandAccountLog> excelUtil=new ExcelUtil<>();
		 try {
			 excelUtil.download(path, response);
			 JOptionPane.showMessageDialog(null, "导出成功！");
			 log.info("excel导出成功");
		 }catch (Exception e){
			 log.error("下载品牌账户流水报表出错！");
			 e.printStackTrace();
		 }
	 }

 }
