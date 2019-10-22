package com.resto.geekpos.web.controller;

import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.PosConfig;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.PosConfigService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.geekpos.web.constant.DictConstants;
import com.resto.geekpos.web.model.CheckResult;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.rpcinterceptors.DataSourceTarget;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.shop.web.model.WaitPicture;
import com.resto.shop.web.service.WaitPictureService;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by carl on 2016/9/5.
 */
@Controller
@RequestMapping("shop")
public class ShopController  extends GenericController{

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    NewAppraiseService newAppraiseService;

    @Resource
    BrandSettingService brandSettingService;

    @Resource
    PosConfigService posConfigService;

    @Resource
    WaitPictureService waitpictureService;

    @RequestMapping(value="/brandInfo",method = RequestMethod.POST)
    public void brandInfo(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandId = queryParams.get("brandId").toString();
        BrandSetting brandSetting = brandSettingService.selectByBrandId(brandId);

        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        json.put("data", new JSONObject(brandSetting));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value="/shopInfo",method = RequestMethod.POST)
    public void shopInfo(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String shopId = queryParams.get("shopId").toString();
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);

        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        json.put("data", new JSONObject(shopDetail));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value="/getShop",method = RequestMethod.POST)
    public void getShop(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        CheckResult result = check(request, response, "getShop");
        if (!result.isSuccess()) {
            return;
        }

        String brandId = queryParams.get("brandId").toString();
        List<ShopDetail> lists = shopDetailService.selectByBrandId(brandId);

        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        json.put("data", lists);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping(value="/shopDeatil",method = RequestMethod.POST)
    public void shopDeatil(HttpServletRequest request, HttpServletResponse response){
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String brandId = queryParams.get("brandId").toString();
        String shopDetailId = queryParams.get("shopDetailId").toString();
        DataSourceTarget.setDataSourceName(brandId);

        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopDetailId);
        List<Map<String, Object>> result = newAppraiseService.appraiseMonthCount(getCurrentBrandId(),shopDetailId);
        if(result.size() != 0 ){
            double res  = Double.parseDouble(result.get(0).get("AVG_SCORE").toString());
            DecimalFormat df = new DecimalFormat("#.00");
            shopDetail.setCountAppraise(Double.parseDouble(df.format(res)));
            shopDetail.setNumAppraise(Math.ceil(res/100*5));
        }

        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        json.put("data", new JSONObject(shopDetail));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/getServerIp")
    @ResponseBody
    public void getServerIp(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();
        PosConfig posConfig = posConfigService.getConfigByClientIp(getLocalIP());
        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        json.put("data", new JSONObject(posConfig));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    @RequestMapping("/editPosWaitred")
    public void editPosWaitred(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String shopId = queryParams.get("shopId").toString();
        String state = queryParams.get("state").toString();
        shopDetailService.updatePosWaitredEnvelope(shopId, Integer.parseInt(state));

        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/getWaitPictureList")
    public void getWaitPictureList(HttpServletRequest request, HttpServletResponse response) {
        MobilePackageBean mobile = AppUtils.unpack(request, response);
        Map queryParams = (Map) mobile.getContent();

        String shopId = queryParams.get("shopId").toString();
        List<WaitPicture> waitPictures = waitpictureService.getWaitPictureList(shopId);

        JSONObject json = new JSONObject();
        json.put("code", DictConstants.SUCCESS);
        json.put("msg", "请求成功");
        json.put("data", waitPictures);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    /**
     * 获取本机IP地址
     * 在服务器中，获取的为服务器公网IP
     * 在开发环境中，获取的为127.0.0.1
     *
     * @return
     */
    public static String getLocalIP() {
        String SERVER_IP = "";
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (SERVER_IP != null) {
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return SERVER_IP;
    }
}
