//package com.resto.pos.web.task;
//
//import com.resto.brand.core.util.CommonThirdExcel;
//import com.resto.brand.core.util.ExcelUtil;
//import com.resto.brand.core.util.FtpUtils;
//import com.resto.brand.web.model.Brand;
//import com.resto.brand.web.model.ShopDetail;
//import com.resto.brand.web.service.BrandService;
//import com.resto.brand.web.service.PlatformService;
//import com.resto.brand.web.service.ShopDetailService;
//import com.resto.pos.web.rpcinterceptors.DataSourceTarget;
//import com.resto.shop.web.model.Order;
//import com.resto.shop.web.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by carl on 2016/12/26.
// */
//@Component("thirdTask")
//public class ThirdTask {
//
//    @Autowired
//    private BrandService brandService;
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
//    @Scheduled(cron = "59 59 23 * * ?")   //每5秒执行一次
//    public void job1() throws ClassNotFoundException, IOException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String brandSign = "luroufan";
//        String beginTime = sdf.format(new Date());
//        String endTime = sdf.format(new Date());
//        //卤肉饭
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
//        FtpUtils.upload("ftp://ftp.chateash.cn/IT/Resto/",sdf.format(new Date())+".xls",bytes);
//
//    }
//
//    public static void main(String[] args) {
//
//    }
//
//}
