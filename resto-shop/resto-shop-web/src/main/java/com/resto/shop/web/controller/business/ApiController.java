package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.StringNameUtil;
import com.resto.brand.core.util.ThirdPatyUtils;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.OrderCountUtils.addKey;

/**
 * 用于同步第三方数据库的Controller(以及用来查询不正常的订单存到品牌)
 *
 * @author lmx
 */
@Controller
@RequestMapping("api")
public class ApiController extends GenericController {

    @Resource
    private OrderService orderService;

    @Resource
    private BrandSettingService brandSettingService;

    @Resource
    OrderPaymentItemService orderpaymentitemService;

    @Resource
    private ShopDetailService shopDetailService;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private PlatformOrderService platformOrderService;

    @Resource
    private PlatformOrderDetailService platformOrderDetailService;

    @Resource
    private PlatformOrderExtraService platformOrderExtraService;
    //对接嫩绿茶

    /**
     * 对接的方式:
     * 1.设定一个秘钥 appid='2323dsfadfewrasa3434" 这个是在品牌设置中开启或者关闭
     * 2.这个appid只有发送方和接收方知道
     * 3.调用时，发送方，组合各个参数用密钥 key按照一定的规则(各种排序，MD5，ip等)生成一个access_key。
     * 一起post提交到API接口
     * 4. 接收方拿到post过来的参数以及这个access_key。
     * 也和发送一样，用密钥key 对各个参数进行一样的规则(各种排序，MD5，ip等)也生成一个access_key2。
     * 5. 对比access_key 和access_key2 。一样。则允许操作，不一样，报错返回或者加入黑名单。
     * 6在店铺端设置 thirdAppid 作为参数传过来 有值 说明需要的店铺数据 没有则返回品牌数据
     *
     * @return
     */

    @RequestMapping(value = "getThirdData", method = RequestMethod.POST)
    @ResponseBody
    public Result getThirdData(String signature, String timestamp, String nonce, String appid, String thirdAppid, String beginDate, String endDate, HttpServletRequest request) {
        //默认返回false
        Result result = new Result();
        result.setSuccess(false);

        //查询所有已经配置第三方接口的品牌
        List<BrandSetting> brandSettingList = brandSettingService.selectListByState();
        List<String> appidList = new ArrayList<>();
        if (!brandSettingList.isEmpty()) {
            for (BrandSetting brandSetting : brandSettingList) {
                appidList.add(brandSetting.getAppid());
            }
        }
        /**
         * 1.将appId timestamp nonce 三个参数进行字典排序
         * 2,.将3个参数字符串拼接成一个字符串进行sha1加密
         */
        if (ThirdPatyUtils.checkSignature(signature, timestamp, nonce, appidList)) {
            BrandSetting brandSetting = brandSettingService.selectByAppid(appid);
            //定位数据库
            request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID, brandSetting.getBrandId());
            if (null == brandSetting || "0".equals(brandSetting.getOpenThirdInterface().toString())) {
                result.setSuccess(false);
                result.setMessage("参数非法");
                return result;
            }
            //判断是需要店铺数据还是品牌数据
            List<Order> orderList = new ArrayList<>();
            ShopDetail shopDetail = null;
            if (StringUtils.isEmpty(thirdAppid)) {//说明需要的是品牌数据
                orderList = orderService.selectBaseToThirdList(brandSetting.getBrandId(), beginDate, endDate);
            } else {
                //说明需要的是店铺端的数据
                //判断是否是
                shopDetail = shopDetailService.selectByThirdAppId(thirdAppid);
                if (null == shopDetail) {
                    result.setSuccess(false);
                    result.setMessage("参数非法");
                    return result;
                } else {
                    orderList = orderService.selectBaseToThirdListByShopId(shopDetail.getId(), beginDate, endDate);
                }
            }
            //把需要的信息封装起来
            List<Map<String, Object>> ThirdData = new ArrayList<>(1000);
            if (!orderList.isEmpty()) {
                for (Order o : orderList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("posId", o.getShopDetailId());
                    map.put("addTime", DateUtil.formatDate(o.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));//订单创建时间
                    map.put("posDate", DateUtil.formatDate(o.getPrintOrderTime(), "yyyy-MM-dd HH:mm:ss"));//订单推送时间
                    map.put("tableNumber", o.getTableNumber());//座号
                    map.put("serialNumber", o.getSerialNumber());//
                    map.put("shopName", shopDetail.getName());
                    //订单支付项
                    List<Map<Integer, BigDecimal>> payList = new ArrayList<>();
                    if (!o.getOrderPaymentItems().isEmpty()) {
                        Map<Integer, BigDecimal> payMap = new HashedMap();
                        for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                            addKey(oi.getPaymentModeId(), oi.getPayValue(), payMap);
                        }
                        payList.add(payMap);
                    }
                    List<Map<String, Object>> itemList = new ArrayList<>();
                    //订单菜品项
                    if (!o.getOrderItems().isEmpty()) {
                        for (OrderItem orderItem : o.getOrderItems()) {
                            Map<String, Object> itemMap = new HashedMap();
                            //对名字进行截取存规格
                            String str = StringNameUtil.getName(orderItem.getArticleName());
                            itemMap.put("menuName", str + "R+");
                            // itemMap.put("attr", StringNameUtil.getAttr(orderItem.getArticleName()));
                            itemMap.put("menuType", orderItem.getType());//订单菜品类型
                            itemMap.put("menuCode", orderItem.getArticleId());//菜品id
                            itemMap.put("number", orderItem.getCount());//个数
                            itemMap.put("originalPrice", orderItem.getOriginalPrice());
                            itemMap.put("unitPirce", orderItem.getUnitPrice());
                            itemMap.put("refundCount", orderItem.getRefundCount());
                            // itemMap.put("parentType",orderItem.getParentId());//针对套餐和子品
                            itemList.add(itemMap);
                        }
                    }
                    map.put("itemList", itemList);
                    map.put("payList", payList);
                    ThirdData.add(map);
                }
            }
            return getSuccessResult(ThirdData);
            //查询地方报表需要的数据
        }

        return result;
    }

    //对接kc
    @RequestMapping(value = "getKcData", method = RequestMethod.POST)
    @ResponseBody
    public Result getKcData(String signature, String timestamp, String nonce, String appid, String thirdAppid, String beginDate, String endDate, HttpServletRequest request) {
        //默认返回false
        Result result = new Result();
        result.setSuccess(false);
        //订单总金额
        BigDecimal zongjine = BigDecimal.ZERO;
        //计算总额
        BigDecimal sum = BigDecimal.ZERO;
        //计算支付宝总和
        BigDecimal aliPay = BigDecimal.ZERO;
        //充值余额
        BigDecimal yuE = BigDecimal.ZERO;
        //查询所有已经配置第三方接口的品牌
        List<BrandSetting> brandSettingList = brandSettingService.selectListByState();
        List<String> appidList = new ArrayList<>();
        if (!brandSettingList.isEmpty()) {
            for (BrandSetting brandSetting : brandSettingList) {
                appidList.add(brandSetting.getAppid());
            }
        }
        /**
         * 1.将appId timestamp nonce 三个参数进行字典排序
         * 2,.将3个参数字符串拼接成一个字符串进行sha1加密
         */
        if (ThirdPatyUtils.checkSignature(signature, timestamp, nonce, appidList)) {
            //定位数据库
            BrandSetting brandSetting = brandSettingService.selectByAppid(appid);
            if (null == brandSetting || "0".equals(brandSetting.getOpenThirdInterface().toString())) {
                result.setSuccess(false);
                result.setMessage("参数非法");
                return result;
            }
            //判断是需要店铺数据还是品牌数据
            List<Order> orderList = new ArrayList<>();
            //定位数据库
            request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID, brandSetting.getBrandId());
            if (StringUtils.isEmpty(thirdAppid)) {//说明需要的是品牌数据
                orderList = orderService.selectBaseToKCList(brandSetting.getBrandId(), beginDate, endDate);
            } else {
                //说明需要的是店铺端的数据
                //判断是否是
                ShopDetail shopDetail = shopDetailService.selectByThirdAppId(thirdAppid);
                if (null == shopDetail) {
                    result.setSuccess(false);
                    result.setMessage("参数非法");
                    return result;
                } else {
                    orderList = orderService.selectBaseToKCListByShopId(shopDetail.getId(), beginDate, endDate);
                }
            }
            //把需要的信息封装起来
            List<Map<String, Object>> ThirdData = new ArrayList<>(1000);
            for (Order o : orderList) {
                BigDecimal money = orderpaymentitemService.sumOrderMoneyByOrderId(o.getId());
                Map<String, Object> ordermap = new HashedMap();
                ordermap.put("id", o.getId());//订单id
                ordermap.put("createTime", DateUtil.formatDate(o.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));//下单时间
                ordermap.put("orderMoney", money);//订单金额
                ordermap.put("amountWithChild", o.getAmountWithChildren());
                ordermap.put("payMode", o.getPayMode());
//                        if(o.getProductionStatus()==6){
//                            ordermap.put("aritlceOver",1);
//                        }else {
//                            ordermap.put("articleOver",0);
//                        }
//                        ordermap.put("cancel",o.getOrderState()==9?0:1);
//                        if(o.getOrderState()==9){
//                            System.out.println("该订单已经取消"+o.getId());
//                        }

                zongjine = zongjine.add(money);

                ordermap.put("parentOrderId", o.getParentOrderId());
                List<Map<String, Object>> payList = new ArrayList<>();
                if (!o.getOrderPaymentItems().isEmpty()) {
                    for (OrderPaymentItem oi : o.getOrderPaymentItems()) {
                        Map<String, Object> payMap = new HashedMap();
                        payMap.put("orderPaymentId", oi.getId());
                        payMap.put("type", oi.getPayValue().compareTo(BigDecimal.ZERO) > 0 ? 1 : -1);
                        payMap.put("payValue", oi.getPayValue());
                        payMap.put("payTime", DateUtil.formatDate(oi.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
//                                if(oi.getResultData().contains("ERROR")||oi.getResultData().equals("{}")||oi.getResultData().contains("累计退款金额大于支付金额")){
//                                    payMap.put("state",0);
//                                }else {
//                                    payMap.put("state",1);
//                                }
                        //计算总额用来对账
                        if (oi.getPaymentModeId() == PayMode.WEIXIN_PAY) {
                            sum = sum.add(oi.getPayValue());
                        } else if (oi.getPaymentModeId() == PayMode.ALI_PAY) {
                            aliPay = aliPay.add(oi.getPayValue());
                        } else if (oi.getPaymentModeId() == PayMode.CHARGE_PAY) {
                            yuE = yuE.add(oi.getPayValue());
                        }
                        payList.add(payMap);
                    }
                }
                ordermap.put("payList", payList);
                ThirdData.add(ordermap);
            }
            return getKcSuccessResult(ThirdData, sum, beginDate, endDate, aliPay, yuE, zongjine);
        }
        return result;
    }


    @RequestMapping(value = "getKcTakeOutData", method = RequestMethod.POST)
    @ResponseBody
    public Result getKcTakeOutData( String appid, String thirdAppid, String beginDate, String endDate,HttpServletRequest request) {
        BrandSetting brandSetting = brandSettingService.selectByAppid(appid);
        Result result = new Result();
        //定位数据库
        request.getSession().setAttribute(SessionKey.CURRENT_BRAND_ID, brandSetting.getBrandId());
        ShopDetail shopDetail = shopDetailService.selectByThirdAppId(thirdAppid);
        List<PlatformOrder> platformOrders= new ArrayList<>();
        if (null == shopDetail) {
            result.setSuccess(false);
            result.setMessage("参数非法");
            return result;
        } else {
             platformOrders = platformOrderService.selectshopDetailIdList(beginDate, endDate, shopDetail.getId());
        }

        List<Map<String, Object>> payList = new ArrayList<>();

        //外卖
        BigDecimal totalMoney=BigDecimal.ZERO;

        if (platformOrders!=null && platformOrders.size()>0){
            for (PlatformOrder platformOrder : platformOrders) {
                Map<String, Object> platformmap = new HashedMap();
                platformmap.put("takeOutType",platformOrder.getType());
                platformmap.put("takeOutMoney",platformOrder.getTotalPrice());
                platformmap.put("createTime",DateUtil.formatDate(platformOrder.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                payList.add(platformmap);
                totalMoney=totalMoney.add(platformOrder.getTotalPrice());
            }
        }
        return getKcTakeOutSuccessResult(payList,totalMoney,beginDate,endDate);
    }

}
