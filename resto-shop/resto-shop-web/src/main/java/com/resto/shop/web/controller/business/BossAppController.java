package com.resto.shop.web.controller.business;

import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author yanjuan
 * @date 17/11/7 下午3:24
 */
@Controller
@RequestMapping("bossApp")
public class BossAppController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private ShopDetailService shopDetailService;

    //查询订单报表的接口

    @RequestMapping(value = "getOrderReport",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> getOrderReport(String brandId,String beginDate,String endDate,HttpServletRequest request){
        //定位数据库
         request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID, brandId);
         List<ShopDetail> shopDetailList = shopDetailService.selectByBrandId(brandId);

         List<Map<String,Object>> list = orderService.callBossAppOrdrReport(brandId,shopDetailList,beginDate,endDate);
         return list;
    }


}
