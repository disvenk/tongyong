//package com.resto.wechat.web.task;
//
//import com.resto.brand.web.model.Brand;
//import com.resto.brand.web.model.BrandSetting;
//import com.resto.brand.web.model.WechatConfig;
//import com.resto.brand.web.service.BrandService;
//import com.resto.brand.web.service.BrandSettingService;
//import com.resto.brand.web.service.WechatChargeConfigService;
//import com.resto.brand.web.service.WechatConfigService;
//import com.resto.shop.web.model.Customer;
//import com.resto.shop.web.model.NewCustomCoupon;
//import com.resto.shop.web.service.CustomerService;
//import com.resto.shop.web.service.NewCustomCouponService;
//import com.resto.wechat.web.rpcinterceptors.DataSourceTarget;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//@Component("couponTask")
//public class CouponTask {
//
//    @Autowired
//    BrandService brandService;
//
//    @Autowired
//    NewCustomCouponService newCustomCouponService;
//
//    @Autowired
//    CustomerService customerService;
//
//    @Autowired
//    WechatConfigService wechatConfigService;
//
//    @Autowired
//    BrandSettingService brandSettingService;
//
////    @Scheduled(cron = "0/10 * *  * * ? ")   //每10秒执行一次
//    @Scheduled(cron = "0 0 7 * * ?")   //每天7点
//    public void addCoupon(){
//        try{
//            //得到所有品牌
//            List<Brand> brands = brandService.selectList();
//            for (Brand brand : brands){
//                WechatConfig config = wechatConfigService.selectByBrandId(brand.getId());
//                BrandSetting setting = brandSettingService.selectByBrandId(brand.getId());
//                DataSourceTarget.setDataSourceName(brand.getId());
//                //查询各个品牌下是否存在生日优惠卷
//                List<NewCustomCoupon> couponList = newCustomCouponService.selectBirthCoupon();
//                if (couponList == null){
//                    continue;
//                }else if (couponList.size() == 0){
//                    continue;
//                }
//                //得到各个品牌下录入生日信息了的用户
//                List<Customer> customerList = customerService.selectBirthUser();
//                if (customerList == null){
//                    continue;
//                }else if (customerList.size() == 0){
//                    continue;
//                }
//                //满足优惠券发放条件后向用户发放优惠卷
//                for (NewCustomCoupon coupon : couponList){
//                    for (Customer customer : customerList){
//                        //得到用户生日
//                        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
//                        String birthDay = format.format(customer.getCustomerDetail().getBirthDate());
//                        String distanceBirthdayDay = coupon.getDistanceBirthdayDay().toString();
//                        String day = getDay(birthDay,distanceBirthdayDay);
//                        if (day == null){
//                            continue;
//                        }
//                        if (day.equalsIgnoreCase(format.format(new Date()))){
//                            newCustomCouponService.insertBirthCoupon(coupon,customer,brand,config,setting);
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 得到一个时间前移几天的时间,birthDay为时间,day为前移或后延的天数
//     */
//    public String getDay(String birthDay, String day) {
//        try{
//            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
//            Date date = format.parse(birthDay);
//            long time = (date.getTime() / 1000) - Integer.parseInt(day) * 24 * 60 * 60;
//            date.setTime(time * 1000);
//            return format.format(date);
//        }catch(Exception e){
//            return null;
//        }
//    }
//}
