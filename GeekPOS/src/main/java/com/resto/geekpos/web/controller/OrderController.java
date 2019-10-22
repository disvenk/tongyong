package com.resto.geekpos.web.controller;

import com.google.zxing.WriterException;
import com.resto.brand.core.util.QRCodeUtil;
import com.resto.geekpos.web.model.CheckResult;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.OrderService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by KONATA on 2016/9/6.
 */
@Controller
@RequestMapping("order")
public class OrderController extends GenericController {

    @Resource
    OrderService orderService;

    @RequestMapping(value="/qCode")
    public void qCode(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
//        CheckResult result = check(request, response, "qCode");
//        if (!result.isSuccess()) {
//            return;
//        }

        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);

        String contents = queryParams.get("contents").toString() ;

        try {
            OutputStream out = response.getOutputStream();
            QRCodeUtil.createQRCode(contents,"png",out);
            JSONObject json = new JSONObject();
            json.put("code", "200");
            json.put("msg", "请求成功");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value="/createOrder",method = RequestMethod.POST)
    public void createOrder(@RequestBody Order order, HttpServletRequest request, HttpServletResponse response) throws AppException {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "createOrder");
        if (!result.isSuccess()) {
            return;
        }
//        String order = queryParams.get("order").toString();
        JSONArray orderJA = new JSONArray(order);
        JSONObject orderJO = new JSONObject(orderJA.getJSONObject(0));
        String name = orderJO.optString("name");

        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);
//        orderService.createOrderByEmployee(order);

        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}
