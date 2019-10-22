package com.resto.shop.web.controller.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.AppendToExcelUtil;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.web.dto.AllPlatformReportDto;
import com.resto.brand.web.dto.PlatformReportDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.PlatformOrder;
import com.resto.shop.web.service.PlatformOrderService;
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
import java.util.*;

/**
 * Created by xielc on 2017/7/11.
 */
@Controller
@RequestMapping("platformReport")
public class PlatformReportController extends GenericController {

    @Resource
    private PlatformOrderService platformOrderService;

    @Resource
    private ShopDetailService shopDetailService;

    @Resource
    private BrandService brandService;

    @RequestMapping("/list")
    public String list(){
        return "platformReport/list";
    }

    @RequestMapping("/shop/list")
    public String shopList(){
        return "platformReport/shopList";
    }

    @RequestMapping("/datas")
    @ResponseBody
    public Result selectMoneyAndNumByDate(String beginDate, String endDate){
        Result result = new Result();
        //第三方外卖品牌数据
        List<ShopDetail> shoplist = getCurrentShopDetails();
        if(shoplist==null){
            shoplist = shopDetailService.selectByBrandId(getCurrentBrandId());
        }
        AllPlatformReportDto allPlatformReportDto = new AllPlatformReportDto();
        int count=0;
        BigDecimal price=new BigDecimal("0.00");
        int allElmCount=0;
        BigDecimal allElmPrice=new BigDecimal("0.00");
        int allMtCount=0;
        BigDecimal allMtPrice=new BigDecimal("0.00");
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal elmTotalPrice = BigDecimal.ZERO;
        BigDecimal mtTotalPrice = BigDecimal.ZERO;
        List<PlatformReportDto> platformlist= new ArrayList<PlatformReportDto>();//第三方外卖店铺数据list
        long startTime=System.currentTimeMillis();
        /*for (ShopDetail shopDetail : shoplist) {*/
        for (int i = 0; i < shoplist.size(); i++) {
            ShopDetail shopDetail=shoplist.get(i);
            //PlatformReportDto platformReportDto=platformOrderService.selectByshopDetailId(beginDate,endDate,shopDetail.getId());
            PlatformReportDto platformReportDto=platformOrderService.proc_shopdetailId(beginDate,endDate,shopDetail.getId());
            count+=platformReportDto.getAllCount();
            if(platformReportDto.getAllPrice()!=null){
                price=price.add(platformReportDto.getAllPrice());
            }
            allElmCount+=platformReportDto.getElmCount();
            if(platformReportDto.getElmPrice()!=null){
                allElmPrice=allElmPrice.add(platformReportDto.getElmPrice());
            }
            allMtCount+=platformReportDto.getMtCount();
            if(platformReportDto.getMtPrice()!=null){
                allMtPrice=allMtPrice.add(platformReportDto.getMtPrice());
            }
            //第三方外卖店铺数据
            platformReportDto.setShopId(shopDetail.getId());
            platformReportDto.setShopName(shopDetail.getName());
            BigDecimal v=new BigDecimal("0.00");
            if(platformReportDto.getAllPrice()==null){
                platformReportDto.setAllPrice(v);
            }
            if(platformReportDto.getElmPrice()==null){
                platformReportDto.setElmPrice(v);
            }
            if(platformReportDto.getMtPrice()==null){
                platformReportDto.setMtPrice(v);
            }
            totalPrice.add(platformReportDto.getTotalPrice());
            elmTotalPrice.add(platformReportDto.getElmTotalPrice());
            mtTotalPrice.add(platformReportDto.getMtTotalPrice());
            platformlist.add(platformReportDto);
        }
        long endTime=System.currentTimeMillis();
        System.out.println("Time cost:------------------------>"+(endTime-startTime)+"millis");
        allPlatformReportDto.setBrandName(getBrandName());
        allPlatformReportDto.setCount(count);
        allPlatformReportDto.setPrice(price);
        allPlatformReportDto.setAllElmCount(allElmCount);
        allPlatformReportDto.setAllElmPrice(allElmPrice);
        allPlatformReportDto.setAllMtCount(allMtCount);
        allPlatformReportDto.setAllMtPrice(allMtPrice);
        allPlatformReportDto.setTotalPrice(totalPrice);
        allPlatformReportDto.setElmTotalPrice(elmTotalPrice);
        allPlatformReportDto.setMtTotalPrice(mtTotalPrice);
        //把三方外卖品牌和店铺的数据封装成map返回给前台
        Map<String,Object> map = new HashMap<>();
        map.put("allPlatformReportDto", allPlatformReportDto);
        map.put("platformlist", platformlist);
        result=getSuccessResult(map);
        return result;
    }

    @RequestMapping("create_brand_excel")
    @ResponseBody
    public Result report_brandExcel (String beginDate, String endDate, PlatformReportDto platformReportDto, HttpServletRequest request, HttpServletResponse response){

        //导出文件名
        String fileName = "第三方外卖报表报表"+beginDate+"至"+endDate+".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns={"shopName","allCount","elmCount","mtCount","allPrice","totalPrice","elmPrice","elmTotalPrice","mtPrice","mtTotalPrice"};
        //定义数据

        //定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
        Map<String,String> map = new HashMap<>();
        Brand brand = brandService.selectById(getCurrentBrandId());
        //获取店铺名称
        List<ShopDetail> shops = shopDetailService.selectByBrandId(getCurrentBrandId());
        String shopName="";
        for (ShopDetail shopDetail : shops) {
            shopName += shopDetail.getName()+",";
        }
        //去掉最后一个逗号
        shopName = shopName.substring(0, shopName.length()-1);
        map.put("brandName", brand.getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", beginDate);
        map.put("reportType", "第三方外卖报表");//表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "9");//显示的位置
        map.put("reportTitle", "外卖表");//表的名字
        map.put("timeType", "yyyy-MM-dd");
        List<PlatformReportDto> result = new LinkedList<>();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("brandAppraise");
        filter.getExcludes().add("shopAppraises");
        if (platformReportDto.getBrandAppraise() != null){
            String brandJson = JSON.toJSONString(platformReportDto.getBrandAppraise(),filter);
            AllPlatformReportDto allPlatformReportDto=JSON.parseObject(brandJson, AllPlatformReportDto.class);
            PlatformReportDto brandPlatformReportDto = new PlatformReportDto();
            brandPlatformReportDto.setShopName(allPlatformReportDto.getBrandName());
            brandPlatformReportDto.setAllCount(allPlatformReportDto.getCount());
            brandPlatformReportDto.setElmCount(allPlatformReportDto.getAllElmCount());
            brandPlatformReportDto.setMtCount(allPlatformReportDto.getAllMtCount());
            brandPlatformReportDto.setAllPrice(allPlatformReportDto.getPrice());
            brandPlatformReportDto.setElmPrice(allPlatformReportDto.getAllElmPrice());
            brandPlatformReportDto.setMtPrice(allPlatformReportDto.getAllMtPrice());
            brandPlatformReportDto.setTotalPrice(allPlatformReportDto.getTotalPrice());
            brandPlatformReportDto.setElmTotalPrice(allPlatformReportDto.getElmTotalPrice());
            brandPlatformReportDto.setMtTotalPrice(allPlatformReportDto.getMtTotalPrice());
            result.add(brandPlatformReportDto);
        }
        String shopJson = JSON.toJSONString(platformReportDto.getShopAppraises(), filter);
        List<PlatformReportDto> platformReportDtos = JSON.parseObject(shopJson, new TypeReference<List<PlatformReportDto>>(){});
        result.addAll(platformReportDtos);
        String[][] headers = {{"品牌/店铺","25"},{"外卖订单数","25"},{"饿了吗订单数","25"},{"美团外卖订单数","25"},{"外卖订单额","25"},{"外卖订单实收额","25"},{"饿了吗订单额","25"},{"饿了吗订单实收额","25"},{"美团外卖订单额","25"},{"美团外卖订单实收额","25"}};
        //定义excel工具类对象
        ExcelUtil<PlatformReportDto> excelUtil=new ExcelUtil<PlatformReportDto>();
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

    @RequestMapping("shopReport")
    public String showModal(String beginDate,String endDate,String shopId, Integer type,HttpServletRequest request){
        request.setAttribute("beginDate", beginDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("type", type);
        ShopDetail shop = shopDetailService.selectById(shopId);
        if(shopId!=null){
            request.setAttribute("shopId", shopId);
            request.setAttribute("shopName", shop.getName());
        }
        return "platformReport/shopReport";
    }

    @RequestMapping("shop_data")
    @ResponseBody
    public Result selectPlatformByShopId(String beginDate,String endDate,String shopId){
        JSONObject object = new JSONObject();
        try{
            ShopDetail shop = shopDetailService.selectById(shopId);
            List<PlatformOrder> platformOrders=platformOrderService.selectshopDetailIdList(beginDate,endDate,shopId);
            for(PlatformOrder platformOrder:platformOrders){
                platformOrder.setShopName(shop.getName());
            }
            object.put("platformOrders",platformOrders);
        }catch (Exception e){
            log.error("查看第三方外卖店铺报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(object);
    }

    @RequestMapping("create_shop_excel")
    @ResponseBody
    public Result report_shopExcel (String beginDate, String endDate, String shopId, PlatformOrder platformOrder, HttpServletRequest request){
        //获取店铺名称
        ShopDetail s = shopDetailService.selectById(shopId);
        //导出文件名
        String fileName = "店铺外卖报表"+beginDate+"至"+endDate+".xls";
        //定义读取文件的路径
        String path = request.getSession().getServletContext().getRealPath(fileName);
        //定义列
        String[]columns={"shopName","type","createTime","originalPrice","totalPrice","name","phone","payType"};
        //定义数据

        //定义一个map用来存数据表格的前四项,1.报表类型,2.品牌名称3,.店铺名称4.日期
        Map<String,String> map = new HashMap<>();
        Brand brand = brandService.selectById(getCurrentBrandId());

        //去掉最后一个逗号
        map.put("brandName", brand.getBrandName());
        map.put("shops", s.getName());
        map.put("beginDate", beginDate);
        map.put("reportType", "店铺外卖报表");//表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "6");//显示的位置
        map.put("reportTitle", "店铺外卖");//表的名字
        map.put("timeType", "yyyy-MM-dd");
        List<PlatformOrder> result = new LinkedList<>();
        if (platformOrder.getShopAppraises() != null){
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("platformOrderDetails");
            filter.getExcludes().add("shopAppraises");
            String json = JSON.toJSONString(platformOrder.getShopAppraises(), filter);
            List<PlatformOrder> dtos = JSON.parseObject(json, new TypeReference<List<PlatformOrder>>(){});
            result.addAll(dtos);
        }
        String[][] headers = {{"店铺","25"},{"外卖平台","25"},{"下单时间","25"},{"订单原价","25"},{"支付金额","25"},{"姓名","25"},{"手机号","25"},{"订单状态","25"}};
        //定义excel工具类对象
        ExcelUtil<PlatformOrder> excelUtil=new ExcelUtil<PlatformOrder>();
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
    public Result appendShopExcel(String path, Integer startPosition, PlatformOrder platformOrder){
        try{
            String[][] items = new String[platformOrder.getShopAppraises().size()][];
            int i = 0;
            for (Map map : platformOrder.getShopAppraises()){
                items[i] = new String[7];
                items[i][0] = map.get("shopName").toString();
                items[i][1] = map.get("type").toString();
                items[i][2] = map.get("createTime").toString();
                items[i][3] = map.get("originalPrice").toString();
                items[i][4] = map.get("totalPrice").toString();
                items[i][5] = map.get("name").toString();
                items[i][6] = map.get("phone").toString();
                items[i][7] = map.get("payType").toString();
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
    @RequestMapping("/platformOrderDetail")
    @ResponseBody
    public Result platformOrderDetail(String platformOrderId){
        Result result=null;
        List<PlatformOrder> platformOrders=platformOrderService.getPlatformOrderDetailList(platformOrderId);
        //获取店铺名称
        ShopDetail shopDetail = shopDetailService.selectById(platformOrders.get(0).getShopDetailId());
        for(PlatformOrder platformOrder:platformOrders){
            platformOrder.setShopName(shopDetail.getName());
        }
        if(platformOrders!=null && !platformOrders.isEmpty()){
            result=getSuccessResult(platformOrders);
        }
        return result;
    }
}
