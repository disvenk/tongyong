package com.resto.pos.web.controller;

import com.alibaba.fastjson.JSON;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.pos.web.config.SessionKey;
import com.resto.pos.web.util.PropertyUtil;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.PlatformName;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.model.Printer;
import com.resto.shop.web.service.PrinterService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RequestMapping("/config")
@Controller
public class ConfigController extends GenericController {

    @Resource
    BrandSettingService brandSettingService;

    @Autowired
    BrandUserService brandUserService;

    @Autowired
    ShopDetailService shopDetailService;

    @Autowired
    WechatConfigService wechatConfigService;

    @Autowired
    PlatformService platformService;

    @Autowired
    PosConfigService posConfigService;

    @Autowired
    PosLoginConfigService posLoginConfigService;

    @Autowired
    PrinterService printerService;

    @Autowired
    ShopTvConfigService shopTvConfigService;

    @Autowired
    RedisService redisService;

    @Autowired
    WeChatService weChatService;

    @RequestMapping("/getBrandSetting")
    @ResponseBody
    public Result getBrandSetting() {
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        return getSuccessResult(brandSetting);
    }

    @RequestMapping("/getServerIp")
    @ResponseBody
    public Result getServerIp() {
        PosConfig posConfig = posConfigService.getConfigByClientIp(getLocalIP());
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, String> ybMap = PropertyUtil.getPropertyValue("ybpath");
            result.put("appKey", ybMap.get("appKey"));
            String requestURL = getRequest().getRequestURL().toString();
            String domain = requestURL.substring(requestURL.indexOf(".") + 1, requestURL.indexOf(".restoplus"));
            result.put("customerId", getCurrentShopId().concat("_POS_").concat(domain));
            result.put("publicTopic", ybMap.get("publicTopic"));
            result.put("serverInfo", posConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }
        log.info(JSON.toJSONString(result));
        return getSuccessResult(result);
    }


    @RequestMapping("/getBrandUser")
    @ResponseBody
    public Result getBrandUser(String password) {
        password = ApplicationUtils.pwd(password);
        BrandUser brandUser = brandUserService.selectByUsername(getCurrentBrandUser().getUsername());
        return new Result(password.equals(brandUser.getSuperPwd()));
    }

    @RequestMapping("/checkMealFee")
    @ResponseBody
    public Result checkMealFee() {
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if (brandSetting.getIsMealFee() == SessionKey.OPEN) { //如果餐盒费是开启的情况
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
            if (shopDetail.getIsMealFee() == SessionKey.OPEN) { //如果店铺开启了就餐费
                return getSuccessResult(shopDetail);
            } else {
                return new Result(false);
            }
        } else {
            return new Result(false);
        }
    }

    @RequestMapping("/getShopInfo")
    @ResponseBody
    public Result getShopInfo() {
        ShopDetail shopDetail = shopDetailService.selectById(getCurrentShopId());
        return getSuccessResult(shopDetail);
    }


    @RequestMapping("/saveLog")
    public Result saveLog(Boolean result, String model) {
        JSONObject json = new JSONObject(model);
//		UserActionUtils.writeToFtp(LogType.POS_LOG, getCurrentBrand().getBrandName(),
//				getCurrentShop().getName(),
//				"开始打印订单模版:"+json.toString() + ",打印返回结果:"+result);
        log.info("开始打印订单模版:" + json.toString() + ",打印返回结果:" + result);
        return new Result(true);
    }


    @RequestMapping("/saveError")
    public Result saveError(String id) {

        List<String> errorList = (List<String>) redisService.get(getCurrentShopId() + "printError");
        if (errorList == null) {
            errorList = new ArrayList<>();
        }
//        Map<String, Object> print = (Map<String, Object>) redisService.get(id);
        // 遍历jsonObject数据，添加到Map对象
        if (!errorList.contains(id)) {
            errorList.add(id);
        }

        Integer count = (Integer) redisService.get(id + "errorCount");
        if (count == null) {
            count = 4;
        }
        count = count + 1;
        redisService.set(id + "errorCount", count);


        redisService.set(getCurrentShopId() + "printError", errorList);
        if (count >= 5 && count % 5 == 0 && count <= 25) {
            WechatConfig config = wechatConfigService.selectByBrandId(getCurrentBrandId());
            String name = (String) redisService.get(id + "article");
            Customer customer = (Customer) redisService.get(id + "customer");
            weChatService.sendCustomerMsg("如下菜品：" + name + "下单异常，请速与服务员联系", customer.getWechatId(), config.getAppid(), config.getAppsecret());//提交推送
        }

        return new Result(true);
    }


    @RequestMapping("/checkError")
    @ResponseBody
    public Result checkError() {

        List<String> errorList = (List<String>) redisService.get(getCurrentShopId() + "printError");
        List<Map<String, Object>> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(errorList)) {
            for (String id : errorList) {
                Map<String, Object> print = (Map<String, Object>) redisService.get(id);
                result.add(print);
            }
        }
        redisService.remove(getCurrentShopId() + "printError");
        return getSuccessResult(result);
    }

    @RequestMapping("/checkPlatformSetting")
    @ResponseBody
    public Result checkPlatformSetting() {
        List<Platform> platformList = platformService.selectByBrandId(getCurrentBrandId());

        Boolean check = false;
        for (Platform platform : platformList) {
            if (platform.getName().equals(PlatformName.MEITUAN)) {
                if (getCurrentShop().getPrintMeituan() == Common.YES) {
                    check = true;
                }
                break;
            }
        }
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if (brandSetting.getOpenShanhuiPay() == Common.YES &&
                getCurrentShop().getPrintMeituan() == Common.YES) {
            check = true;
        }

        if (check) {
            return new Result(true);
        } else {
            return new Result(false);
        }
    }

    @RequestMapping("/loginConfig")
    @ResponseBody
    public Result loginConfig(HttpServletRequest request) {
        PosLoginConfig posLoginConfig = posLoginConfigService.getConfigByIp(getIpAddr(request));
        if (posLoginConfig == null) {
            return new Result(false);
        } else {
            return getSuccessResult(posLoginConfig);
        }

    }


    @RequestMapping("/loginOut")
    public ModelAndView loginOut(HttpSession session) {
        return new ModelAndView("login");
    }

    @RequestMapping("/printList")
    @ResponseBody
    public Result printList() {
        List<Printer> result = printerService.selectListNotSame(getCurrentShopId());
        for(Printer printer : result){
            printer.setTaskList(new ArrayList<Map<String, Object>>());
        }
        return getSuccessResult(result);
    }

    @RequestMapping("/getShopTvConfig")
    @ResponseBody
    public Result getShopTvConfig() {
        ShopTvConfig shopTvConfig = shopTvConfigService.selectByShopId(getCurrentShopId());
        return getSuccessResult(shopTvConfig);
    }
}
