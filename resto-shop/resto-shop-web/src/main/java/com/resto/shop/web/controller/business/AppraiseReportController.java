package com.resto.shop.web.controller.business;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.AppendToExcelUtil;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.dto.AppraiseDto;
import com.resto.brand.web.dto.AppraiseShopDto;
import com.resto.brand.web.dto.BrandOrderReportDto;
import com.resto.brand.web.dto.ShopOrderReportDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.TableQrcode;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.TableQrcodeService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.service.AppraiseService;
import com.resto.shop.web.service.OrderService;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("appraiseReport")
public class AppraiseReportController extends GenericController{

    @Resource
    private OrderService orderService;

    @Resource
    private BrandService brandService;

    @Resource
    private ShopDetailService shopDetailService;

    @Resource
    private TableQrcodeService tableQrcodeService;

    @Resource
    private AppraiseService appraiseService;

    @RequestMapping("/list")
    public String list(){
        return "appraiseReport/list";
    }

    @RequestMapping("/shop/list")
    public String shopList(){
        return "appraiseReport/shopList";
    }

    @RequestMapping("/brand_data")
    @ResponseBody
    public Result selectMoneyAndNumByDate(String beginDate,String endDate){
        Result result = new Result();
        try {
            result = this.getResult(beginDate, endDate);
            result.setSuccess(true);
        }catch (Exception e){
            return new Result(false);
        }
        return result;
    }

    private Result getResult(String beginDate, String endDate) {
        return getSuccessResult(this.getSuccess(beginDate, endDate));
    }

    private Map<String,Object> getSuccess(String beginDate,String endDate){
        beginDate+=" 00:00:00";
        endDate+=" 23:59:59";
        List<Order> olist =  orderService.selectListBybrandId(beginDate,endDate,getCurrentBrandId(), Common.NO);
        Map<String, Object> orderCountMap = orderService.callMoneyAndNumByDate(beginDate,endDate,getCurrentBrandId(),getBrandName(),getCurrentShopDetails());
        if (orderCountMap == null){
            return null;
        }
        int appraiseNum=0;//评价单数
        BrandOrderReportDto brandOrderReportDto = (BrandOrderReportDto)orderCountMap.get("brandId");
        List<ShopOrderReportDto> shopOrderReportDtos = (List<ShopOrderReportDto>)orderCountMap.get("shopId");
        int totalNum = brandOrderReportDto.getOrderCount();//已消费订单数
        BigDecimal orderMoney = BigDecimal.ZERO;//订单总额
        BigDecimal redMoney = BigDecimal.ZERO;//评论红包总额
        int oneStart=0;
        int twoStart=0;
        int threeStart=0;
        int fourStart=0;
        int fiveStart=0;
        //品牌数据
        AppraiseDto brandAppraise = new AppraiseDto();
        for (Order o : olist) {
            //评价的的单数 //评价红包
            if(o.getAppraise()!=null){
                appraiseNum++;
                redMoney = add(redMoney, o.getAppraise().getRedMoney());

                if(o.getAppraise().getLevel()==1){
                    oneStart++;
                }
                if(o.getAppraise().getLevel()==2){
                    twoStart++;
                }
                if(o.getAppraise().getLevel()==3){
                    threeStart++;
                }
                if(o.getAppraise().getLevel()==4){
                    fourStart++;
                }
                if(o.getAppraise().getLevel()==5){
                    fiveStart++;
                }
            }
            //消费的总金额
            orderMoney = add(orderMoney,o.getOrderMoney());
        }
        //设置品牌
        brandAppraise.setName(getBrandName());
        //设置评价单数
        brandAppraise.setAppraiseNum(appraiseNum);
        //设置评价率
        brandAppraise.setAppraiseRatio(topercent(appraiseNum, totalNum));
        //设置订单的总额
        brandAppraise.setTotalMoney(orderMoney);
        //设置红包的总额
        brandAppraise.setRedMoney(redMoney);
        //设置五星,四星...的数量
        brandAppraise.setOnestar(oneStart);
        brandAppraise.setTwostar(twoStart);
        brandAppraise.setThreestar(threeStart);
        brandAppraise.setFourstar(fourStart);
        brandAppraise.setFivestar(fiveStart);
        //店铺数据
        List<ShopDetail> shoplist = getCurrentShopDetails();
        if(shoplist.isEmpty()){
            shoplist = shopDetailService.selectByBrandId(getCurrentBrandId());
        }
        List<AppraiseDto> shopAppraiseList = new ArrayList<>();
        for (ShopDetail s : shoplist) {
            AppraiseDto shopAppraise = new AppraiseDto(s.getId(),s.getName(), appraiseNum, "", BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0, 0, 0);
            shopAppraise.setName(s.getName());
            //每个店铺设置默认值
            int appraiseNum2=0;//评价单数
            int totalNum2 = 0;//已消费订单数
            for (ShopOrderReportDto orderReportDto : shopOrderReportDtos){
                if (orderReportDto.getShopDetailId().equalsIgnoreCase(s.getId())){
                    totalNum2 = orderReportDto.getShop_orderCount();
                }
            }
            BigDecimal orderMoney2 = BigDecimal.ZERO;//订单总额
            BigDecimal redMoney2 = BigDecimal.ZERO;//评论红包总额
            //设置红包撬动率
            int oneStart2=0;
            int twoStart2=0;
            int threeStart2=0;
            int fourStart2=0;
            int fiveStart2=0;
            for (Order o : olist) {
                if(o.getShopDetailId().equals(s.getId())){
                    //评价的的单数 //评价红包
                    if(o.getAppraise()!=null){
                        appraiseNum2++;
                        redMoney2 = add(redMoney2, o.getAppraise().getRedMoney());

                        if(o.getAppraise().getLevel()==1){
                            oneStart2++;
                        }
                        if(o.getAppraise().getLevel()==2){
                            twoStart2++;
                        }
                        if(o.getAppraise().getLevel()==3){
                            threeStart2++;
                        }
                        if(o.getAppraise().getLevel()==4){
                            fourStart2++;
                        }
                        if(o.getAppraise().getLevel()==5){
                            fiveStart2++;
                        }
                    }
                    //消费的总金额
                    orderMoney2 = add(orderMoney2,o.getOrderMoney());
                }
            }
            //设置评价单数
            shopAppraise.setAppraiseNum(appraiseNum2);
            //设置评价率
            shopAppraise.setAppraiseRatio(topercent(appraiseNum2, totalNum2));
            //设置订单总额
            shopAppraise.setTotalMoney(orderMoney2);
            //设置红包的总额
            shopAppraise.setRedMoney(redMoney2);
            //设置星的数量
            shopAppraise.setOnestar(oneStart2);
            shopAppraise.setTwostar(twoStart2);
            shopAppraise.setThreestar(threeStart2);
            shopAppraise.setFourstar(fourStart2);
            shopAppraise.setFivestar(fiveStart2);
            shopAppraiseList.add(shopAppraise);
        }
        //把店铺和品牌的数据封装成map返回给前台
        Map<String,Object> map = new HashMap<>();
        map.put("brandAppraise", brandAppraise);
        map.put("shopAppraise", shopAppraiseList);
        return map;
    }

    private BigDecimal add(BigDecimal orderMoney, BigDecimal orderMoney2) {

        return orderMoney.add(orderMoney2);
    }

    //两个int类型的数据相除并得到百分比数据
    private static String topercent(int num1,int num2){
        if(num2==0){
            return "0%";
        }
        double d1 = num1;
        double d2 = num2;
        double d3 = d1/d2;
        NumberFormat num = NumberFormat.getPercentInstance();
        num.setMaximumIntegerDigits(3);
        num.setMaximumFractionDigits(2);
        String result=num.format(d3);
        return result;
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
        return "appraiseReport/shopReport";
    }

    @RequestMapping("create_brand_excel")
    @ResponseBody
    public Result report_brandExcel (String beginDate,String endDate,AppraiseDto appraiseDto,HttpServletRequest request, HttpServletResponse response){
        //导出文件名
        String fileName = "评论报表"+beginDate+"至"+endDate+".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns={"name","appraiseNum","appraiseRatio","redMoney","totalMoney","fivestar","fourstar","threestar","twostar","onestar"};
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
        List<AppraiseDto> result = new LinkedList<>();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("brandAppraise");
        filter.getExcludes().add("shopAppraises");
        if (appraiseDto.getBrandAppraise() != null) {
            String brandJson = JSON.toJSONString(appraiseDto.getBrandAppraise(), filter);
            AppraiseDto brandAppraiseDto = JSON.parseObject(brandJson, AppraiseDto.class);
            result.add(brandAppraiseDto);
        }
        String shopJson = JSON.toJSONString(appraiseDto.getShopAppraises(), filter);
        List<AppraiseDto> appraiseDtos = JSON.parseObject(shopJson, new TypeReference<List<AppraiseDto>>(){});
        result.addAll(appraiseDtos);
        String[][] headers = {{"品牌/店铺","25"},{"评价单数","25"},{"评价率","25"},{"评论红包总额","25"},{"订单总额(元)","25"},{"五星评价","25"},{"四星评价","25"},{"三星评价","25"},{"二星评价","25"},{"一星评价","25"}};
        //定义excel工具类对象
        ExcelUtil<AppraiseDto> excelUtil=new ExcelUtil<AppraiseDto>();
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
        try{
            Map<String, Object> selectMap = new HashMap<>();
            selectMap.put("beginDate",beginDate);
            selectMap.put("endDate",endDate);
            selectMap.put("shopId",shopId);
            List<AppraiseShopDto> appraiseShopDtos = appraiseService.selectAppraiseShopDto(selectMap);
            for(AppraiseShopDto ignored :appraiseShopDtos){
                TableQrcode tableQrcode=tableQrcodeService.selectByTableNumberShopId(shopId,ignored.getTablenumber());
                if(tableQrcode!=null){
                    ignored.setAreaname(tableQrcode.getAreaName());
                    ignored.setTablenumber(ignored.getTablenumber());
                }else{
                    ignored.setAreaname(null);
                    ignored.setTablenumber(null);
                }
            }
            object.put("appraiseShopDtos",appraiseShopDtos);
        }catch (Exception e){
            log.error("查看店铺评论报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(object);
    }

    @RequestMapping("create_shop_excel")
    @ResponseBody
    public Result report_shopExcel (String beginDate,String endDate,String shopId,AppraiseShopDto appraiseShopDto,HttpServletRequest request){
        //获取店铺名称
        ShopDetail s = shopDetailService.selectById(shopId);
        //导出文件名
        String fileName = "店铺评论报表"+beginDate.substring(0,10)+"至"+endDate.substring(0,10)+".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns={"levelName","feedBack","createTime","telephone","orderMoney","redMoney","content"};
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
        List<AppraiseShopDto> result = new LinkedList<>();
        if (appraiseShopDto.getAppraiseShopDtos() != null){
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("appraiseShopDtos");
            String json = JSON.toJSONString(appraiseShopDto.getAppraiseShopDtos(), filter);
            List<AppraiseShopDto> dtos = JSON.parseObject(json, new TypeReference<List<AppraiseShopDto>>(){});
            result.addAll(dtos);
        }
        String[][] headers = {{"评分","25"},{"评论对象","25"},{"评论时间","25"},{"手机号","25"},{"订单金额(元)","25"},{"评论金额","25"},{"评论内容","100"}};
        //定义excel工具类对象
        ExcelUtil<AppraiseShopDto> excelUtil=new ExcelUtil<AppraiseShopDto>();
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

    @RequestMapping("/appendShopExcel")
    @ResponseBody
    public Result appendShopExcel(String path, Integer startPosition, AppraiseShopDto appraiseShopDto){
        try{
            String[][] items = new String[appraiseShopDto.getAppraiseShopDtos().size()][];
            int i = 0;
            for (Map map : appraiseShopDto.getAppraiseShopDtos()){
                items[i] = new String[7];
                items[i][0] = map.get("levelName").toString();
                items[i][1] = map.get("feedBack").toString();
                items[i][2] = map.get("createTime").toString();
                items[i][3] = map.get("telephone").toString();
                items[i][4] = map.get("orderMoney").toString();
                items[i][5] = map.get("redMoney").toString();
                items[i][6] = map.get("content").toString();
                i++;
            }
            AppendToExcelUtil.insertRows(path,startPosition,items);
        }catch (Exception e){
            log.error("追加店铺评论报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult();
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //得到该品牌所有店铺
            List<ShopDetail> shopDetails = getCurrentShopDetails();
            //初始化店铺名称集合
            String[] shopNames = new String[1];
            //初始化数据集合
            AppraiseDto[][] result = new AppraiseDto[1][monthDay];
            //查询本月订单
            List<Order> orderList = orderService.selectListBybrandId(year.concat("-").concat(month).concat("-01"), year.concat("-").concat(month).concat("-" + String.valueOf(monthDay))
                    , getCurrentBrandId(), Common.NO);
            //用来保存每次循环没有用到的订单
            List<Order> orders = new ArrayList<>();
            //声明迭代器
            Iterator<Order> orderIterator = orderList.iterator();
            //初始化评论报表实体
            AppraiseDto appraiseDto = new AppraiseDto("","", 0, "0.00%", BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0, 0, 0);
            if (type.equals(Common.YES)) {
                shopNames = new String[shopDetails.size()];
                //重新new一个二位数组，行是店铺数、列是天数
                result = new AppraiseDto[shopDetails.size()][monthDay];
                //初始化i用来记录当前所在店铺
                int i = 0;
                //初始化j用来记录当前所在天数
                int j = 0;
                //循环每个店铺，每个店铺相当于一个sheet
                for (ShopDetail shopDetail : shopDetails){
                    //循环每一天，封装每个sheet的数据
                    for (int day = 0; day < monthDay; day++){
                        Date beginDate = getBeginDay(year, month, day);
                        Date endDate = getEndDay(year, month, day);
                        int orderCount = 0;
                        int appraiseCount = 0;
                        //判断当天是否又产生订单，有则进入逻辑判断
                        if (!orderList.isEmpty()) {
                            while (orderIterator.hasNext()) {
                                //得到当前订单
                                Order order = orderIterator.next();
                                //循环订单找到某店铺某一天的数据
                                if (order.getShopDetailId().equalsIgnoreCase(shopDetail.getId()) && order.getCreateTime().getTime() >= beginDate.getTime() && order.getCreateTime().getTime() <= endDate.getTime()) {
                                    //如果该笔订单产生评论
                                    if (order.getAppraise() != null) {
                                        //评论数递增
                                        appraiseCount++;
                                        //红包金额累加
                                        appraiseDto.setRedMoney(appraiseDto.getRedMoney().add(order.getAppraise().getRedMoney()));
                                        if (order.getAppraise().getLevel() == 1) {
                                            appraiseDto.setOnestar(appraiseDto.getOnestar() + 1);
                                        } else if (order.getAppraise().getLevel() == 2) {
                                            appraiseDto.setTwostar(appraiseDto.getTwostar() + 1);
                                        } else if (order.getAppraise().getLevel() == 3) {
                                            appraiseDto.setThreestar(appraiseDto.getThreestar() + 1);
                                        } else if (order.getAppraise().getLevel() == 4) {
                                            appraiseDto.setFourstar(appraiseDto.getFourstar() + 1);
                                        } else if (order.getAppraise().getLevel() == 5) {
                                            appraiseDto.setFivestar(appraiseDto.getFivestar() + 1);
                                        }
                                    }
                                    appraiseDto.setTotalMoney(appraiseDto.getTotalMoney().add(order.getOrderMoney()));
                                    if (StringUtils.isBlank(order.getParentOrderId())) {
                                        orderCount++;
                                    }
                                }else {
                                    orders.add(order);
                                }
                            }
                            appraiseDto.setAppraiseNum(appraiseCount);
                            appraiseDto.setAppraiseRatio(orderCount == 0 ? "0.00%" : new BigDecimal(appraiseCount).divide(new BigDecimal(orderCount), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                            orderIterator = orders.iterator();
                            orders = new ArrayList<>();
                        }
                        appraiseDto.setRedRatio(format.format(beginDate));
                        result[i][j] = appraiseDto;
                        j++;
                        appraiseDto = new AppraiseDto("","", 0, "0.00%", BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0, 0, 0);
                    }
                    shopNames[i] = shopDetail.getName();
                    //所在店铺下标累加
                    i++;
                    //将所在天数恢复初始值
                    j = 0;
                }
            }else {
                //得到所有店铺名称
                StringBuilder builder = new StringBuilder();
                for (ShopDetail shopDetail : shopDetails){
                    builder.append(shopDetail.getName()).append(",");
                }
                String shopName = builder.toString();
                shopName = shopName.substring(0, shopName.length() - 1);
                shopNames[0] = shopName;
                //初始化j用来记录当前所在天数
                int j = 0;
                //循环每一天，封装每个sheet的数据
                for (int day = 0; day < monthDay; day++){
                    Date beginDate = getBeginDay(year, month, day);
                    Date endDate = getEndDay(year, month, day);
                    int orderCount = 0;
                    int appraiseCount = 0;
                    //判断当天是否又产生订单，有则进入逻辑判断
                    if (!orderList.isEmpty()) {
                        while (orderIterator.hasNext()) {
                            //得到当前订单
                            Order order = orderIterator.next();
                            //循环订单找到某店铺某一天的数据
                            if (order.getCreateTime().getTime() >= beginDate.getTime() && order.getCreateTime().getTime() <= endDate.getTime()) {
                                //如果该笔订单产生评论
                                if (order.getAppraise() != null) {
                                    //评论数递增
                                    appraiseCount++;
                                    //红包金额累加
                                    appraiseDto.setRedMoney(appraiseDto.getRedMoney().add(order.getAppraise().getRedMoney()));
                                    if (order.getAppraise().getLevel() == 1) {
                                        appraiseDto.setOnestar(appraiseDto.getOnestar() + 1);
                                    } else if (order.getAppraise().getLevel() == 2) {
                                        appraiseDto.setTwostar(appraiseDto.getTwostar() + 1);
                                    } else if (order.getAppraise().getLevel() == 3) {
                                        appraiseDto.setThreestar(appraiseDto.getThreestar() + 1);
                                    } else if (order.getAppraise().getLevel() == 4) {
                                        appraiseDto.setFourstar(appraiseDto.getFourstar() + 1);
                                    } else if (order.getAppraise().getLevel() == 5) {
                                        appraiseDto.setFivestar(appraiseDto.getFivestar() + 1);
                                    }
                                }
                                appraiseDto.setTotalMoney(appraiseDto.getTotalMoney().add(order.getOrderMoney()));
                                if (StringUtils.isBlank(order.getParentOrderId())) {
                                    orderCount++;
                                }
                            }else {
                                orders.add(order);
                            }
                        }
                        appraiseDto.setAppraiseNum(appraiseCount);
                        appraiseDto.setAppraiseRatio(orderCount == 0 ? "0.00%" : new BigDecimal(appraiseCount).divide(new BigDecimal(orderCount), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
                        orderIterator = orders.iterator();
                        orders = new ArrayList<>();
                    }
                    appraiseDto.setRedRatio(format.format(beginDate));
                    result[0][j] = appraiseDto;
                    j++;
                    appraiseDto = new AppraiseDto("","", 0, "0.00%", BigDecimal.ZERO, BigDecimal.ZERO, "", 0, 0, 0, 0, 0);
                }
            }
            //封装excel基本信息
            Map<String, Object> map = new HashMap<>();
            map.put("brandName", getBrandName());
            map.put("beginDate", year.concat("-").concat(month).concat("-01"));
            map.put("reportType", typeName);// 表的头，第一行内容
            map.put("endDate", year.concat("-").concat(month).concat("-").concat(String.valueOf(monthDay)));
            map.put("num", "9");// 显示的位置
            map.put("timeType", "yyyy-MM-dd");
            map.put("reportTitle", shopNames);// 表的名字
            String[][] headers = {{"日期","25"},{"评价单数","25"},{"评价率","25"},{"评论红包总额","25"},{"订单总额(元)","25"},{"五星评价","25"},{"四星评价","25"},{"三星评价","25"},{"二星评价","25"},{"一星评价","25"}};
            String[] columns = {"redRatio","appraiseNum","appraiseRatio","redMoney","totalMoney","fivestar","fourstar","threestar","twostar","onestar"};
            ExcelUtil<AppraiseDto> excelUtil = new ExcelUtil<>();
            OutputStream out = new FileOutputStream(path);
            excelUtil.createMonthDtoExcel(headers, columns, result, out, map);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            log.error("生成月评论报表出错！");
            return new Result(false);
        }
        return getSuccessResult(path);
    }
}
