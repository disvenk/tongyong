package com.resto.geekpos.web.controller;

import com.resto.brand.web.model.Employee;
import com.resto.brand.web.model.SysLoginLog;
import com.resto.brand.web.model.VCode;
import com.resto.brand.web.service.EmployeeService;
import com.resto.brand.web.service.TokenService;
import com.resto.brand.web.service.VCodeService;
import com.resto.geekpos.web.constant.DictConstants;
import com.resto.geekpos.web.model.CheckResult;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by KONATA on 2016/9/5.
 */
@Controller
@RequestMapping("auth")
public class AuthController extends GenericController {

    @Autowired
    private VCodeService VCodeService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmployeeService employeeService;



    @RequestMapping(value = "/getLogin", method = RequestMethod.POST)
    public void getLogin(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getLogin");
        if (!result.isSuccess()) {
            return;
        }

        Boolean loginSuccess = false;
        String message = "";
        String code = DictConstants.SUCCESS;
        Employee employee = null;
        String phone = queryParams.get("userName").toString();


        //判断登陆模式
        String loginType = queryParams.get("loginType").toString();
        if (!loginType.equals(DictConstants.SCAN_CODE)) { //常规登陆
            VCode vCode = VCodeService.selectLastPhoneInfo(phone);
            if (vCode != null) {
                loginSuccess = queryParams.get("password").equals(vCode.getvCode());
                message = loginSuccess == true ? "请求成功" : "登陆失败，验证码错误！";
                code = loginSuccess == true ? DictConstants.SUCCESS : DictConstants.FAIL;
            } else {
                loginSuccess = false;
                message = "验证码获取失败，请重新获取！";
                code = DictConstants.FAIL;
            }
            if (loginSuccess) {
                employee = employeeService.getEmployeeByPhone(phone);
                message = employee != null ? "请求成功" : "用户不存在！";
                code = employee != null ? DictConstants.SUCCESS : DictConstants.FAIL;
            }
        } else {
            employee = employeeService.getEmployeeById(queryParams.get("userId").toString());
            message = employee != null ? "请求成功" : "用户不存在！";
            code = employee != null ? DictConstants.SUCCESS : DictConstants.FAIL;

        }

        loginSuccess = employee == null ? false : true;

        JSONObject json = new JSONObject();
        if (loginSuccess) {
            JSONObject obj = new JSONObject();
            String token = new Date().getTime() + random(6);
            String sign = queryParams.get("header[sign]").toString();
            obj.put("token", token);
            obj.put("sign", sign);
            obj.put("brandId", employee.getBrandId());
            obj.put("userId", employee.getId());
            obj.put("telephone", employee.getTelephone());
            json.put("data", obj);

            SysLoginLog check = tokenService.queryToken(String.valueOf(employee.getId()));
            SysLoginLog sysLoginLog = new SysLoginLog();
            sysLoginLog.setUserId(employee.getId());
            sysLoginLog.setToken(token);
            sysLoginLog.setSign(sign);
            if (check != null) {
                tokenService.updateToken(sysLoginLog);
            } else {
                tokenService.insertToken(sysLoginLog);
            }

        }
        json.put("code", code);
        json.put("msg", message);
        ApiUtils.setBackInfo(response, json); // 返回信息设置


    }

    @RequestMapping(value = "/LoginOut", method = RequestMethod.POST)
    public void LoginOut(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "LoginOut");
        if (!result.isSuccess()) {
            return;
        }
        int delNum = tokenService.deleteToken(queryParams.get("header[userId]").toString());

        JSONObject json = new JSONObject();
        if(delNum == 0){
            json.put("code", DictConstants.FAIL);
            json.put("msg", "请求失败，暂无token");
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}
