package com.resto.shop.web.controller.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.web.dto.RefundArticleOrder;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.OrderItem;
import com.resto.shop.web.model.OrderPaymentItem;
import com.resto.shop.web.service.OrderItemService;
import com.resto.shop.web.service.OrderPaymentItemService;
import com.resto.shop.web.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("refundArticle")
public class RefundArticleController extends GenericController{

    @Resource
    OrderService orderService;

    @Resource
    OrderPaymentItemService orderPaymentItemService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    ShopDetailService shopDetailService;

    @RequestMapping("/list")
    public String list(){
        return "refundArticle/list";
    }

    @RequestMapping("/shop/list")
    public String shopList(){
        return "refundArticle/shopList";
    }

    /**
     * 查询退菜报表list
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("/getRefundArticleList")
    @ResponseBody
    public Result getRefundArticleList(String beginDate, String endDate){
        try{
            //得到所有退菜单信息
            List<RefundArticleOrder> refundArticleOrders = orderService.addRefundArticleDto(beginDate, endDate, null);
            for (RefundArticleOrder articleOrder : refundArticleOrders){
                for (ShopDetail shopDetail : getCurrentShopDetails()){
                    if (articleOrder.getShopId().equalsIgnoreCase(shopDetail.getId())){
                        articleOrder.setShopName(shopDetail.getName());
                        break;
                    }
                }
            }
            return getSuccessResult(refundArticleOrders);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询退菜报表出错" + e.getMessage());
            return new Result(false);
        }
    }


    /**
     * 查询退菜报表list
     * @param beginDate
     * @param endDate
     * @return
     */
    @RequestMapping("/shop/getRefundArticleList")
    @ResponseBody
    public Result getShopRefundArticleList(String beginDate, String endDate){
        try{
            //得到所有退菜单信息
            List<RefundArticleOrder> refundArticleOrders = orderService.addRefundArticleDto(beginDate, endDate, getCurrentShopId());
            for (RefundArticleOrder articleOrder : refundArticleOrders){
                for (ShopDetail shopDetail : getCurrentShopDetails()){
                    if (articleOrder.getShopId().equalsIgnoreCase(shopDetail.getId())){
                        articleOrder.setShopName(shopDetail.getName());
                        break;
                    }
                }
            }
            return getSuccessResult(refundArticleOrders);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询退菜报表出错" + e.getMessage());
            return new Result(false);
        }
    }

    @RequestMapping("/getRefundArticleDetail")
    @ResponseBody
    public Result getRefundArticleDetail(String orderId){
        try{
            JSONObject jsonObject = new JSONObject();
            //得到订单项
            Order order = orderService.selectById(orderId);
            //得到该订单所在店铺
            ShopDetail shopDetail = shopDetailService.selectById(order.getShopDetailId());
            //得到退款支付项
            List<OrderPaymentItem> orderPaymentItems = orderPaymentItemService.selectRefundPayMent(orderId);
            //得到退菜的明细
            List<OrderItem> orderItems = orderItemService.selectRefundArticleItem(orderId);
            //组装退款详情
            for (OrderPaymentItem paymentItem : orderPaymentItems){
                if (paymentItem.getPaymentModeId().equals(PayMode.WEIXIN_PAY)){
                    paymentItem.setId(JSONObject.parseObject(paymentItem.getResultData()).getString("out_refund_no"));
                }else if (paymentItem.getPaymentModeId().equals(PayMode.ALI_PAY)){
                    paymentItem.setId(JSONObject.parseObject(paymentItem.getResultData()).getString("out_trade_no"));
                }
                paymentItem.setPayValue(paymentItem.getPayValue().abs());
                paymentItem.setPaymentModeVal(PayMode.getPayModeName(paymentItem.getPaymentModeId()));
            }
            //声明菜品信息数组，存储退菜详情
            JSONArray array = new JSONArray();
            for (OrderItem orderItem : orderItems){
                jsonObject = new JSONObject();
                jsonObject.put("articleName", orderItem.getArticleName());
                jsonObject.put("unitPrice", orderItem.getUnitPrice());
                jsonObject.put("refundCount", orderItem.getOrderRefundRemark().getRefundCount());
                jsonObject.put("refundMoney", orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getOrderRefundRemark().getRefundCount())));
                jsonObject.put("refundTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderItem.getOrderRefundRemark().getCreateTime()));
                jsonObject.put("refundRemark", orderItem.getOrderRefundRemark().getRefundRemark());
                if (StringUtils.isNotBlank(orderItem.getOrderRefundRemark().getRemarkSupply())){
                    jsonObject.put("refundRemark", jsonObject.get("refundRemark").toString().concat("、").concat(orderItem.getOrderRefundRemark().getRemarkSupply()));
                }
                array.add(jsonObject);
            }
            //查看是否有退服务费
            if (order.getBaseCustomerCount() != null && order.getCustomerCount() != null
                    && !order.getBaseCustomerCount().equals(order.getCustomerCount())
                    && order.getBaseCustomerCount() > order.getCustomerCount()){
                jsonObject = new JSONObject();
                jsonObject.put("articleName", shopDetail.getServiceName());
                jsonObject.put("unitPrice", order.getServicePrice().divide(new BigDecimal(order.getCustomerCount())));
                jsonObject.put("refundCount", order.getBaseCustomerCount() - order.getCustomerCount());
                jsonObject.put("refundMoney", new BigDecimal(order.getBaseCustomerCount() - order.getCustomerCount()).multiply(new BigDecimal(jsonObject.get("unitPrice").toString())));
                jsonObject.put("refundTime", "--");
                jsonObject.put("refundRemark", "--");
                array.add(jsonObject);
            }
            //查看是否有退餐盒费
            if (order.getBaseMealAllCount() != null && order.getMealAllNumber() != null
                    && !order.getBaseMealAllCount().equals(order.getMealAllNumber())
                    && order.getBaseMealAllCount() > order.getMealAllNumber()){
                jsonObject = new JSONObject();
                jsonObject.put("articleName", shopDetail.getMealFeeName());
                jsonObject.put("unitPrice", order.getMealFeePrice().divide(new BigDecimal(order.getMealAllNumber())));
                jsonObject.put("refundCount", order.getBaseMealAllCount() - order.getMealAllNumber());
                jsonObject.put("refundMoney", new BigDecimal(order.getBaseMealAllCount() - order.getMealAllNumber()).multiply(new BigDecimal(jsonObject.get("unitPrice").toString())));
                jsonObject.put("refundTime", "--");
                jsonObject.put("refundRemark", "--");
                array.add(jsonObject);
            }
            jsonObject = new JSONObject();
            jsonObject.put("refundPayment", orderPaymentItems);
            jsonObject.put("refundItem", array);
            return getSuccessResult(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询退菜明细出错" + e.getMessage());
            return new Result(false);
        }
    }

    @RequestMapping("/createExcel")
    @ResponseBody
    public Result createExcel(String beginDate, String endDate, RefundArticleOrder refundArticleOrder, HttpServletRequest request){
        List<ShopDetail> shopDetailList = getCurrentShopDetails();
        // 导出文件名
        String str = "退菜报表" + beginDate + "至" + endDate + ".xls";
        String path = request.getSession().getServletContext().getRealPath(str);
        String shopName = "";
        for (ShopDetail shopDetail : shopDetailList) {
            shopName += shopDetail.getName() + ",";
        }
        // 去掉最后一个逗号
        shopName = shopName.substring(0, shopName.length() - 1);

        Map<String, String> map = new HashMap<>();
        map.put("brandName", getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", beginDate);
        map.put("reportType", "退菜报表");// 表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "6");// 显示的位置
        map.put("reportTitle", "退菜报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] headers = {{"店铺", "20"},{"订单编号", "20"},{"桌号", "20"},{"手机号", "20"},{"用户名称", "20"},{"退菜数量", "20"},{"退菜金额", "20"}};
        String[] columns = {"shopName", "orderId", "tableNumber", "telephone", "nickName", "refundCount", "refundMoney"};

        List<RefundArticleOrder> result = new ArrayList<>();
        if (refundArticleOrder.getRefundArticleOrderList() != null && refundArticleOrder.getRefundArticleOrderList().size() != 0) {
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            filter.getExcludes().add("refundArticleOrderList");
            String json = JSON.toJSONString(refundArticleOrder.getRefundArticleOrderList(), filter);
            List<RefundArticleOrder> brandIncomeDto = JSON.parseObject(json, new TypeReference<List<RefundArticleOrder>>() {
            });
            result.addAll(brandIncomeDto);
        }
        ExcelUtil<RefundArticleOrder> excelUtil = new ExcelUtil<>();
        try {
            OutputStream out = new FileOutputStream(path);
            excelUtil.ExportExcel(headers, columns, result, out, map);
            out.close();
        } catch (Exception e) {
            log.error("生成退菜报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(path);
    }

    /**
     * 下载报表
     * @param path
     * @param response
     */
    @RequestMapping("/downloadExcel")
    public void downloadExcel(String path, HttpServletResponse response){
        ExcelUtil<RefundArticleOrder> excelUtil = new ExcelUtil<>();
        try {
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        }catch (Exception e){
            log.error("下载退菜报表出错！");
            JOptionPane.showMessageDialog(null, "导出失败！");
            e.printStackTrace();
            throw e;
        }
    }
}
