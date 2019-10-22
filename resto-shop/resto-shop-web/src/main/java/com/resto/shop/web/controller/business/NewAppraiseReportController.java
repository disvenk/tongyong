 package com.resto.shop.web.controller.business;

 import com.alibaba.fastjson.JSON;
 import com.alibaba.fastjson.JSONObject;
 import com.alibaba.fastjson.TypeReference;
 import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.core.util.ExcelUtil;
 import com.resto.brand.web.dto.AppraiseDto;
 import com.resto.brand.web.dto.AppraiseShopDto;
 import com.resto.brand.web.model.Brand;
 import com.resto.brand.web.model.ShopDetail;
 import com.resto.brand.web.service.BrandService;
 import com.resto.brand.web.service.ShopDetailService;
 import com.resto.shop.web.constant.Common;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.dto.AppraiseNewBrandDto;
 import com.resto.shop.web.dto.AppraiseNewDto;
 import com.resto.shop.web.dto.AppraiseNewShopDetailDto;
 import com.resto.shop.web.dto.AppraiseNewShopDto;
 import com.resto.shop.web.service.AppraiseNewService;
 import org.springframework.beans.BeanUtils;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;
 import javax.annotation.Resource;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.swing.*;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.math.BigDecimal;
 import java.time.Year;
 import java.util.*;

 @Controller
 @RequestMapping("newAppraiseReport")
 public class NewAppraiseReportController extends GenericController{

     @Resource
     private BrandService brandService;

     @Resource
     private ShopDetailService shopDetailService;

     @Resource
     private AppraiseNewService appraiseNewService;

     @RequestMapping("/list")
     public String list(){
         return "newAppraiseReport/list";
     }

     @RequestMapping("/shop/list")
     public String shopList(){
         return "newAppraiseReport/shopList";
     }

     @RequestMapping("/brand_data")
     @ResponseBody
     public Result selectMoneyAndNumByDate(String beginDate,String endDate){
         String brandId=getCurrentBrandId();
         List<AppraiseNewBrandDto> appraiseNewBrands = appraiseNewService.selectAppraiseNewBrand(beginDate,endDate,brandId);
         Brand  brand = brandService.selectById(brandId);
         if(appraiseNewBrands!=null && !appraiseNewBrands.isEmpty() && appraiseNewBrands.get(0)!=null){//mybitis查询第一行存在null
             for(AppraiseNewBrandDto appraiseNewBrandDto:appraiseNewBrands){
                 appraiseNewBrandDto.setBrandName(brand.getBrandName());
                 appraiseNewBrandDto.setAppraiseRatio(appraiseNewBrandDto.getAppraiseRatio().multiply(new BigDecimal(100)));
             }
         }else{
             AppraiseNewBrandDto appraiseNewBrandDto=new AppraiseNewBrandDto();
             appraiseNewBrandDto.setBrandName(brand.getBrandName());
             appraiseNewBrands.remove(0);
             appraiseNewBrands.add(appraiseNewBrandDto);
         }
         List<ShopDetail> shopDetails = shopDetailService.selectByBrandId(brandId);
         List<AppraiseNewShopDto> shopAppraiseList = new ArrayList<>();
         for(ShopDetail s:shopDetails){
             List<AppraiseNewShopDto> appraiseNewShops = appraiseNewService.selectAppraiseNewShop(beginDate,endDate,s.getId());
             if(appraiseNewShops!=null && !appraiseNewShops.isEmpty() && appraiseNewShops.get(0)!=null){
                 for(AppraiseNewShopDto appraiseNewShopDto:appraiseNewShops){
                     appraiseNewShopDto.setShopName(s.getName());
                     appraiseNewShopDto.setShopId(s.getId());
                     if(appraiseNewShopDto.getAppraiseRatio()!=null){
                         appraiseNewShopDto.setAppraiseRatio(appraiseNewShopDto.getAppraiseRatio().multiply(new BigDecimal(100)));
                     }
                     shopAppraiseList.add(appraiseNewShopDto);
                 }
             }else {
                 AppraiseNewShopDto appraiseNewShopDto = new AppraiseNewShopDto();
                 appraiseNewShopDto.setShopName(s.getName());
                 appraiseNewShopDto.setShopId(s.getId());
                 shopAppraiseList.add(appraiseNewShopDto);
             }
         }
         //把店铺和品牌的数据封装成map返回给前台
         Map<String,Object> map = new HashMap<>();
         map.put("brandAppraise", appraiseNewBrands);
         map.put("shopAppraise", shopAppraiseList);
        return getSuccessResult(map);
     }

     @RequestMapping("shopReport")
     public String showModal(String beginDate,String endDate,String shopId,HttpServletRequest request){
         request.setAttribute("beginDate", beginDate);
         request.setAttribute("endDate", endDate);
         ShopDetail shop = shopDetailService.selectById(shopId);
         if(shopId!=null){
             request.setAttribute("shopId", shopId);
             request.setAttribute("shopName", shop.getName());
         }
         return "newAppraiseReport/shopReport";
     }

     @RequestMapping("create_brand_excel")
     @ResponseBody
     public Result report_brandExcel (String beginDate,String endDate,AppraiseDto appraiseDto,HttpServletRequest request){
         //导出文件名
         String fileName = "评论报表"+beginDate+"至"+endDate+".xls";
         //定义读取文件的路径
         String path = request.getSession().getServletContext().getRealPath(fileName);
         //定义列
         String[]columns={"name","num","appraiseRatio","allLevel","service","exhibit","ambience","conditions","price"};
         //定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
         Map<String,String> map = new HashMap<>();
         Brand brand = brandService.selectById(getCurrentBrandId());
         //获取店铺名称
         List<ShopDetail> shops = shopDetailService.selectByBrandId(getCurrentBrandId());
         String shopName = "";
         for (ShopDetail shopDetail : shops) {
             shopName += shopDetail.getName()+",";
         }
         //去掉最后一个逗号
         shopName = shopName.substring(0, shopName.length()-1);
         map.put("brandName", brand.getBrandName());
         map.put("shops", shopName);
         map.put("beginDate", beginDate);
         map.put("reportType", "品牌评论报表");//表的头，第一行内容
         map.put("endDate", endDate);
         map.put("num", "9");//显示的位置
         map.put("reportTitle", "品牌评论");//表的名字
         map.put("timeType", "yyyy-MM-dd");
         List<AppraiseNewDto> result = new LinkedList<>();
         SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
         filter.getExcludes().add("brandAppraise");
         filter.getExcludes().add("shopAppraises");
         if (appraiseDto.getBrandAppraise() != null) {
             String brandJson = JSON.toJSONString(appraiseDto.getBrandAppraise(), filter);
             AppraiseNewDto brandAppraiseNewDto = JSON.parseObject(brandJson, AppraiseNewDto.class);
             brandAppraiseNewDto.setName(brandAppraiseNewDto.getBrandName());
             brandAppraiseNewDto.setNum(brandAppraiseNewDto.getBrandNum());
             result.add(brandAppraiseNewDto);
         }
         String shopJson = JSON.toJSONString(appraiseDto.getShopAppraises(), filter);
         List<AppraiseNewDto> shopAppraiseNewDto = JSON.parseObject(shopJson, new TypeReference<List<AppraiseNewDto>>(){});
         shopAppraiseNewDto.forEach(s -> {
             s.setName(s.getShopName());
             s.setNum(s.getShopNum());
         });
         result.addAll(shopAppraiseNewDto);
         String[][] headers = {{"品牌/店铺名称","25"},{"评论数量","25"},{"评价率","25"},{"总评分","25"},{"服务","25"},{"出品","25"},{"氛围","25"},{"环境","25"},{"性价比","25"}};
         //定义excel工具类对象
         ExcelUtil<AppraiseNewDto> excelUtil=new ExcelUtil<AppraiseNewDto>();
         OutputStream out  = null;
         try{
             out = new FileOutputStream(path);
             excelUtil.ExportExcel(headers, columns, result, out, map);
             out.close();
         }catch(Exception e){
             e.printStackTrace();
             return new Result(false);
         }finally {
             if (out != null) {
                 try {
                     out.close();
                 } catch (IOException io) {
                     io.printStackTrace();
                 }
             }
         }
         return getSuccessResult(path);
     }

     @RequestMapping("/downloadBrandExcel")
     public void downloadBrandExcel(String path , HttpServletResponse response){
         ExcelUtil<Object> excelUtil = new ExcelUtil<>();
         try{
             excelUtil.download(path, response);
             JOptionPane.showMessageDialog(null, "导出成功！");
             log.info("excel导出成功");
         }catch (Exception e){
             e.printStackTrace();
         }
     }

     @RequestMapping("shop_data")
     @ResponseBody
     public Result selectAppraiseByShopId(String beginDate,String endDate,String shopId){
         JSONObject object = new JSONObject();
         List<AppraiseNewShopDetailDto> appraiseNewShopDetails = appraiseNewService.selectAppraiseNewShopDetail(beginDate,endDate,shopId);
         object.put("appraiseShopDtos",appraiseNewShopDetails);
         return getSuccessResult(object);
     }

     @RequestMapping("create_shop_excel")
     @ResponseBody
     public Result report_shopExcel (String beginDate,String endDate,String shopId,AppraiseShopDto appraiseShopDto,HttpServletRequest request){
         //获取店铺名称
         ShopDetail s = shopDetailService.selectById(shopId);
         //导出文件名
         String fileName = "店铺评论报表"+beginDate+"至"+endDate+".xls";
         //定义读取文件的路径
         String path = request.getSession().getServletContext().getRealPath(fileName);
         //定义列
         String[]columns={"createTime","telephone","orderMoney","redMoney","level","service","exhibit","ambience","conditions","price","content","feedback","zanArticle","spitArticle"};
         //定义数据

         //定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
         Map<String,String> map = new HashMap<>();
         Brand brand = brandService.selectById(getCurrentBrandId());

         //去掉最后一个逗号
         map.put("brandName", brand.getBrandName());
         map.put("shops", s.getName());
         map.put("beginDate", beginDate);
         map.put("reportType", "店铺评论报表");//表的头，第一行内容
         map.put("endDate", endDate);
         map.put("num", "6");//显示的位置
         map.put("reportTitle", "店铺评论");//表的名字
         map.put("timeType", "yyyy-MM-dd");
         List<AppraiseNewShopDetailDto> result = new LinkedList<>();
         if (appraiseShopDto.getAppraiseShopDtos() != null){
             SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
             filter.getExcludes().add("appraiseShopDtos");
             String json = JSON.toJSONString(appraiseShopDto.getAppraiseShopDtos(), filter);
             List<AppraiseNewShopDetailDto> dtos = JSON.parseObject(json, new TypeReference<List<AppraiseNewShopDetailDto>>(){});
             result.addAll(dtos);
         }
         String[][] headers = {{"评论时间","25"},{"用户手机号","25"},{"订单金额","25"},{"评论红包金额","25"},{"评分","25"},{"服务","25"},{"出品","25"},{"氛围","25"},{"环境","25"},{"性价比","25"},{"评论内容","100"},{"评论标签","100"},{"推荐菜品","100"},{"吐槽菜品","100"}};
         //定义excel工具类对象
         ExcelUtil<AppraiseNewShopDetailDto> excelUtil=new ExcelUtil<AppraiseNewShopDetailDto>();
         OutputStream out  = null;
         try{
             out = new FileOutputStream(path);
             excelUtil.ExportExcel(headers, columns, result, out, map);
             out.close();
         }catch(Exception e){
             e.printStackTrace();
             return new Result(false);
         }finally {
             if (out != null) {
                 try {
                     out.close();
                 } catch (IOException io) {
                 }
             }
         }
         return getSuccessResult(path);
     }


     @RequestMapping("/downloadShopExcel")
     public void downloadShopExcel(String path, HttpServletResponse response){
         ExcelUtil<Object> excelUtil = new ExcelUtil<>();
         try{
             excelUtil.download(path, response);
             JOptionPane.showMessageDialog(null, "导出成功！");
             log.info("excel导出成功");
         }catch (Exception e){
             e.printStackTrace();
         }
     }

     @RequestMapping("/createMonthDto")
     @ResponseBody
     public Result createMonthDto(String year, String month, Integer type, HttpServletRequest request){
         //得到传入的某年某月一共有多少天
         Integer monthDay = getMonthDay(year, month);
         // 导出文件名
         String typeName = type.equals(Common.YES) ? "店铺评论报表月报表" : "品牌评论报表月报表" ;
         String str = typeName + year.concat("-").concat(month).concat("-01") + "至"
                 + year.concat("-").concat(month).concat("-").concat(String.valueOf(monthDay)) + ".xls";
         //得到文件存储路径
         String path = request.getSession().getServletContext().getRealPath(str);
         try {
             //初始化店铺名称集合
             String[] shopNames = new String[1];
             //初始化数据集合
             List<AppraiseNewDto> result = new LinkedList<>();
             String beginDate=year+"-"+month+"-01";
             String endDate= Year.of(Integer.parseInt(year)).atMonth(Integer.parseInt(month)).atEndOfMonth().toString();
             if (type.equals(1)) {//店铺
                 List<ShopDetail> shopDetails = getCurrentShopDetails();
                 List<AppraiseNewShopDto> shopAppraiseNewList = new ArrayList<>();
                 for(ShopDetail s:shopDetails){
                     List<AppraiseNewShopDto> appraiseNewShops = appraiseNewService.selectAppraiseNewShop(beginDate,endDate,s.getId());
                     if(appraiseNewShops!=null && !appraiseNewShops.isEmpty() && appraiseNewShops.get(0)!=null){
                         for(AppraiseNewShopDto appraiseNewShopDto:appraiseNewShops){
                             appraiseNewShopDto.setShopName(s.getName());
                             appraiseNewShopDto.setShopId(s.getId());
                             if(appraiseNewShopDto.getAppraiseRatio()!=null){
                                 appraiseNewShopDto.setAppraiseRatio(appraiseNewShopDto.getAppraiseRatio().multiply(new BigDecimal(100)));
                             }
                             shopAppraiseNewList.add(appraiseNewShopDto);
                         }
                     }else {
                         AppraiseNewShopDto appraiseNewShopDto = new AppraiseNewShopDto();
                         appraiseNewShopDto.setShopName(s.getName());
                         appraiseNewShopDto.setShopId(s.getId());
                         shopAppraiseNewList.add(appraiseNewShopDto);
                     }
                 }
                 List<AppraiseNewDto> shopAppraiseNewDtos = new ArrayList<>();
                 shopAppraiseNewList.forEach(s -> {
                     AppraiseNewDto appraiseNewDto=new AppraiseNewDto();
                     BeanUtils.copyProperties(s,appraiseNewDto);
                     appraiseNewDto.setName(s.getShopName());
                     appraiseNewDto.setNum(s.getShopNum());
                     shopAppraiseNewDtos.add(appraiseNewDto);
                 });
                 result.addAll(shopAppraiseNewDtos);
             }else{//品牌
                 List<AppraiseNewDto> brandAppraiseNewDtos = new ArrayList<>();
                 List<AppraiseNewBrandDto> appraiseNewBrands = appraiseNewService.selectAppraiseNewBrand(beginDate,endDate,getCurrentBrandId());
                 appraiseNewBrands.forEach(d -> {
                     AppraiseNewDto appraiseNewDto=new AppraiseNewDto();
                     BeanUtils.copyProperties(d,appraiseNewDto);
                     appraiseNewDto.setName(getBrandName());
                     appraiseNewDto.setNum(d.getBrandNum());
                     brandAppraiseNewDtos.add(appraiseNewDto);
                 });
                 result.addAll(brandAppraiseNewDtos);
             }
             //封装excel基本信息
             Map<String, String> map = new HashMap<>();
             map.put("brandName", getBrandName());
             map.put("beginDate", year.concat("-").concat(month).concat("-01"));
             map.put("reportType", typeName);// 表的头，第一行内容
             map.put("endDate", year.concat("-").concat(month).concat("-").concat(String.valueOf(monthDay)));
             map.put("num", "9");// 显示的位置
             map.put("timeType", "yyyy-MM-dd");
             //map.put("reportTitle", shopNames);// 表的名字
             map.put("reportTitle", "ddd");// 表的名字
             String[][] headers = {{"品牌/店铺名称","25"},{"评论数量","25"},{"评价率","25"},{"总评分","25"},{"服务","25"},{"出品","25"},{"氛围","25"},{"环境","25"},{"性价比","25"}};
             String[]columns={"name","num","appraiseRatio","allLevel","service","exhibit","ambience","conditions","price"};
             ExcelUtil<AppraiseNewDto> excelUtil = new ExcelUtil<>();
             OutputStream out = new FileOutputStream(path);
             excelUtil.ExportExcel(headers, columns, result, out, map);
             out.close();
         }catch (Exception e){
             e.printStackTrace();
             log.error("生成月评论报表出错！");
             return new Result(false);
         }
         return getSuccessResult(path);
     }
 }