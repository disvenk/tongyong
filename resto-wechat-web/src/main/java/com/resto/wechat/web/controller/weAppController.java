package com.resto.wechat.web.controller;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WeBrand;
import com.resto.brand.web.model.WeBrandScore;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.WeBrandService;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.WeOrderDetailService;
import com.resto.shop.web.service.WeShopService;
import com.resto.wechat.web.rpcinterceptors.DataSourceTarget;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yz on 2017-01-17.
 */
@RestController
@RequestMapping("weApp")
public class weAppController extends GenericController {

    @Resource
    private WeBrandService weBrandService;

    @Resource
    private WeShopService weShopService;


    @Resource
    private WeOrderDetailService weOrderDetailService;

    @Resource
    private ShopDetailService shopDetailService;


    @RequestMapping("/getBrandDataList")
    public Result getBrandDataList(String createTime) {

        //查询所有品牌的分数
        List<WeBrand> weBrandList = weBrandService.selectWeBrandList(createTime);

        List<Map<String, Object>> list = new ArrayList<>();
        if (!weBrandList.isEmpty()) {
            int i = 1;
            for (WeBrand weBrand : weBrandList) {
                //封装小程序需要的数据结果
                Map<String, Object> map = new HashMap<>();
                map.put("index", i);
                i++;
                map.put("brand", weBrand.getBrandName());
                map.put("brandId",weBrand.getBrandId());
                if (!weBrand.getWeBrandScores().isEmpty()) {
                    for (WeBrandScore weBrandScore : weBrand.getWeBrandScores()) {
                        if (weBrandScore.getFlag()) {
                            map.put("brandimg", "/images/arrow_up.png");
                        } else {
                            map.put("brandimg", "/images/arrow_down.png");
                        }
                        map.put("score", weBrandScore.getBrandScore());
                    }
                }
                list.add(map);
            }
        }
        return getSuccessResult(list);
    }

    /**
     * 查看某品牌下店铺的分数
     *
     * @param brandId    当前品牌的id
     * @param createTime 查询的时间
     * @return
     */
    @RequestMapping("/getShopDataList")
    public Result getShopDataList(String brandId, String createTime) {
        //判断当前的session中是否有当前brandId的标识

//        if (null == getSession().getAttribute(brandId)) {
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMessage("无权查看或到了失效时间");
//            return result;
//
//        }

        //根据点击用户来定位数据库
        DataSourceTarget.setDataSourceName(brandId);

        List<WeShop> weShopList = weShopService.selectWeShopListByBrandIdAndTime(brandId, createTime);

        List<Map<String, Object>> recordList = new ArrayList<>();
        if (!weShopList.isEmpty()) {
            int i = 1;
            for (WeShop weShop : weShopList) {
                //封装微信端需要的店铺的数据
                Map<String, Object> map = new HashMap<>();
                map.put("index", i);
                i++;
                map.put("brand", weShop.getShopName());
                if (!weShop.getWeShopScoreList().isEmpty()) {
                    for (WeShopScore weShopScore : weShop.getWeShopScoreList()) {
                        if (weShopScore.getScoreFlag()) {
                            map.put("brandimg", "/images/arrow_up.png");
                        } else {
                            map.put("brandimg", "/images/arrow_down.png");
                        }

                        if (weShopScore.getTotalFlag()) {
                            map.put("scoreimg", "/images/arrow_up.png");
                        } else {
                            map.put("scoreimg", "/images/arrow_down.png");
                        }
                        map.put("shopId",weShop.getShopId());
                        map.put("brandId",weShop.getBrandId());
                        map.put("score", weShopScore.getShopScore());
                        map.put("amount", weShopScore.getTotalIncome());
                    }
                    recordList.add(map);
                }

            }

        }
        return getSuccessResult(recordList);

    }

    @RequestMapping("/getCode")
    public Result getCode(String telephone, HttpSession httpSession, String brandId) {

        Result r = new Result(false);
        if (StringUtils.isBlank(telephone)) {
            r.setMessage("请输入手机号码！");
            return r;
        }
        //判断是否是可用手机号码
        List<ShopDetail> shopDetailList = shopDetailService.selectByBrandId(brandId);
        Boolean tag = false;
        for (ShopDetail s : shopDetailList) {
            if (s.getIsOpenSms() == 1 && s.getNoticeTelephone().contains(telephone)) {
                tag = true;
                break;
            }
        }
        if (!tag) {
            r.setMessage("不是该品牌的管理员号码");
            return r;
        }

        String code = RandomStringUtils.randomNumeric(4);

        //com.alibaba.fastjson.JSONObject smsResult = SMSUtils.sendCode("餐加",  code, telephone,null);
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		jsonObject.put("code",code);
		jsonObject.put("product","餐加");
		com.alibaba.fastjson.JSONObject smsResult = SMSUtils.sendMessage(telephone,jsonObject,SMSUtils.SIGN,SMSUtils.CODE_SMS_TEMP);
        if (!smsResult.getBoolean("success")) {
            r.setMessage(smsResult.getString("msg"));
            log.error("发送短信失败:" + smsResult);
            return r;
        }
        httpSession.setAttribute("phone-code-" + telephone, code);
        log.info("发送短信成功:" + code);
        return new Result(true);
    }

    //验证身份
//        URL http:// 192.168.1.119:8088/wechat/weApp/checkTelephone
//    参数     类型   备注
//    phone   String
//    vercode  String

    @RequestMapping("/getCheckTelephone")
    public Result checkTelephone(String telephone, HttpSession session, String vercode) {
        String vailCode = (String) session.getAttribute("phone-code-" + telephone);
        Result result = new Result();
        if (vailCode == null || !vailCode.equals(vercode)) {
            result.setSuccess(false);
            result.setMessage("验证码错误!");
        } else {
            result.setSuccess(true);
        }
        return result;

    }

    @RequestMapping("/getShopDataInfo")
    public Result getShopDataInfo(String shopId, String brandId, String createTime) {

        //根据点击用户来定位数据库
        DataSourceTarget.setDataSourceName(brandId);
        WeShop weShop = weShopService.selectWeShopByShopIdAndTime(shopId, createTime);
        WeOrderDetail ws = weOrderDetailService.selectWeOrderByShopIdAndTime(shopId, createTime);
        Map<String, Object> map = new HashMap<>();

        if (null != ws) {

            Map<String, Object> shopInfoMap = new HashMap<>();
            shopInfoMap.put("restaurantName", weShop.getShopName());
            shopInfoMap.put("data", createTime);
            if (!weShop.getWeShopScoreList().isEmpty()) {
                for (WeShopScore weShopScore : weShop.getWeShopScoreList()) {//实际只有一条数据
                    shopInfoMap.put("score", weShopScore.getShopScore());
                }
            }
            map.put("restaurantInfo", shopInfoMap);//第一个数据


            //实收金额  1.微信支付+支付宝+现金+刷卡+充值
            BigDecimal totalpayIncome = ws.getWechatTotal().add(ws.getAlipayTotal()).add(ws.getCashTotal()).add(ws.getBankTotal()).add(ws.getChargeTotal());
            //实收笔数 1.微信+支付宝+现金+刷卡+充值
            int sumPayCount = ws.getWechatCount() + ws.getAlipayCount() + ws.getCashCount() + ws.getBankCount() + ws.getChargeCount();

            //订单总额  resto订单总额 + 线下订单总额
            BigDecimal orderIncome = ws.getEnterTotal().add(ws.getRestoTotal());//订单总额

            //营业总额 订单总额+充值
            BigDecimal totalIncome = BigDecimal.ZERO;
            totalIncome = orderIncome.add(ws.getChargeTotal());

            //订单总数
            int totalCount = ws.getEnterCount() + ws.getRestoCount();
            //订单平均金额
            BigDecimal avgIncome = BigDecimal.ZERO;
            if (totalCount != 0) {
                avgIncome = orderIncome.divide(new BigDecimal(totalCount), 2, BigDecimal.ROUND_HALF_UP);
            }

            //总折扣金额 红包支付+优惠券+充值赠送+等位红包+退菜红包
            BigDecimal offorderIncome = ws.getRedTotal().add(ws.getCouponTotal()).add(ws.getChargeReturnTotal()).add(ws.getWaitTotal()).add(ws.getReturnTotal());
            //系统折扣比率
            String offPrecnet = String.valueOf(offorderIncome.divide(new BigDecimal(totalCount), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));

            List<Map<String, Object>> contentList = new ArrayList<>();

            //1订单数据
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("resId", 1);
            orderMap.put("resItemTitle", "订单数据");
            orderMap.put("resItemDetailFlag", "项目");
            orderMap.put("resItemDetailFlag", "数值");
            List<Map<String, Object>> orderList = new ArrayList<>();
            Map<String, Object> orderListMap = new HashMap<>();
            orderListMap.put("index", 1);
            orderListMap.put("resItemDetailCon", "订单总额总额（线上+线下)");
            orderListMap.put("resItemDetailConFlag", orderIncome);//订单总额
            Map<String, Object> orderListMap2 = new HashMap<>();
            orderListMap2.put("index", 2);
            orderListMap2.put("resItemDetailCon", "订单总数(线上+线下)");
            orderListMap2.put("resItemDetailConFlag", totalCount);
            Map<String, Object> orderListMap3 = new HashMap<>();
            orderListMap3.put("index", 3);
            orderListMap3.put("resItemDetailCon", "订单总额均额");
            orderListMap3.put("resItemDetailConFlag", avgIncome);
            orderList.add(orderListMap);
            orderList.add(orderListMap2);
            orderList.add(orderListMap3);
            orderMap.put("resItemDetail", orderList);
            contentList.add(orderMap);

            //2来客人数
            Map<String, Object> customerMap = new HashMap<>();
            customerMap.put("resId", 2);
            customerMap.put("resItemTitle", "来客人数");
            customerMap.put("resItemDetailFlag", "项目");
            customerMap.put("resItemDetailFlag", "数值");

            List<Map<String, Object>> customerMaplist = new ArrayList<>();
            Map<String, Object> customerListMap = new HashMap<>();
            customerListMap.put("index", 1);
            customerListMap.put("resItemDetailCon", "就餐人数");
            customerListMap.put("resItemDetailConFlag", ws.getCustomerCount());

            Map<String, Object> customerListMap2 = new HashMap<>();
            customerListMap2.put("index", 2);
            customerListMap2.put("resItemDetailCon", "人均");
            customerListMap2.put("resItemDetailConFlag", ws.getAvgCustomerTotal());
            customerMaplist.add(customerListMap);
            customerMaplist.add(customerListMap2);
            customerMap.put("resItemDetail", customerMaplist);
            contentList.add(customerMap);

            Map<String, Object> payMap = new HashMap<>();
            payMap.put("resId", 3);
            payMap.put("resItemTitle", "Resto+系统实收金额(¥：)" + totalpayIncome + "/" + orderIncome + getPerent(totalpayIncome, orderIncome));
            payMap.put("resItemDetailFlag", "项目");
            payMap.put("resItemDetailFlag", "数值（交易笔数/总金额）");

            List<Map<String, Object>> payMaplist = new ArrayList<>();
            Map<String, Object> payListMap = new HashMap<>();
            payListMap.put("index", 1);
            payListMap.put("resItemDetailCon", "微信笔数/支付");

            payListMap.put("resItemDetailConFlag", ws.getWechatCount() + "笔 / ¥：" + ws.getWechatTotal());

            Map<String, Object> payListMap2 = new HashMap<>();
            payListMap2.put("index", 2);
            payListMap2.put("resItemDetailCon", "支付宝笔数/支付");
            payListMap2.put("resItemDetailConFlag", ws.getAlipayCount() + "笔 / ¥：" + ws.getAlipayTotal());

            Map<String, Object> payListMap3 = new HashMap<>();
            payListMap3.put("index", 3);
            payListMap3.put("resItemDetailCon", "现金笔数/支付");
            payListMap3.put("resItemDetailConFlag", ws.getCashCount() + "笔 / ¥：" + ws.getCashTotal());

            Map<String, Object> payListMap4 = new HashMap<>();
            payListMap4.put("index", 4);
            payListMap4.put("resItemDetailCon", "刷卡笔数/支付");
            payListMap4.put("resItemDetailConFlag", ws.getBankCount() + "笔 / ¥：" + ws.getBankTotal());

            Map<String, Object> payListMap5 = new HashMap<>();
            payListMap5.put("index", 5);
            payListMap5.put("resItemDetailCon", "充值笔数/支付");
            payListMap5.put("resItemDetailConFlag", ws.getChargeCount() + "笔 / ¥：" + ws.getChargeTotal());

            payMaplist.add(customerListMap);
            payMaplist.add(customerListMap2);
            payMap.put("resItemDetail", customerMaplist);
            contentList.add(payMap);


            //4折扣金额
            Map<String, Object> discountMap = new HashMap<>();
            discountMap.put("resId", 4);
            discountMap.put("resItemTitle", "Resto+系统折扣金额（¥：" + offorderIncome + "/" + orderIncome + "|" + offPrecnet + "%");
            discountMap.put("resItemDetailFlag", "项目");
            discountMap.put("resItemDetailFlag", "数值（交易笔数/总金额）");

            List<Map<String, Object>> discountMaplist = new ArrayList<>();
            Map<String, Object> discountListMap = new HashMap<>();
            ;
            discountListMap.put("index", 1);
            discountListMap.put("resItemDetailCon", "红包个数/支付");
            discountListMap.put("resItemDetailConFlag", ws.getRedCount() + "笔 / ¥：" + ws.getRedTotal());

            Map<String, Object> discountListMap2 = new HashMap<>();
            ;
            discountListMap2.put("index", 2);
            discountListMap2.put("resItemDetailCon", "优惠券数/支付");
            discountListMap2.put("resItemDetailConFlag", ws.getCouponCount() + "张 / ¥：" + ws.getCouponTotal());

            Map<String, Object> discountListMap3 = new HashMap<>();
            ;
            discountListMap3.put("index", 3);
            discountListMap3.put("resItemDetailCon", "充值赠送数/支付");
            discountListMap3.put("resItemDetailConFlag", ws.getChargeReturnCount() + "笔 / ¥：" + ws.getChargeReturnTotal());

            Map<String, Object> discountListMap4 = new HashMap<>();
            ;
            discountListMap4.put("index", 4);
            discountListMap4.put("resItemDetailCon", "等位红包数/支付");
            discountListMap4.put("resItemDetailConFlag", ws.getWaitCount() + "个 / ¥：" + ws.getWaitTotal());

            Map<String, Object> discountListMap5 = new HashMap<>();
            ;
            discountListMap5.put("index", 5);
            discountListMap5.put("resItemDetailCon", "退菜红包数/支付");
            discountListMap5.put("resItemDetailConFlag", ws.getReturnCount() + "个 / ¥：" + ws.getWaitTotal());

            discountMaplist.add(customerListMap);
            discountMaplist.add(customerListMap2);
            discountMap.put("resItemDetail", customerMaplist);
            contentList.add(discountMap);


            //5充值详情
            if (!ws.getWeChargeLogList().isEmpty()) {
                Map<String, Object> chargeSellMap = new HashMap<>();
                ;
                chargeSellMap.put("resId", 5);
                chargeSellMap.put("resItemTitle", "充值详情（¥：" + ws.getChargeTotal() + "/" + ws.getChargeCount() + "个");
                chargeSellMap.put("resItemDetailFlag", "项目");
                chargeSellMap.put("resItemDetailFlag", "数值");

                List<Map<String, Object>> chargeSellMaplist = new ArrayList<>();
                for (int i = 0; i < ws.getWeChargeLogList().size(); i++) {
                    Map<String, Object> chargeSellListMap = new HashMap<>();
                    ;
                    chargeSellListMap.put("index", i + 1);
                    if (ws.getWeChargeLogList().get(i).getChargeType() == 0) {//0wechat 1 pos
                        chargeSellListMap.put("resItemDetailCon", ws.getWeChargeLogList().get(i).getChargeTelephone() + "(wechat)");
                    } else {
                        chargeSellListMap.put("resItemDetailCon", ws.getWeChargeLogList().get(i).getChargeTelephone() + "(pos)");
                    }

                    chargeSellListMap.put("resItemDetailConFlag", "￥" + ws.getWeChargeLogList().get(i).getChargeMoney());
                    chargeSellMaplist.add(chargeSellListMap);
                }

                chargeSellMap.put("resItemDetail", chargeSellMaplist);
                contentList.add(chargeSellMap);

            }

            //6菜品销量
            Map<String, Object> articleSellMap = new HashMap<>();
            ;
            articleSellMap.put("resId", 6);
            articleSellMap.put("resItemTitle", "菜品销量（¥" + ws.getOrderItemTotal() + "/" + ws.getOrderItemCount() + "份");
            articleSellMap.put("resItemDetailFlag", "项目");
            articleSellMap.put("resItemDetailFlag", "数值（销量/营收）");

            List<Map<String, Object>> articleSellMaplist = new ArrayList<>();
            int wi = 0;
            if (!ws.getWeItemList().isEmpty()) {
                for (WeItem weItem : ws.getWeItemList()) {
                    wi++;
                    Map<String, Object> articleSellListMap = new HashMap<>();
                    ;
                    articleSellListMap.put("index", wi);
                    articleSellListMap.put("resItemDetailCon", weItem.getItemName());
                    articleSellListMap.put("resItemDetailConFlag", weItem.getItemCount() + "份/¥：" + weItem.getItemTotal());
                    articleSellMaplist.add(articleSellListMap);
                }
            }

            articleSellMap.put("resItemDetail", articleSellMaplist);
            contentList.add(articleSellMap);

            //7退菜统计
            Map<String, Object> returnMap = new HashMap<>();
            ;
            returnMap.put("resId", 7);
            returnMap.put("resItemTitle", "退菜统计（¥" + ws.getReturnItemTotal() + "/" + ws.getReturnItemCount() + "份");
            returnMap.put("resItemDetailFlag", "项目");
            returnMap.put("resItemDetailFlag", "数值（数量/实退）");

            List<Map<String, Object>> returnMaplist = new ArrayList<>();
            if (!ws.getWeReturnItemList().isEmpty()) {
                int wm = 0;
                for (WeReturnItem weReturnItem : ws.getWeReturnItemList()) {
                    wm++;
                    Map<String, Object> returnListMap = new HashMap<>();
                    ;
                    returnListMap.put("index", wm);
                    returnListMap.put("resItemDetailCon", weReturnItem.getReturnItemName());
                    returnListMap.put("resItemDetailConFlag", weReturnItem.getReturnItemCount() + "份/¥：" + weReturnItem.getReturnItemTotal());
                    returnMaplist.add(returnListMap);
                }

            }
            returnMap.put("resItemDetail", returnMaplist);
            contentList.add(returnMap);

            //8退菜用户
            Map<String, Object> returnCustomerMap = new HashMap<>();
            returnCustomerMap.put("resId", 8);
            returnCustomerMap.put("resItemTitle", "退菜用户（¥" + ws.getReturnCustomerTotal() + "/" + ws.getReturnCustomerCount() + "位");
            returnCustomerMap.put("resItemDetailFlag", "项目");
            returnCustomerMap.put("resItemDetailFlag", "数值");

            List<Map<String, Object>> returnCustomerMaplist = new ArrayList<>();
            if (!ws.getWeReturnCustomerList().isEmpty()) {
                int wl = 0;
                for (WeReturnCustomer weReturnCustomer : ws.getWeReturnCustomerList()) {
                    wl++;
                    Map<String, Object> returnCustomerListMap = new HashMap<>();
                    returnCustomerListMap.put("index", wl);
                    returnCustomerListMap.put("resItemDetailCon", weReturnCustomer.getTelephone());
                    returnCustomerListMap.put("resItemDetailConFlag", "¥：" + weReturnCustomer.getMoney());
                    returnCustomerMaplist.add(returnCustomerListMap);
                }
            }
            returnCustomerMap.put("resItemDetail", returnCustomerMaplist);
            contentList.add(returnCustomerMap);


            //9新增用户
            Map<String, Object> newCustomerMap = new HashMap<>();
            newCustomerMap.put("resId", 9);
            newCustomerMap.put("resItemTitle", "Resto+新增用户（¥" + ws.getNewCustomerTotal() + "/" + ws.getNewCustomerCount() + "/" + getPerent(ws.getNewCustomerTotal(), orderIncome));
            newCustomerMap.put("resItemDetailFlag", "项目");
            newCustomerMap.put("resItemDetailFlag", "数值（位数/营收)");

            List<Map<String, Object>> newCustomerMaplist = new ArrayList<>();
            Map<String, Object> newCustomerListMap = new HashMap<>();
            newCustomerListMap.put("index", 1);
            newCustomerListMap.put("resItemDetailCon", "自然到店用户消费");
            newCustomerListMap.put("resItemDetailConFlag", ws.getNewNormalCustomerCount() + "位 / ¥：" + ws.getNewNormalCustomerTotal());

            Map<String, Object> newCustomerListMap2 = new HashMap<>();
            newCustomerListMap2.put("index", 2);
            newCustomerListMap2.put("resItemDetailCon", "分享到店用户消费");
            newCustomerListMap2.put("resItemDetailConFlag", ws.getNewShareCustomerCount() + "位 / ¥：" + ws.getNewShareCustomerTotal());

            newCustomerMaplist.add(customerListMap);
            newCustomerMaplist.add(customerListMap2);
            newCustomerMap.put("resItemDetail", customerMaplist);
            contentList.add(newCustomerMap);

            //10回头用户
            Map<String, Object> backCustomerMap = new HashMap<>();
            backCustomerMap.put("resId", 10);
            backCustomerMap.put("resItemTitle", "Resto+回头用户（¥" + ws.getBackCustomerToatal() + "/" + ws.getBackCustomerCount() + "位/" + getPerent(ws.getBackCustomerToatal(), orderIncome));
            backCustomerMap.put("resItemDetailFlag", "项目");
            backCustomerMap.put("resItemDetailFlag", "数值（位数/营收)");

            List<Map<String, Object>> backCustomerMaplist = new ArrayList<>();
            Map<String, Object> backCustomerListMap = new HashMap<>();
            ;
            backCustomerListMap.put("index", 1);
            backCustomerListMap.put("resItemDetailCon", "二次回头用户");
            backCustomerListMap.put("resItemDetailConFlag", ws.getBackTwoCustomerCount() + "位 / ¥：" + ws.getBackTwoCustomerTotal());

            Map<String, Object> backCustomerListMap2 = new HashMap<>();
            backCustomerListMap2.put("index", 2);
            backCustomerListMap2.put("resItemDetailCon", "三次及以上回头用户");
            backCustomerListMap2.put("resItemDetailConFlag", ws.getBackTwoMoreCustomerCount() + "位 / ¥：" + ws.getBackTwoMoreCustomerTotal());

            backCustomerMaplist.add(customerListMap);
            backCustomerMaplist.add(customerListMap2);
            backCustomerMap.put("resItemDetail", customerMaplist);
            contentList.add(backCustomerMap);


            //11上旬用户数据
            Map<String, Object> s_customerMap = new HashMap<>();
            s_customerMap.put("resId", 11);
            s_customerMap.put("resItemTitle", "Resto+上旬用户的数据");
            s_customerMap.put("resItemDetailFlag", "项目");
            s_customerMap.put("resItemDetailFlag", "数值");

            List<Map<String, Object>> s_customerMaplist = new ArrayList<>();
            Map<String, Object> s_customerListMap = new HashMap<>();
            s_customerListMap.put("index", 1);
            s_customerListMap.put("resItemDetailCon", "上旬平均分");
            s_customerListMap.put("resItemDetailConFlag", ws.getsAvgScore());

            Map<String, Object> s_customerListMap2 = new HashMap<>();
            s_customerListMap2.put("index", 2);
            s_customerListMap2.put("resItemDetailCon", "R+订单总额/占比");
            s_customerListMap2.put("resItemDetailConFlag", "￥：" + ws.getsAllTotal() + "/" + getPerent(ws.getsAllTotal(), ws.getsTotalIncome()));

            Map<String, Object> s_customerListMap3 = new HashMap<>();
            s_customerListMap3.put("index", 3);
            s_customerListMap3.put("resItemDetailCon", "在线支付总额占比");
            s_customerListMap3.put("resItemDetailConFlag", "￥：" + ws.getsPayTotal() + "/" + getPerent(ws.getsPayTotal(), ws.getsAllTotal()));


            Map<String, Object> s_customerListMap4 = new HashMap<>();
            s_customerListMap4.put("index", 4);
            s_customerListMap4.put("resItemDetailCon", "新增/营收/占比");
            s_customerListMap4.put("resItemDetailConFlag", ws.getsNewCustomr() + "位/￥：" + ws.getsNewCustomerTotal() + "/" + getPerent(ws.getsNewCustomerTotal(), ws.getsRestoTotal()));

            Map<String, Object> s_customerListMap5 = new HashMap<>();
            s_customerListMap5.put("index", 5);
            s_customerListMap5.put("resItemDetailCon", "自然新增数据");
            s_customerListMap5.put("resItemDetailConFlag", ws.getsNewNormalCustomer() + "位/￥：" + ws.getsNewNormalCustomerTotal() + "/" + getPerent(ws.getsNewNormalCustomerTotal(), ws.getsRestoTotal()));


            Map<String, Object> s_customerListMap6 = new HashMap<>();
            s_customerListMap6.put("index", 6);
            s_customerListMap6.put("resItemDetailCon", "分享新增数据");
            s_customerListMap6.put("resItemDetailConFlag", ws.getsNewShareCustomer() + "位/￥：" + ws.getsNewShareCustomer() + "/" + getPerent(ws.getsNewShareCustomerTotal(), ws.getsRestoTotal()));

            Map<String, Object> s_customerListMap7 = new HashMap<>();
            s_customerListMap7.put("index", 7);
            s_customerListMap7.put("resItemDetailCon", "回头营收占比");
            s_customerListMap7.put("resItemDetailConFlag", ws.getsBackCustomer() + "位/￥：" + ws.getsBackCustomerTotal() + "/" + getPerent(ws.getsBackCustomerTotal(), ws.getsRestoTotal()));

            Map<String, Object> s_customerListMap8 = new HashMap<>();
            s_customerListMap8.put("index", 8);
            s_customerListMap8.put("resItemDetailCon", "二次回头数据");
            s_customerListMap8.put("resItemDetailConFlag", ws.getsBackTwoCustomer() + "位/￥：" + ws.getsBackTwoCustomerTotal() + "/" + getPerent(ws.getsBackTwoCustomerTotal(), ws.getsRestoTotal()));

            Map<String, Object> s_customerListMap9 = new HashMap<>();
            s_customerListMap9.put("index", 9);
            s_customerListMap9.put("resItemDetailCon", "多次回头数据");
            s_customerListMap9.put("resItemDetailConFlag", ws.getsBackTwoMoreCustomer() + "位/￥：" + ws.getsBackTwoMoreCustomerTotal() + "/" + getPerent(ws.getsBackTwoMoreCustomerTotal(), ws.getsRestoTotal()));

            s_customerMaplist.add(s_customerListMap);
            s_customerMaplist.add(s_customerListMap2);
            s_customerMaplist.add(s_customerListMap3);
            s_customerMaplist.add(s_customerListMap4);
            s_customerMaplist.add(s_customerListMap5);
            s_customerMaplist.add(s_customerListMap6);
            s_customerMaplist.add(s_customerListMap7);
            s_customerMaplist.add(s_customerListMap8);
            s_customerMaplist.add(s_customerListMap9);
            s_customerMap.put("resItemDetail", s_customerMaplist);
            contentList.add(s_customerMap);

            //判断当前日期是上旬 中旬 还是下旬
            int date = DateUtil.getEarlyMidLate(DateUtil.fomatDate(createTime));
            int a = 12;//中旬默认序号
            int b = 13;//下旬默认序号
            int c = 14;//本月默认序号
            switch (date) {
                case 1: //上旬
                    c = 12;
                    break;
                case 2://中询
                    a = 12;
                    c = 13;
                    break;
                default:
                    break;
            }

            //12中旬用户数据
            Map<String, Object> z_customerMap = new HashMap<>();
            z_customerMap.put("resId", a);
            z_customerMap.put("resItemTitle", "Resto+中旬用户的数据");
            z_customerMap.put("resItemDetailFlag", "项目");
            z_customerMap.put("resItemDetailFlag", "数值");

            List<Map<String, Object>> z_customerMaplist = new ArrayList<>();
            Map<String, Object> z_customerListMap = new HashMap<>();
            z_customerListMap.put("index", 1);
            z_customerListMap.put("resItemDetailCon", "中旬平均分");
            z_customerListMap.put("resItemDetailConFlag", ws.getzAvgScore());

            Map<String, Object> z_customerListMap2 = new HashMap<>();
            z_customerListMap2.put("index", 2);
            z_customerListMap2.put("resItemDetailCon", "R+订单总额/占比");
            z_customerListMap2.put("resItemDetailConFlag", ws.getzAllTotal() + "/" + getPerent(ws.getzAllTotal(), ws.getzTotalIncome()));

            Map<String, Object> z_customerListMap3 = new HashMap<>();
            z_customerListMap3.put("index", 3);
            z_customerListMap3.put("resItemDetailCon", "在线支付总额占比");
            z_customerListMap3.put("resItemDetailConFlag", "￥：" + ws.getzPayTotal() + "/" + getPerent(ws.getzPayTotal(), ws.getzAllTotal()));


            Map<String, Object> z_customerListMap4 = new HashMap<>();
            z_customerListMap4.put("index", 4);
            z_customerListMap4.put("resItemDetailCon", "新增/营收/占比");
            z_customerListMap4.put("resItemDetailConFlag", ws.getzNewCustomerCount() + "位/￥：" + ws.getzNewCustomerTotal() + "/" + getPerent(ws.getzNewCustomerTotal(), ws.getzRestoTotal()));

            Map<String, Object> z_customerListMap5 = new HashMap<>();
            z_customerListMap5.put("index", 5);
            z_customerListMap5.put("resItemDetailCon", "自然新增数据");
            z_customerListMap5.put("resItemDetailConFlag", ws.getzNewNormalCustomer() + "位/￥：" + ws.getzNewNormalCustomerTotal() + "/" + getPerent(ws.getzNewNormalCustomerTotal(), ws.getzRestoTotal()));


            Map<String, Object> z_customerListMap6 = new HashMap<>();
            z_customerListMap6.put("index", 6);
            z_customerListMap6.put("resItemDetailCon", "分享新增数据");
            z_customerListMap6.put("resItemDetailConFlag", ws.getzNewShareCustomer() + "位/￥：" + ws.getzNewShareCustomer() + "/" + getPerent(ws.getzNewShareCustomerTotal(), ws.getzRestoTotal()));

            Map<String, Object> z_customerListMap7 = new HashMap<>();
            z_customerListMap7.put("index", 7);
            z_customerListMap7.put("resItemDetailCon", "回头营收占比");
            z_customerListMap7.put("resItemDetailConFlag", ws.getzBackCustomer() + "位/￥：" + ws.getzBackCustomerTotal() + "/" + getPerent(ws.getzBackCustomerTotal(), ws.getzRestoTotal()));

            Map<String, Object> z_customerListMap8 = new HashMap<>();
            z_customerListMap8.put("index", 8);
            z_customerListMap8.put("resItemDetailCon", "二次回头数据");
            z_customerListMap8.put("resItemDetailConFlag", ws.getzBackTwoCustomer() + "位/￥：" + ws.getzBackTwoCustomerTotal() + "/" + getPerent(ws.getzBackTwoCustomerTotal(), ws.getzRestoTotal()));

            Map<String, Object> z_customerListMap9 = new HashMap<>();
            z_customerListMap9.put("index", 9);
            z_customerListMap9.put("resItemDetailCon", "多次回头数据");
            z_customerListMap9.put("resItemDetailConFlag", ws.getzBackTwoMoreCustomer() + "位/￥：" + ws.getzBackTwoMoreCustomerTotal() + "/" + getPerent(ws.getzBackTwoMoreCustomerTotal(), ws.getzRestoTotal()));

            z_customerMaplist.add(z_customerListMap);
            z_customerMaplist.add(z_customerListMap2);
            z_customerMaplist.add(z_customerListMap3);
            z_customerMaplist.add(z_customerListMap4);
            z_customerMaplist.add(z_customerListMap5);
            z_customerMaplist.add(z_customerListMap6);
            z_customerMaplist.add(z_customerListMap7);
            z_customerMaplist.add(z_customerListMap8);
            z_customerMaplist.add(z_customerListMap9);
            z_customerMap.put("resItemDetail", z_customerMaplist);


            //13下旬用户数据
            Map<String, Object> x_customerMap = new HashMap<>();
            x_customerMap.put("resId", b);
            x_customerMap.put("resItemTitle", "Resto+下旬用户的数据");
            x_customerMap.put("resItemDetailFlag", "项目");
            x_customerMap.put("resItemDetailFlag", "数值");

            List<Map<String, Object>> x_customerMaplist = new ArrayList<>();
            Map<String, Object> x_customerListMap = new HashMap<>();
            x_customerListMap.put("index", 1);
            x_customerListMap.put("resItemDetailCon", "下旬平均分");
            x_customerListMap.put("resItemDetailConFlag", ws.getxAvgScore());

            Map<String, Object> x_customerListMap2 = new HashMap<>();
            x_customerListMap2.put("index", 2);
            x_customerListMap2.put("resItemDetailCon", "R+订单总额/占比");
            x_customerListMap2.put("resItemDetailConFlag", "￥：" + ws.getxAllTotal() + "/" + getPerent(ws.getxAllTotal(), ws.getxTotalIncome()));

            Map<String, Object> x_customerListMap3 = new HashMap<>();
            x_customerListMap3.put("index", 3);
            x_customerListMap3.put("resItemDetailCon", "在线支付总额占比");
            x_customerListMap3.put("resItemDetailConFlag", "￥：" + ws.getxPayTotal() + "/" + getPerent(ws.getxPayTotal(), ws.getxAllTotal()));


            Map<String, Object> x_customerListMap4 = new HashMap<>();
            x_customerListMap4.put("index", 4);
            x_customerListMap4.put("resItemDetailCon", "新增/营收/占比");
            x_customerListMap4.put("resItemDetailConFlag", ws.getxNewCustomer() + "位/￥：" + ws.getxNewCustomerTotal() + "/" + getPerent(ws.getxNewCustomerTotal(), ws.getxRestoTotal()));

            Map<String, Object> x_customerListMap5 = new HashMap<>();
            x_customerListMap5.put("index", 5);
            x_customerListMap5.put("resItemDetailCon", "自然新增数据");
            x_customerListMap5.put("resItemDetailConFlag", ws.getxNewNormalCustomer() + "位/￥：" + ws.getxNewNormalCustomerTotal() + "/" + getPerent(ws.getxNewNormalCustomerTotal(), ws.getxRestoTotal()));


            Map<String, Object> x_customerListMap6 = new HashMap<>();
            x_customerListMap6.put("index", 6);
            x_customerListMap6.put("resItemDetailCon", "分享新增数据");
            x_customerListMap6.put("resItemDetailConFlag", ws.getxNewShareCustomer() + "位/￥：" + ws.getxNewShareCustomer() + "/" + getPerent(ws.getxNewShareCustomerTotal(), ws.getxRestoTotal()));

            Map<String, Object> x_customerListMap7 = new HashMap<>();
            x_customerListMap7.put("index", 7);
            x_customerListMap7.put("resItemDetailCon", "回头营收占比");
            x_customerListMap7.put("resItemDetailConFlag", ws.getxBackCustomer() + "位/￥：" + ws.getxBackCustomerTotal() + "/" + getPerent(ws.getxBackCustomerTotal(), ws.getxRestoTotal()));

            Map<String, Object> x_customerListMap8 = new HashMap<>();
            x_customerListMap8.put("index", 8);
            x_customerListMap8.put("resItemDetailCon", "二次回头数据");
            x_customerListMap8.put("resItemDetailConFlag", ws.getxBackTwoCustomer() + "位/￥：" + ws.getxBackTwoCustomerTotal() + "/" + getPerent(ws.getxBackTwoCustomerTotal(), ws.getxRestoTotal()));

            Map<String, Object> x_customerListMap9 = new HashMap<>();
            x_customerListMap9.put("index", 9);
            x_customerListMap9.put("resItemDetailCon", "多次回头数据");
            x_customerListMap9.put("resItemDetailConFlag", ws.getxBackTwoMoreCustomer() + "位/￥：" + ws.getxBackTwoMoreCustomerTotal() + "/" + getPerent(ws.getxBackTwoMoreCustomerTotal(), ws.getxRestoTotal()));

            x_customerMaplist.add(x_customerListMap);
            x_customerMaplist.add(x_customerListMap2);
            x_customerMaplist.add(x_customerListMap3);
            x_customerMaplist.add(x_customerListMap4);
            x_customerMaplist.add(x_customerListMap5);
            x_customerMaplist.add(x_customerListMap6);
            x_customerMaplist.add(x_customerListMap7);
            x_customerMaplist.add(x_customerListMap8);
            x_customerMaplist.add(x_customerListMap9);
            x_customerMap.put("resItemDetail", x_customerMaplist);


            //13本月用户数据
            Map<String, Object> m_customerMap = new HashMap<>();
            m_customerMap.put("resId", c);
            m_customerMap.put("resItemTitle", "Resto+本月用户的数据");
            m_customerMap.put("resItemDetailFlag", "项目");
            m_customerMap.put("resItemDetailFlag", "数值");

            List<Map<String, Object>> m_customerMaplist = new ArrayList<>();
            Map<String, Object> m_customerListMap = new HashMap<>();
            m_customerListMap.put("index", 1);
            m_customerListMap.put("resItemDetailCon", "本月平均分");
            m_customerListMap.put("resItemDetailConFlag", ws.getmAvgScore());

            Map<String, Object> m_customerListMap2 = new HashMap<>();
            m_customerListMap2.put("index", 2);
            m_customerListMap2.put("resItemDetailCon", "R+订单总额/占比");
            m_customerListMap2.put("resItemDetailConFlag", "￥：" + ws.getmAllTotal() + "/" + getPerent(ws.getmAllTotal(), ws.getmTotalIncome()));

            Map<String, Object> m_customerListMap3 = new HashMap<>();
            m_customerListMap3.put("index", 3);
            m_customerListMap3.put("resItemDetailCon", "在线支付总额占比");
            m_customerListMap3.put("resItemDetailConFlag", "￥：" + ws.getmPayTotal() + "/" + getPerent(ws.getmPayTotal(), ws.getmAllTotal()));


            Map<String, Object> m_customerListMap4 = new HashMap<>();
            m_customerListMap4.put("index", 4);
            m_customerListMap4.put("resItemDetailCon", "新增/营收/占比");
            m_customerListMap4.put("resItemDetailConFlag", ws.getmNewCustomr() + "位/￥：" + ws.getmNewCustomerTotal() + "/" + getPerent(ws.getmNewCustomerTotal(), ws.getmRestoTotal()));

            Map<String, Object> m_customerListMap5 = new HashMap<>();
            m_customerListMap5.put("index", 5);
            m_customerListMap5.put("resItemDetailCon", "自然新增数据");
            m_customerListMap5.put("resItemDetailConFlag", ws.getmNewNormalCustomer() + "位/￥：" + ws.getmNewNormalCustomerTotal() + "/" + getPerent(ws.getmNewNormalCustomerTotal(), ws.getmRestoTotal()));


            Map<String, Object> m_customerListMap6 = new HashMap<>();
            m_customerListMap6.put("index", 6);
            m_customerListMap6.put("resItemDetailCon", "分享新增数据");
            m_customerListMap6.put("resItemDetailConFlag", ws.getmNewShareCustomer() + "位/￥：" + ws.getmNewShareCustomer() + "/" + getPerent(ws.getmNewShareCustomerTotal(), ws.getmRestoTotal()));

            Map<String, Object> m_customerListMap7 = new HashMap<>();
            m_customerListMap7.put("index", 7);
            m_customerListMap7.put("resItemDetailCon", "回头营收占比");
            m_customerListMap7.put("resItemDetailConFlag", ws.getmBackCustomer() + "位/￥：" + ws.getmBackCustomerTotal() + "/" + getPerent(ws.getmBackCustomerTotal(), ws.getmRestoTotal()));

            Map<String, Object> m_customerListMap8 = new HashMap<>();
            m_customerListMap8.put("index", 8);
            m_customerListMap8.put("resItemDetailCon", "二次回头数据");
            m_customerListMap8.put("resItemDetailConFlag", ws.getmBackTwoCustomer() + "位/￥：" + ws.getmBackTwoCustomerTotal() + "/" + getPerent(ws.getmBackTwoCustomerTotal(), ws.getmRestoTotal()));

            Map<String, Object> m_customerListMap9 = new HashMap<>();
            m_customerListMap9.put("index", 9);
            m_customerListMap9.put("resItemDetailCon", "多次回头数据");
            m_customerListMap9.put("resItemDetailConFlag", ws.getmBackTwoMoreCustomer() + "位/￥：" + ws.getmBackTwoMoreCustomerTotal() + "/" + getPerent(ws.getmBackTwoMoreCustomerTotal(), ws.getmRestoTotal()));

            m_customerMaplist.add(m_customerListMap);
            m_customerMaplist.add(m_customerListMap2);
            m_customerMaplist.add(m_customerListMap3);
            m_customerMaplist.add(m_customerListMap4);
            m_customerMaplist.add(m_customerListMap5);
            m_customerMaplist.add(m_customerListMap6);
            m_customerMaplist.add(m_customerListMap7);
            m_customerMaplist.add(m_customerListMap8);
            m_customerMaplist.add(m_customerListMap9);
            m_customerMap.put("resItemDetail", m_customerMaplist);

            switch (date) {
                case 1: //上旬
                    contentList.add(m_customerMap);
                    break;
                case 2://中询
                    contentList.add(z_customerMap);
                    contentList.add(m_customerMap);
                    break;
                default:
                    contentList.add(z_customerMap);
                    contentList.add(x_customerMap);
                    contentList.add(m_customerMap);
                    break;
            }
            map.put("contentList", contentList);//第二条数据

        }

        return getSuccessResult();
    }


    private String getPerent(BigDecimal a, BigDecimal b) { //a 除 b *100
        String m = "";
        if (b.compareTo(BigDecimal.ZERO) != 0) {
            m = a.divide(b, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%";
        }

        return m;
    }


}
