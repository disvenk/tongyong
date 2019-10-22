package com.resto.geekpos.web.controller;

import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.model.QueueQrcode;
import com.resto.shop.web.service.QueueQrcodeService;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by carl on 2016/11/9.
 */
@Controller
@RequestMapping("qrcode")
public class QRcodeController extends GenericController {

    @Resource
    private QueueQrcodeService queueQrcodeService;

    @RequestMapping(value = "/lastCode", method = RequestMethod.POST)
    public void lastCode(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandId = queryParams.get("brandId").toString();
        DataSourceTarget.setDataSourceName(brandId);
        String shopDetailId = queryParams.get("shopDetailId").toString();
        QueueQrcode qrcode = queueQrcodeService.selectLastQRcode(shopDetailId);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("code", "200");
        json.put("msg", "请求成功");
        if(qrcode != null){

            json.put("data", new net.sf.json.JSONObject().fromObject(qrcode));
        } else {
            json.put("data", "");
        }
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

}
