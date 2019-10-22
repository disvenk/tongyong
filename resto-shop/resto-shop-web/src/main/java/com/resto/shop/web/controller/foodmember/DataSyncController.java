package com.resto.shop.web.controller.foodmember;

import com.resto.brand.web.model.Brand;
import com.resto.brand.web.service.BrandService;
import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.OrderPaymentItem;
import com.resto.shop.web.service.OrderPaymentItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-08-28 15:32
 */

@Controller
@RequestMapping("foodMember")
public class DataSyncController extends GenericController {

    @Autowired
    OrderPaymentItemService orderPaymentItemService;

    @Autowired
    BrandService brandService;

    @RequestMapping(value = "orderPayMentItem")
    @ResponseBody
    public String orderPayMentItem(HttpServletRequest request,String baseUrl,@RequestBody List<OrderPaymentItem> orderPaymentItems){
        String requestURL = request.getRequestURL().toString();
        String brandSign = baseUrl.substring("http://".length(), baseUrl.indexOf("."));
        Brand brand = brandService.selectBySign(brandSign);
        log.info("获取到品牌"+brand);
       request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID,brand.getId() );
        log.info("应同步支付项条数:"+orderPaymentItems.size());
        int i = orderPaymentItemService.insertMany(orderPaymentItems);
        if(i==orderPaymentItems.size()){
            log.info("实际同步条数："+i);
            return "success";
        }
        log.info("实际同步条数："+i);
        return "fail";
    }

    @RequestMapping("updatePayMentItem")
    @ResponseBody
    public String updatePayMentItem(HttpServletRequest request,@RequestBody IdForm form){
        String requestURL = request.getRequestURL().toString();
        String brandSign = requestURL.substring("http://".length(), requestURL.indexOf("."));
        Brand brand = brandService.selectBySign(brandSign);
        request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID,brand.getId() );
        log.info("卡号:"+form.code);
        int i = orderPaymentItemService.updatePayMentByCode(form.code,form.orderId);
        if(i>0){
            log.info("实际更新条数："+i);
            return "success";
        }
        log.info("实际更新条数："+i);
        return "fail";
    }
}
