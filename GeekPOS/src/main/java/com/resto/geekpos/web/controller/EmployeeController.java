package com.resto.geekpos.web.controller;

import com.resto.geekpos.web.model.CheckResult;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import com.resto.shop.web.model.Employee;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.service.EmployeeService;
import com.resto.geekpos.web.constant.DictConstants;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.service.OrderService;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by carl on 2016/9/5.
 */
@Controller
@RequestMapping("employee")
public class EmployeeController extends GenericController{

    @Resource
    EmployeeService shopEmployeeService;

    @Resource
    OrderService orderService;

    @RequestMapping(value = "getEmployee" , method = RequestMethod.POST)
    public void getEmployee(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getEmployee");
        if (!result.isSuccess()) {
            return;
        }

        String brandId = queryParams.get("brandId").toString();
        String telephone = queryParams.get("telephone").toString();
        DataSourceTarget.setDataSourceName(brandId);

        Employee employee = shopEmployeeService.selectEmployeeByTel(telephone);

        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        json.put("data", new JSONObject(employee));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value="/getOrder",method = RequestMethod.POST)
    public void getOrder(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getOrder");
        if (!result.isSuccess()) {
            return;
        }
        String brandId = queryParams.get("brandId").toString();
        String userId = queryParams.get("userId").toString();
        String shopId = queryParams.get("shopId").toString();
        DataSourceTarget.setDataSourceName(brandId);

        List<Order> order = orderService.getOrderByEmployee(userId, shopId);

        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        json.put("data", order);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

}
