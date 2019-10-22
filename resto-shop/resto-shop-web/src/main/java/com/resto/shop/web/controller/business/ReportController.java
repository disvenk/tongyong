package com.resto.shop.web.controller.business;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import com.resto.brand.core.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.core.util.ExcelUtilSheetsUtil;
import com.resto.brand.web.dto.ArticleSellDto;
import com.resto.brand.web.dto.ExcelReportDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.OrderPaymentItem;
import com.resto.shop.web.service.ArticleFamilyService;
import com.resto.shop.web.service.OrderPaymentItemService;
import com.resto.shop.web.service.OrderService;

@Controller
@RequestMapping("report")
public class ReportController extends GenericController{
	
	@Resource
	OrderPaymentItemService orderPaymentItemService;
	
	@Resource
	OrderService orderService;
	
	@Resource
	BrandService brandService;
	
	@Resource
	ArticleFamilyService articleFamilyService;
	
	@Resource
	ShopDetailService shopDetailService;
	
	@RequestMapping("/shopIncome/list")
    public String getIncomelist(){
		
		return "report/shopIncome/list";
    }
	
	@RequestMapping("/articleSell/list")
	public String getArticleSellList(){
		
		return "report/articleSell/list";
	}
	
	@RequestMapping("/orderPaymentItems")
	@ResponseBody
	public Result orderPaymentItems(String beginDate,String endDate){
		return getSuccessResult(this.getResult(beginDate, endDate));
	}
	
	
	private List<OrderPaymentItem> getResult(String beginDate,String endDate){
		//初始化值，用来在前端显示
		List<OrderPaymentItem> list = new LinkedList<>();
		list.add(new OrderPaymentItem(BigDecimal.ZERO, 1,"微信支付"));
		list.add(new OrderPaymentItem(BigDecimal.ZERO, 6,"充值账户支付"));
		list.add(new OrderPaymentItem(BigDecimal.ZERO, 2,"红包支付"));
		list.add(new OrderPaymentItem(BigDecimal.ZERO, 3,"优惠券支付"));
		list.add(new OrderPaymentItem(BigDecimal.ZERO, 7,"充值赠送支付"));
		//收入条目
		List<OrderPaymentItem> olist = orderPaymentItemService.selectpaymentByPaymentMode(getCurrentShopId(),beginDate,endDate);
		for (OrderPaymentItem od : list) {
			for (OrderPaymentItem orderPaymentItem : olist) {
				if(od.getPaymentModeId().equals(orderPaymentItem.getPaymentModeId())){
					od.setPayValue(orderPaymentItem.getPayValue());
				}
			}
		}
		return list;
	}
	
	
	@RequestMapping("/orderArticleItems")
	@ResponseBody
	public Result reportList(String beginDate,String endDate,String sort){
		//菜品销售记录
		//return getSuccessResult(orderItemService.selectSaleArticleByDate(getCurrentShopId() ,beginDate, endDate,sort));
		if(beginDate==null){
			beginDate= DateUtil.formatDate(new Date(),"yyyy-MM-dd");
		}
		if(endDate==null){
			endDate= DateUtil.formatDate(new Date(),"yyyy-MM-dd");
		}
		return getSuccessResult(orderService.selectShopArticleByDate(getCurrentShopId(), beginDate, endDate, sort));
	}
	
	
	//店铺收入报表导出
		@RequestMapping("income_excel")
		@ResponseBody
		public void reportIncome(String beginDate,String endDate,HttpServletRequest request, HttpServletResponse response){
					//导出文件名
					String fileName = "收入详情报表"+beginDate+"至"+endDate+".xls";
					//定义读取文件的路径
					String path = request.getSession().getServletContext().getRealPath(fileName);
					//定义列
					String[]columns={"paymentModeVal","payValue"};
					//定义数据
					List<OrderPaymentItem> result = this.getResult(beginDate, endDate);
					Brand brand = brandService.selectById(getCurrentBrandId());
					//获取店铺名称
					ShopDetail shopDetail = shopDetailService.selectById(getCurrentShopId());
					Map<String,String> map = new HashMap<>();
					map.put("brandName", brand.getBrandName());
					map.put("shops", shopDetail.getName());
					map.put("beginDate", beginDate);
					map.put("reportType", "店铺收入报表");//表的头，第一行内容
					map.put("endDate", endDate);
					map.put("num", "1");//显示的位置
					map.put("reportTitle", "店铺收入");//表的名字
					map.put("timeType", "yyyy-MM-dd");
					
					String[][] headers = {{"支付类型","25"},{"支付金额(元)","25"}};
					//定义excel工具类对象
					ExcelUtil<OrderPaymentItem> excelUtil=new ExcelUtil<OrderPaymentItem>();
					try{
						OutputStream out = new FileOutputStream(path);
						excelUtil.ExportExcel(headers, columns, result, out, map);
						out.close();
						excelUtil.download(path, response);
						JOptionPane.showMessageDialog(null, "导出成功！");
						log.info("excel导出成功");
					}catch(Exception e){
						e.printStackTrace();
					}
		}
		
		//店铺 菜品  销售报表
		
		@RequestMapping("article_excel")
		@ResponseBody
		public void reportArticle(String beginDate,String endDate,String selectValue,String sort,HttpServletRequest request, HttpServletResponse response){
			//导出文件名
					String fileName = "菜品销售报表"+beginDate+"至"+endDate+".xls";
					//定义读取文件的路径
					String path = request.getSession().getServletContext().getRealPath(fileName);
					//定义列
					String[]columns={"articleFamilyName","articleName","shopSellNum"};
					//定义数据
					List<ArticleSellDto> result = new LinkedList<>();
					//定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
					Map<String,String> map = new HashMap<>();
					Brand brand = brandService.selectById(getCurrentBrandId());
					//获取店铺名称
					String shopId = getCurrentShopId();
					ShopDetail shop = shopDetailService.selectById(shopId);
					map.put("brandName", brand.getBrandName());
					map.put("shops", shop.getName());
					map.put("beginDate", beginDate);
					map.put("reportType", "菜品销售报表");//表的头，第一行内容
					map.put("endDate", endDate);
					map.put("num", "2");//显示的位置
					map.put("reportTitle", "菜品销售");//表的名字
					map.put("timeType", "yyyy-MM-dd");
					
					//定义excel表格的表头
					if(selectValue==null||"".equals(selectValue)){
						selectValue="全部";
						result = orderService.selectShopArticleByDate(shopId, beginDate, endDate, sort);
					}else{
						//根据菜品分类的名称获取菜品分类的id
						String articleFamilyId = articleFamilyService.selectByName(selectValue);
						result = orderService.selectShopArticleByDateAndArcticleFamilyId(beginDate, endDate,shopId,articleFamilyId,sort);
					}
					String[][] headers = {{"菜品分类("+selectValue+")","25"},{"菜品名称","25"},{"菜品销量(份)","25"}};
					
					//定义excel工具类对象
					ExcelUtil<ArticleSellDto> excelUtil=new ExcelUtil<ArticleSellDto>();
					try{
						OutputStream out = new FileOutputStream(path);
						excelUtil.ExportExcel(headers, columns, result, out, map);
						out.close();
						excelUtil.download(path, response);
						JOptionPane.showMessageDialog(null, "导出成功！");
						log.info("excel导出成功");
					}catch(Exception e){
						e.printStackTrace();
					}
					
		}
		
		
		//店铺数据导出excel（包含菜品和收入的信息放在同一excel表格不同的sheet对象上）
		
		@RequestMapping("shop_excel")
		@ResponseBody
		public void reportShopExcel(String beginDate,String endDate,String selectValue,String sort,HttpServletRequest request, HttpServletResponse response){
					//导出文件名
					String fileName = "结算报表"+beginDate+"至"+endDate+".xls";
					//定义读取文件的路径
					String path = request.getSession().getServletContext().getRealPath(fileName);
					
					Brand brand = brandService.selectById(getCurrentBrandId());
					//获取店铺名称
					String shopId = getCurrentShopId();
					ShopDetail shop = shopDetailService.selectById(shopId);
					List<ExcelReportDto> list = new LinkedList<>();
					
					//定义第一个sheet里的内容
					ExcelReportDto excel = new ExcelReportDto();
					String[] columns  = {"paymentModeVal","payValue"};
					excel.setColumns(columns);
					Map<String,String> map = new HashMap<>();
					map.put("brandName", brand.getBrandName());
					map.put("shops", shop.getName());
					map.put("beginDate", beginDate);
					map.put("reportType", "店铺收入报表");//表的头，第一行内容
					map.put("endDate", endDate);
					map.put("num", "1");//显示的位置
					map.put("reportTitle", "店铺收入");//表的名字
					map.put("timeType", "yyyy-MM-dd");
					excel.setMap(map);
					String[][] headers = {{"支付类型","25"},{"支付金额(元)","25"}};
					excel.setHeaders(headers);
					//定义数据
					List<OrderPaymentItem> result = this.getResult(beginDate, endDate);
					excel.setResult(result);
					list.add(excel);
					
					//定义第二个sheet里的内容
					ExcelReportDto excel2 = new ExcelReportDto();
					String[]columns2={"articleFamilyName","articleName","shopSellNum"};
					excel2.setColumns(columns2);//列
					
					//定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
					Map<String,String> map2 = new HashMap<>();
					map2.put("brandName", brand.getBrandName());
					map2.put("shops", shop.getName());
					map2.put("beginDate", beginDate);
					map2.put("reportType", "菜品销售报表");//表的头，第一行内容
					map2.put("endDate", endDate);
					map2.put("num", "2");//显示的位置
					map2.put("reportTitle", "菜品销售");//表的名字
					map2.put("timeType", "yyyy-MM-dd");
					excel2.setMap(map2);//map对象
					//定义数据
					List<ArticleSellDto> result2 = new LinkedList<>();
					//定义excel表格的表头
					if(selectValue==null||"".equals(selectValue)){
						selectValue="全部";
						result2 = orderService.selectShopArticleByDate(shopId, beginDate, endDate, sort);
					}else{
						//根据菜品分类的名称获取菜品分类的id
						String articleFamilyId = articleFamilyService.selectByName(selectValue);
						result2 = orderService.selectShopArticleByDateAndArcticleFamilyId(beginDate, endDate,shopId,articleFamilyId,sort);
					}
					String[][] headers2 = {{"菜品分类("+selectValue+")","25"},{"菜品名称","25"},{"菜品销量(份)","25"}};
					
					excel2.setHeaders(headers2);//表格的头
					excel2.setResult(result2);//获取数据
					list.add(excel2);
					
					//定义excel工具类对象
					ExcelUtilSheetsUtil ex = new ExcelUtilSheetsUtil();
					try{
						OutputStream out = new FileOutputStream(path);
						HSSFWorkbook workbook = new HSSFWorkbook();
						
						for ( ExcelReportDto er : list) {
							ex.ExportExcel(workbook, er.getColumns(), er.getHeaders(), er.getMap(), er.getResult(),out);
						}
						workbook.write(out);
						out.close();
						JOptionPane.showMessageDialog(null, "导出成功！");
						log.info("excel导出成功");
					}catch(Exception e){
						e.printStackTrace();
					}
					
		}
	
}
