package com.resto.wechat.web.controller;

import com.resto.brand.web.model.Vocation;
import com.resto.brand.web.service.VocationService;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by carl on 2016/12/28.
 */
@RestController
@RequestMapping("other")
public class OtherController extends GenericController {

    @Autowired
    VocationService vocationService;

    @RequestMapping("/vocationlist")
    public void vocationList(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        List<Vocation> list = vocationService.selectList();

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("success", true);
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}
