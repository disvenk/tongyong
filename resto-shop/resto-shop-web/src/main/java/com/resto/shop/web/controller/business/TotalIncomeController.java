package com.resto.shop.web.controller.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.dto.OrderNumDto;
import com.resto.shop.web.model.OrderPaymentItem;
import com.resto.shop.web.service.OffLineOrderService;
import com.resto.shop.web.service.OrderPaymentItemService;
import com.resto.shop.web.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.util.ExcelUtil;
import com.resto.brand.web.dto.ShopIncomeDto;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;

@Controller
@RequestMapping("totalIncome")
public class TotalIncomeController extends GenericController {

    @Resource
    BrandService brandService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    OrderService orderService;

    @Resource
    OffLineOrderService offLineOrderService;

    @Autowired
    OrderPaymentItemService orderPaymentItemService;

    @RequestMapping("/list")
    public String list() {
        return "totalIncome/list";
    }

    @RequestMapping("/shop/list")
    public String shopList() {
        return "totalIncome/shopList";
    }


    @RequestMapping("reportIncome")
    @ResponseBody
    public Map<String, Object> selectIncomeReportList(@RequestParam("beginDate") String beginDate, @RequestParam("endDate") String endDate) {
        return getIncomeReportList(beginDate, endDate);
    }

    /**
     * 2016-10-29
     * 查询收入报表（品牌+店铺）
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    private Map<String, Object> getIncomeReportList(String beginDate, String endDate) {
        //封装品牌信息
        ShopIncomeDto brandIncomeDto = new ShopIncomeDto(getBrandName(), getCurrentBrandId());
        //封装店铺的信息
        List<ShopIncomeDto> shopIncomeDtos = getCurrentShopDetails().stream()
                .map(shopDetail -> new ShopIncomeDto(shopDetail.getName(), shopDetail.getId()))
                .collect(Collectors.toList());
        //用来接收分段查询出来的订单金额信息
        List<ShopIncomeDto> shopIncomeDtosItem = new ArrayList<>();
        shopIncomeDtosItem.add(new ShopIncomeDto());
        //用来累加分段查询出来的订单金额信息
        List<ShopIncomeDto> shopIncomeDtosItems = new ArrayList<>();
        //用来接收分段查询出来的订单支付项信息
        List<OrderPaymentItem> shopIncomeDtosPayMent = new ArrayList<>();
        shopIncomeDtosPayMent.add(new OrderPaymentItem());
        //用来累加分段查询出来的订单支付项信息
        List<OrderPaymentItem> shopIncomeDtosPayMents = new ArrayList<>();
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("beginDate", beginDate);
        selectMap.put("endDate", endDate);
        for (int pageNo = 0; shopIncomeDtosItem != null && !shopIncomeDtosItem.isEmpty(); pageNo++) {
            selectMap.put("pageNo", pageNo * 1000);
            shopIncomeDtosItem = orderService.callProcDayAllOrderItem(selectMap);
            shopIncomeDtosItems.addAll(shopIncomeDtosItem);
        }
        for (int pageNo = 0; shopIncomeDtosPayMent != null && !shopIncomeDtosPayMent.isEmpty(); pageNo++) {
            selectMap.put("pageNoTwo", pageNo * 1000);
            shopIncomeDtosPayMent = orderService.selectBusinessReport(selectMap);
            shopIncomeDtosPayMents.addAll(shopIncomeDtosPayMent);
        }
        //查询店铺线下交易笔数
        List<OrderNumDto> orderNumDtoOffLineList = offLineOrderService.selectOrderNumByTimeAndBrandId(getCurrentBrandId(), beginDate, endDate);
        //得到店铺营业信息
        for (ShopIncomeDto shopIncomeDto : shopIncomeDtos) {
            //将线下交易订单根据shopId分组
            Integer offLineOrderNum = orderNumDtoOffLineList.stream().filter(orderNumDto -> orderNumDto.getShopId().equalsIgnoreCase(shopIncomeDto.getShopDetailId()))
                    .map(OrderNumDto::getNum).findFirst().orElse(0);
            shopIncomeDto.setOffLineOrderNum(offLineOrderNum);
            //得到当前店铺的订单消息
            List<ShopIncomeDto> nowShopList = shopIncomeDtosItems.stream().filter(shopIncomeDtoItem -> shopIncomeDto.getShopDetailId().equalsIgnoreCase(shopIncomeDtoItem.getShopDetailId()))
                    .collect(Collectors.toList());
            //Resto交易数
            Integer restoOrderNum = nowShopList.size();
            shopIncomeDto.setRestoOrderNum(restoOrderNum);
            //聚合店铺订单金额相关字段
            setShopIncomeDtoItem(shopIncomeDto, nowShopList);
            //得到当前店铺的订单支付信息
            List<OrderPaymentItem> paymentItemList = shopIncomeDtosPayMents.stream().filter(orderPaymentItem -> orderPaymentItem.getShopDetailId().equalsIgnoreCase(shopIncomeDto.getShopDetailId()))
                    .collect(Collectors.toList());
            //聚合当前门店的支付项信息
            setShopIncomeDtoPayMent(shopIncomeDto, paymentItemList);
            //计算店铺连接率
            BigDecimal totalOrderNum = new BigDecimal(restoOrderNum).add(new BigDecimal(offLineOrderNum));
            String connectRatio = totalOrderNum.compareTo(BigDecimal.ZERO) == 0 ? "无" : new BigDecimal(restoOrderNum).divide(totalOrderNum, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
            shopIncomeDto.setConnctRatio(connectRatio);
        }
        //得到品牌汇总信息
        setBrandIncomeDto(brandIncomeDto, shopIncomeDtos);
        List<ShopIncomeDto> brandIncomeDtos = new ArrayList<>();
        brandIncomeDtos.add(brandIncomeDto);
        Map<String, Object> map = new HashMap<>();
        map.put("shopIncome", shopIncomeDtos);
        map.put("brandIncome", brandIncomeDtos);
        return map;
    }

    /**
     * 聚合店铺订单金额、折扣金额等订单信息
     *
     * @param shopIncomeDto
     * @param newShopIncomeList
     */
    private void setShopIncomeDtoItem(ShopIncomeDto shopIncomeDto, List<ShopIncomeDto> newShopIncomeList) {
        //POS端折扣
        BigDecimal orderPosDiscountMoney = newShopIncomeList.stream().map(ShopIncomeDto::getOrderPosDiscountMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        shopIncomeDto.setOrderPosDiscountMoney(orderPosDiscountMoney);
        //会员折扣
        BigDecimal memberDiscounrMoney = newShopIncomeList.stream().map(ShopIncomeDto::getMemberDiscountMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        shopIncomeDto.setMemberDiscountMoney(memberDiscounrMoney);
        //抹零金额
        BigDecimal realEraseMoney = newShopIncomeList.stream().map(ShopIncomeDto::getRealEraseMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        shopIncomeDto.setRealEraseMoney(realEraseMoney);
        //抹零金额
        BigDecimal exemptionMoney = newShopIncomeList.stream().map(ShopIncomeDto::getExemptionMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        shopIncomeDto.setExemptionMoney(exemptionMoney);
        //赠菜金额
        BigDecimal grantMoney = newShopIncomeList.stream().map(ShopIncomeDto::getGrantArticleMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        shopIncomeDto.setGrantArticleMoney(grantMoney);
        //原价金额
        shopIncomeDto.setOriginalAmount(newShopIncomeList.stream().map(ShopIncomeDto::getOriginalAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(orderPosDiscountMoney).add(memberDiscounrMoney).add(realEraseMoney));
        //订单金额
        shopIncomeDto.setTotalIncome(newShopIncomeList.stream().map(ShopIncomeDto::getTotalIncome).reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(orderPosDiscountMoney).add(memberDiscounrMoney).add(realEraseMoney).add(grantMoney));
        //折扣金额总和
        shopIncomeDto.setDiscountTotalMoney(orderPosDiscountMoney.add(memberDiscounrMoney).add(realEraseMoney).add(exemptionMoney).add(grantMoney));

    }

    /**
     * 循环累加得到店铺各个支付项的值
     *
     * @param shopIncomeDto
     * @param paymentItemList
     */
    private void setShopIncomeDtoPayMent(ShopIncomeDto shopIncomeDto, List<OrderPaymentItem> paymentItemList) {
        //将现金退款从支付来源中去除
        List<OrderPaymentItem> cashRefundList = paymentItemList.stream().filter(orderPaymentItem -> Optional.ofNullable(orderPaymentItem.getRefundSourceId()).isPresent())
                .collect(Collectors.toList());
        cashRefundList.forEach(cashRefund ->
            paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getId().equalsIgnoreCase(cashRefund.getRefundSourceId())
                && !orderPaymentItem.getPaymentModeId().equals(PayMode.WEIXIN_PAY)&& !orderPaymentItem.getPaymentModeId().equals(PayMode.ALI_PAY))
                .forEach(orderPaymentItem -> orderPaymentItem.setPayValue(orderPaymentItem.getPayValue().add(cashRefund.getPayValue())))
        );
        shopIncomeDto.setOnLineWechatIncome(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.WEIXIN_PAY)
            && StringUtils.isNotBlank(orderPaymentItem.getResultData())).map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setOnLineAliPayment(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.ALI_PAY)
                && StringUtils.isNotBlank(orderPaymentItem.getResultData())).map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setOffLineWechatIncome(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.WEIXIN_PAY)
                && StringUtils.isBlank(orderPaymentItem.getResultData())).map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setOffLineAliPayment(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.ALI_PAY)
                && StringUtils.isBlank(orderPaymentItem.getResultData())).map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setChargeAccountIncome(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.CHARGE_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setRedIncome(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.ACCOUNT_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setCouponIncome(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.COUPON_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setChargeGifAccountIncome(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.REWARD_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setWaitNumberIncome(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.WAIT_MONEY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setBackCartPay(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.BANK_CART_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setMoneyPay(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.CRASH_PAY)
                || orderPaymentItem.getPaymentModeId().equals(PayMode.GIVE_CHANGE)).map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setShanhuiPayment(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.SHANHUI_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setIntegralPayment(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.INTEGRAL_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setRefundCrashPayment(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.REFUND_CRASH))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add).abs());
        shopIncomeDto.setGroupPurchasePayment(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.GROUP_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setCashCouponPayment(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.CASH_COUPIN_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add).abs());
        shopIncomeDto.setCardRechargePay(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.CARD_CHARGE_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setCardRechargeFreePay(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.CARD_REWARD_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setRefundCardPay(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.CARD_REFUND_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add).abs());
        shopIncomeDto.setCardDiscountPay(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.CARD_DISCOUNT_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        shopIncomeDto.setArticleBackPay(paymentItemList.stream().filter(orderPaymentItem -> orderPaymentItem.getPaymentModeId().equals(PayMode.ARTICLE_BACK_PAY))
                .map(OrderPaymentItem::getPayValue).reduce(BigDecimal.ZERO, BigDecimal::add).abs());
        //累加店铺实收金额 实收金额=微信支付+支付宝+现金+银联+充值金额支付+新美大支付
        shopIncomeDto.setAmountCollected(shopIncomeDto.getOnLineWechatIncome().add(shopIncomeDto.getOnLineAliPayment())
                .add(shopIncomeDto.getBackCartPay()).add(shopIncomeDto.getOffLineWechatIncome()).add(shopIncomeDto.getOffLineAliPayment())
                .add(shopIncomeDto.getMoneyPay()).add(shopIncomeDto.getChargeAccountIncome()).add(shopIncomeDto.getShanhuiPayment())
                .add(shopIncomeDto.getGroupPurchasePayment().add(shopIncomeDto.getCardRechargePay())));
        //累加折扣
        shopIncomeDto.setDiscountTotalMoney(shopIncomeDto.getDiscountTotalMoney().add(shopIncomeDto.getRedIncome()).add(shopIncomeDto.getCouponIncome())
                .add(shopIncomeDto.getChargeGifAccountIncome()).add(shopIncomeDto.getWaitNumberIncome()).add(shopIncomeDto.getIntegralPayment())
                .add(shopIncomeDto.getCashCouponPayment()));
        //累加退款金额总和
        shopIncomeDto.setRefundTotalMoney(shopIncomeDto.getArticleBackPay().add(shopIncomeDto.getRefundCrashPayment()));

    }

    /**
     * 根据各店铺的营业信息封装品牌的信息
     *
     * @param brandIncomeDto
     * @param shopIncomeDtos
     */
    private void setBrandIncomeDto(ShopIncomeDto brandIncomeDto, List<ShopIncomeDto> shopIncomeDtos) {
        brandIncomeDto.setOriginalAmount(shopIncomeDtos.stream().map(ShopIncomeDto::getOriginalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setTotalIncome(shopIncomeDtos.stream().map(ShopIncomeDto::getTotalIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setOnLineWechatIncome(shopIncomeDtos.stream().map(ShopIncomeDto::getOnLineWechatIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setOnLineAliPayment(shopIncomeDtos.stream().map(ShopIncomeDto::getOnLineAliPayment).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setOffLineWechatIncome(shopIncomeDtos.stream().map(ShopIncomeDto::getOffLineWechatIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setOffLineAliPayment(shopIncomeDtos.stream().map(ShopIncomeDto::getOffLineAliPayment).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setOrderPosDiscountMoney(shopIncomeDtos.stream().map(ShopIncomeDto::getOrderPosDiscountMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setMemberDiscountMoney(shopIncomeDtos.stream().map(ShopIncomeDto::getMemberDiscountMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setRealEraseMoney(shopIncomeDtos.stream().map(ShopIncomeDto::getRealEraseMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setChargeAccountIncome(shopIncomeDtos.stream().map(ShopIncomeDto::getChargeAccountIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setRedIncome(shopIncomeDtos.stream().map(ShopIncomeDto::getRedIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setCouponIncome(shopIncomeDtos.stream().map(ShopIncomeDto::getCouponIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setChargeGifAccountIncome(shopIncomeDtos.stream().map(ShopIncomeDto::getChargeGifAccountIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setWaitNumberIncome(shopIncomeDtos.stream().map(ShopIncomeDto::getWaitNumberIncome).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setBackCartPay(shopIncomeDtos.stream().map(ShopIncomeDto::getBackCartPay).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setMoneyPay(shopIncomeDtos.stream().map(ShopIncomeDto::getMoneyPay).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setShanhuiPayment(shopIncomeDtos.stream().map(ShopIncomeDto::getShanhuiPayment).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setIntegralPayment(shopIncomeDtos.stream().map(ShopIncomeDto::getIntegralPayment).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setArticleBackPay(shopIncomeDtos.stream().map(ShopIncomeDto::getArticleBackPay).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setRefundCrashPayment(shopIncomeDtos.stream().map(ShopIncomeDto::getRefundCrashPayment).reduce(BigDecimal.ZERO, BigDecimal::add));
        //品牌累加实收金额
        brandIncomeDto.setAmountCollected(shopIncomeDtos.stream().map(ShopIncomeDto::getAmountCollected).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setExemptionMoney(shopIncomeDtos.stream().map(ShopIncomeDto::getExemptionMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setDiscountTotalMoney(shopIncomeDtos.stream().map(ShopIncomeDto::getDiscountTotalMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setRefundTotalMoney(shopIncomeDtos.stream().map(ShopIncomeDto::getRefundTotalMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setGroupPurchasePayment(shopIncomeDtos.stream().map(ShopIncomeDto::getGroupPurchasePayment).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setCashCouponPayment(shopIncomeDtos.stream().map(ShopIncomeDto::getCashCouponPayment).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setCardDiscountPay(shopIncomeDtos.stream().map(ShopIncomeDto::getCardDiscountPay).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setCardRechargePay(shopIncomeDtos.stream().map(ShopIncomeDto::getCardRechargePay).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setCardRechargeFreePay(shopIncomeDtos.stream().map(ShopIncomeDto::getCardRechargeFreePay).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setRefundCardPay(shopIncomeDtos.stream().map(ShopIncomeDto::getRefundCardPay).reduce(BigDecimal.ZERO, BigDecimal::add));
        brandIncomeDto.setGrantArticleMoney(shopIncomeDtos.stream().map(ShopIncomeDto::getGrantArticleMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    /**
     * 2016-10-29
     * 生成报表
     *
     * @param beginDate
     * @param endDate
     * @param request
     * @throws IOException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/brandExprotExcel")
    @ResponseBody
    public Result exprotBrandExcel(@RequestParam("beginDate") String beginDate, @RequestParam("endDate") String endDate,
                                   ShopIncomeDto shopIncomeDto, HttpServletRequest request) throws IOException, Exception {
        String str = "营业总额报表" + beginDate.replace(":", "-") + "至" + endDate.replace(":", "-") + ".xls";
        String path = request.getSession().getServletContext().getRealPath(str);
        String shopName = getCurrentShopNames();
        Map<String, String> map = new HashMap<>();
        map.put("brandName", getBrandName());
        map.put("shops", shopName);
        map.put("beginDate", beginDate);
        map.put("reportType", "品牌营业额报表");// 表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "34");// 显示的位置
        map.put("reportTitle", "品牌收入条目");// 表的名字
        List<ShopIncomeDto> result = new ArrayList<>();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("brandIncomeDtos");
        filter.getExcludes().add("shopIncomeDtos");
        if (shopIncomeDto.getBrandIncomeDtos() != null) {
            String brandJson = JSON.toJSONString(shopIncomeDto.getBrandIncomeDtos(), filter);
            List<ShopIncomeDto> brandIncomeDto = JSON.parseObject(brandJson, new TypeReference<List<ShopIncomeDto>>() {
            });
            result.addAll(brandIncomeDto);
        }
        String shopJson = JSON.toJSONString(shopIncomeDto.getShopIncomeDtos(), filter);
        List<ShopIncomeDto> shopIncomeDtos = JSON.parseObject(shopJson, new TypeReference<List<ShopIncomeDto>>() {
        });
        Optional.ofNullable(shopIncomeDtos).ifPresent(shopIncomeDtos1 -> result.addAll(shopIncomeDtos1));
        ExcelUtil<ShopIncomeDto> excelUtil = new ExcelUtil<>();
        try {
            OutputStream out = new FileOutputStream(path);
            excelUtil.NewExportExcel(result, out, map);
            out.close();
        } catch (Exception e) {
            log.error("生成营业总额报表出错！");
            e.printStackTrace();
            return new Result(false);
        }
        return getSuccessResult(path);
    }

    /**
     * 下载报表
     *
     * @param path
     * @param response
     */
    @RequestMapping("/downloadExcel")
    public void downloadExcel(String path, HttpServletResponse response) {
        ExcelUtil<ShopIncomeDto> excelUtil = new ExcelUtil<>();
        try {
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        } catch (Exception e) {
            log.error("下载营业总额报表出错！");
            JOptionPane.showMessageDialog(null, "导出失败！");
            e.printStackTrace();
            throw e;
        }
    }

    @RequestMapping("/createMonthDto")
    @ResponseBody
    public Result createMonthDto(String year, String month, Integer type, HttpServletRequest request) {
        Integer monthDay = getMonthDay(year, month);
        // 导出文件名
        String typeName = type.equals(Common.YES) ? "店铺营业总额月报表" : "品牌营业总额月报表";
        String concat = year.concat("-").concat(month);
        String str = typeName + concat.concat("-01") + "至" + concat.concat("-").concat(String.valueOf(monthDay)) + ".xls";
        String path = request.getSession().getServletContext().getRealPath(str);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String beginTime = concat.concat("-01");
        String endTime = concat.concat("-").concat(String.valueOf(monthDay));
        try {
            List<ShopDetail> shopDetails = getCurrentShopDetails();
            Map<String, List<ShopIncomeDto>> result = new HashMap<>();
            Map<String, Object> selectMap = new HashMap<>();
            selectMap.put("beginDate", beginTime);
            selectMap.put("endDate", endTime + " 23:59:59");
            //用来接收分段查询出来的订单金额信息
            List<ShopIncomeDto> shopIncomeDtosItem = new ArrayList<>();
            shopIncomeDtosItem.add(new ShopIncomeDto());
            //用来累加分段查询出来的订单金额信息
            List<ShopIncomeDto> shopIncomeDtosItems = new ArrayList<>();
            //用来接收分段查询出来的订单支付项信息
            List<OrderPaymentItem> shopIncomeDtosPayMent = new ArrayList<>();
            shopIncomeDtosPayMent.add(new OrderPaymentItem());
            //用来累加分段查询出来的订单支付项信息
            List<OrderPaymentItem> shopIncomeDtosPayMents = new ArrayList<>();
            for (int pageNo = 0; shopIncomeDtosItem != null && !shopIncomeDtosItem.isEmpty(); pageNo++) {
                selectMap.put("pageNo", pageNo * 1000);
                shopIncomeDtosItem = orderService.callProcDayAllOrderItem(selectMap);
                shopIncomeDtosItems.addAll(shopIncomeDtosItem);
            }
            for (int pageNo = 0; shopIncomeDtosPayMent != null && !shopIncomeDtosPayMent.isEmpty(); pageNo++) {
                selectMap.put("pageNoTwo", pageNo * 1000);
                shopIncomeDtosPayMent = orderService.selectBusinessReport(selectMap);
                shopIncomeDtosPayMents.addAll(shopIncomeDtosPayMent);
            }

            if (type.equals(Common.YES)) {
                for (ShopDetail shopDetail : shopDetails) {
                    List<ShopIncomeDto> shopIncomeDtos = new ArrayList<>();
                    for (int day = 1; day <= monthDay; day++) {
                        LocalDate nowDay = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), day);
                        ShopIncomeDto shopIncomeDto = new ShopIncomeDto(format.format(nowDay), shopDetail.getName(), shopDetail.getId());
                        //筛选出当前店铺当前日期下的订单
                        List<ShopIncomeDto> shopIncomeDtoList = shopIncomeDtosItems.stream().filter(shopOrder -> shopOrder.getShopDetailId().equalsIgnoreCase(shopDetail.getId())
                                && shopOrder.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(nowDay)).collect(Collectors.toList());
                        //累加店铺订单总额、原价金额
                        setShopIncomeDtoItem(shopIncomeDto, shopIncomeDtoList);
                        //筛选出当前店铺当前时间下的支付项信息
                        List<OrderPaymentItem> shopIncomePayMentList = shopIncomeDtosPayMents.stream().filter(shopPayMentOrder -> shopPayMentOrder.getShopDetailId().equalsIgnoreCase(shopDetail.getId())
                                && shopPayMentOrder.getPayTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(nowDay)).collect(Collectors.toList());
                        //累加得到店铺各个支付项的值
                        setShopIncomeDtoPayMent(shopIncomeDto, shopIncomePayMentList);
                        shopIncomeDtos.add(shopIncomeDto);
                    }
                    result.put(shopDetail.getName(), shopIncomeDtos);
                }
            } else {
                String shopName = getCurrentShopDetails().stream().map(ShopDetail::getName).collect(Collectors.joining(","));
                List<ShopIncomeDto> shopIncomeDtos = new ArrayList<>();
                for (int day = 1; day <= monthDay; day++) {
                    LocalDate nowDay = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), day);
                    ShopIncomeDto shopIncomeDto = new ShopIncomeDto(format.format(nowDay), getBrandName(), getCurrentBrandId());
                    //筛选出当前店铺当前日期下的订单
                    List<ShopIncomeDto> shopIncomeDtoList = shopIncomeDtosItems.stream().filter(shopOrder -> shopOrder.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(nowDay))
                            .collect(Collectors.toList());
                    //累加店铺订单总额、原价金额
                    setShopIncomeDtoItem(shopIncomeDto, shopIncomeDtoList);
                    //筛选出当前店铺当前时间下的支付项信息
                    List<OrderPaymentItem> shopIncomePayMentList = shopIncomeDtosPayMents.stream().filter(shopPayMentOrder -> shopPayMentOrder.getPayTime().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate().equals(nowDay)).collect(Collectors.toList());
                    //累加得到店铺各个支付项的值
                    setShopIncomeDtoPayMent(shopIncomeDto, shopIncomePayMentList);
                    shopIncomeDtos.add(shopIncomeDto);
                }
                result.put(shopName, shopIncomeDtos);
            }
            Map<String, Object> map = new HashMap<String, Object>(){{
                put("brandName", getBrandName());
                put("beginDate", concat.concat("-01"));
                put("reportType", typeName);// 表的头，第一行内容
                put("endDate", concat.concat("-").concat(String.valueOf(monthDay)));
                put("num", "34");// 显示的位置
            }};
            ExcelUtil<ShopIncomeDto> excelUtil = new ExcelUtil<>();
            OutputStream out = new FileOutputStream(path);
            excelUtil.NewCreateMonthDtoExcel(result, out, map);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成月营业报表出错！");
            return new Result(false);
        }
        return getSuccessResult(path);
    }
}