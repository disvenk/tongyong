//package com.resto.pos.web.controller;
//
//import com.resto.brand.core.entity.TxSmsResult;
//import com.resto.brand.core.util.CommonThirdExcel;
//import com.resto.brand.core.util.ExcelUtil;
//import com.resto.brand.web.model.Brand;
//import com.resto.brand.web.model.BrandSetting;
//import com.resto.brand.web.model.ShopDetail;
//import com.resto.brand.web.service.BrandService;
//import com.resto.brand.web.service.BrandSettingService;
//import com.resto.brand.web.service.PlatformService;
//import com.resto.brand.web.service.ShopDetailService;
//import com.resto.pos.web.rpcinterceptors.DataSourceTarget;
//import com.resto.shop.web.model.Order;
//import com.resto.shop.web.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by KONATA on 2017/2/7.
// * 第三方对接控制器
// */
//@Controller
//@RequestMapping("/third")
//public class ThirdController extends GenericController{
//
//    @Autowired
//    private BrandService brandService;
//
//    @Autowired
//    private BrandSettingService brandSettingService;
//
//    @Autowired
//    private ShopDetailService shopDetailService;
//
//    @Autowired
//    private PlatformService platformService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @RequestMapping("/index")
//    public ModelAndView index(){
//        return new ModelAndView("third");
//    }
//
//    @RequestMapping("/checkLogin")
//    @ResponseBody
//    public TxSmsResult checkLogin(String loginname, String password) throws IOException {
//        Brand currentBrand = null;
//        List<Brand> brands = brandService.selectList();
//        for(Brand brand : brands){
//            if(brand.getBrandSign().equals(loginname)){ //找到品牌
//                currentBrand = brand;
//            }
//        }
//
//        if(currentBrand == null){
//            return new TxSmsResult("用户名不存在",false);
//        }
//        BrandSetting setting = brandSettingService.selectByBrandId(currentBrand.getId());
//        if(!setting.getExportPassword().equals(password)){
//            return new TxSmsResult("密码错误",false);
//        }
//
//        return new TxSmsResult(true);
//    }
//
//    @RequestMapping("/export")
//    public void export(String brandSign,String beginTime,String endTime,HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Brand brand  = brandService.selectBySign(brandSign);
//        DataSourceTarget.setDataSourceName(brand.getId());
//        List<ShopDetail> shopList = shopDetailService.selectByBrandId(brand.getId());
//        String [] sheetName = new String[shopList.size()];
//        List<String []> titles = new ArrayList<>();
//        List<List<String []>> datas = new ArrayList<>();
//        for(int i = 0;i<shopList.size();i++){
//            sheetName[i] = shopList.get(i).getName();
//            String [] title = platformService.getParamByBrandId(brand.getId());
//            titles.add(title);
//            List<Order> orderList = orderService.getTodayFinishOrder(shopList.get(i).getId(),beginTime,endTime);
//            List<String []> order = orderService.getThirdData(orderList,title.length,brandSign);
//            datas.add(order);
//        }
//
//
//        byte [] bytes = CommonThirdExcel.writeToStream(sheetName,titles,datas);
//        response.addHeader("Content-Disposition", "attachment;filename="+
//                ExcelUtil.toUtf8String(brand.getBrandName() +  "营业数据"+beginTime + "-"+endTime)+".xls");
//        OutputStream toClient = new BufferedOutputStream(
//                response.getOutputStream());
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//        toClient.write(bytes);
//        toClient.flush();
//        toClient.close();
//
//    }
//
//
//    public static void main(String[] args) throws IOException {
//        URL url = new  URL("ftp://ftp.chateash.cn/IT/Resto/");
//        PrintWriter pw = new  PrintWriter(url.openConnection().getOutputStream());
//        pw.write( " this is a test " );
//        pw.flush();
//        pw.close();
//    }
//}
