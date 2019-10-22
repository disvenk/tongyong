package com.resto.shop.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.resto.brand.web.model.RefundRemark;
import com.resto.shop.web.posDto.OrderDto;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;


@JsonInclude(Include.NON_NULL)
@Data
public class Order implements Serializable {

    public Order(OrderDto orderDto) {
        this.id = orderDto.getId() == null ? "" : orderDto.getId();
        this.tableNumber = orderDto.getTableNumber() == null ? "" : orderDto.getTableNumber();
        this.customerCount = orderDto.getCustomerCount() == null ? 0 : orderDto.getCustomerCount();
        this.accountingTime = orderDto.getAccountingTime() == null ? new Date() : orderDto.getAccountingTime();
        this.orderState = orderDto.getOrderState() == null ? 0 : orderDto.getOrderState();
        this.productionStatus = orderDto.getProductionStatus() == null ? 0 : orderDto.getProductionStatus();
        this.originalAmount = orderDto.getOriginalAmount() == null ? BigDecimal.valueOf(0) : orderDto.getOriginalAmount();
        this.orderMoney = orderDto.getOrderMoney() == null ? BigDecimal.valueOf(0) : orderDto.getOrderMoney();
        this.articleCount = orderDto.getArticleCount() == null ? 0 : orderDto.getArticleCount();
        this.serialNumber = orderDto.getSerialNumber() == null ? "" : orderDto.getSerialNumber();
        this.allowCancel = orderDto.getAllowCancel() == null ? false : orderDto.getAllowCancel() > 0;
        this.closed = orderDto.getClosed() == null ? false : orderDto.getClosed() > 0;
        this.createTime = orderDto.getCreateTime() == null ? new Date() : new Date(orderDto.getCreateTime());
        this.pushOrderTime = orderDto.getPushOrderTime() == null ? new Date() : new Date(orderDto.getPushOrderTime());
        this.printOrderTime = orderDto.getPrintOrderTime() == null ? new Date() : new Date(orderDto.getPrintOrderTime());
        this.remark = orderDto.getRemark() == null ? "" : orderDto.getRemark();
        this.distributionModeId = orderDto.getDistributionModeId() == null ? 0 : orderDto.getDistributionModeId();
        this.amountWithChildren = orderDto.getAmountWithChildren() == null ? BigDecimal.valueOf(0) : orderDto.getAmountWithChildren();
        this.parentOrderId = StringUtils.isEmpty(orderDto.getParentOrderId())  ? null : orderDto.getParentOrderId();
        this.servicePrice = orderDto.getServicePrice() == null ? BigDecimal.valueOf(0) : orderDto.getServicePrice();
        this.shopDetailId = orderDto.getShopDetailId() == null ? "" : orderDto.getShopDetailId();
        this.payType = orderDto.getPayType() == null ? 0 : orderDto.getPayType();
        this.countWithChild = orderDto.getCountWithChild() == null ? 0 : orderDto.getCountWithChild();
        this.allowContinueOrder = orderDto.getAllowContinueOrder() == null ? false : orderDto.getAllowContinueOrder() > 0;
        this.paymentAmount = orderDto.getPaymentAmount() == null ? BigDecimal.valueOf(0) : orderDto.getPaymentAmount();
        this.customerId = orderDto.getCustomerId() == null ? "0" : orderDto.getCustomerId();
        this.customerAddressId = StringUtils.isEmpty(orderDto.getCustomerAddressId()) ? null : orderDto.getCustomerAddressId();
        this.mealAllNumber = orderDto.getMealAllNumber() == null ? 0 : orderDto.getMealAllNumber();
        this.mealFeePrice = orderDto.getMealFeePrice() == null ? BigDecimal.valueOf(0) : orderDto.getMealFeePrice();
        this.verCode = orderDto.getVerCode() == null ? "" : orderDto.getVerCode();
        this.allowAppraise = orderDto.getAllowAppraise() == null ? false : orderDto.getAllowAppraise();
        this.reduceMoney = orderDto.getReduceMoney() == null ? BigDecimal.valueOf(0) : orderDto.getReduceMoney();
        this.realEraseMoney = orderDto.getRealEraseMoney() == null ? BigDecimal.ZERO : orderDto.getRealEraseMoney();
        this.needConfirmOrderItem = orderDto.getNeedConfirmOrderItem() == null ? 0 : orderDto.getNeedConfirmOrderItem();
        this.exemptionMoney = orderDto.getExemptionMoney() == null ? BigDecimal.valueOf(0) : orderDto.getExemptionMoney();
        this.syncState = orderDto.getSyncState();
        this.lastSyncTime = orderDto.getLastSyncTime();
        this.sauceFeeCount = orderDto.getSauceFeeCount();
        this.sauceFeePrice = orderDto.getSauceFeePrice();
        this.towelFeeCount = orderDto.getTowelFeeCount();
        this.towelFeePrice = orderDto.getTowelFeePrice();
        this.tablewareFeeCount = orderDto.getTablewareFeeCount();
        this.tablewareFeePrice = orderDto.getTablewareFeePrice();
        this.isUseNewService = orderDto.getIsUseNewService();
    }


    private Integer tag;//不去重

    private String id;

    private String tableNumber;

    private Integer customerCount;

    private Date accountingTime;

    private Integer orderState;

    private Integer productionStatus;

    private BigDecimal originalAmount;

    private BigDecimal reductionAmount;

    private BigDecimal paymentAmount;

    private BigDecimal orderMoney;

    private BigDecimal aliPayDiscountMoney;

    private Integer articleCount;

    private String serialNumber;

    private Date confirmTime;

    private Integer printTimes;

    private Boolean allowCancel;

    private Boolean allowAppraise;

    private Boolean closed;

    private String remark;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String operatorId;

    private String customerId;

    private String customerAddressId;

    private Date distributionDate;

    private Integer distributionTimeId;

    private Integer deliveryPointId;

    private String shopDetailId;

    private Integer distributionModeId;

    private List<OrderItem> orderItems;

    private String verCode;

    private boolean useAccount;

    private String useCoupon;

    private Date pushOrderTime;

    private Date printOrderTime;

    private Date callNumberTime;

    private Integer orderMode;

    private String brandId;

    private BigDecimal amountWithChildren;

    private String parentOrderId;

    private Boolean allowContinueOrder;

    private Integer countWithChild;

    private Date lastOrderTime;

    //顾客
    private Customer customer;

    //评价
    private Appraise appraise;

    private int personCount;

    private Integer payMode;

    private Long employeeId;

    /**
     * 用于保存 订单的 菜品名称（查询时使用）
     */
    private List<String> articleNames;

    /**
     * 用于保存 店铺的名称
     */
    private String shopName;

    private Boolean timeOut;

    private String telephone;

    private BigDecimal payValue;

    private int paymentModeId;

    private int orderCount;

    private BigDecimal orderTotal;

    //等位红包
    private BigDecimal waitMoney;

    private String waitId;

    private Integer isShare;

    private String shareCustomer;

    //该订单是否使用优惠券
    private Boolean ifUseCoupon;

    //该订单的优惠券信息
    private Coupon couponInfo;

    private List<Order> childList; //子订单

    //新增微信支付单号
    public OrderPaymentItem orderPaymentItem;

    //菜品总数量（包含加菜）
    private Integer totalCount;

    //加菜次数
    private int childCount;

    //订单原始金额（退菜 编辑菜 前）
    private BigDecimal baseMoney;

    private BigDecimal baseOrderMoney;

    //支付状态（0 未支付 1支付中 2已支付）
    private Integer isPay;

    //子订单的菜品项 ，key为子订单id
    private Map<String, List<OrderItem>> childItems;

    private BigDecimal refundMoney;

    //订单原始人数（退菜前）
    private Integer baseCustomerCount;

    //是否需要扫码 0不需要 1需要
    private Integer needScan;

    private Integer payType;

    //是否是退菜订单 0 不是 1是
    private Integer isRefund;

    //是否确认 0-未确认 1-已确认
    private Integer isConfirm;

    //订单及他所有加菜订单的原价总和
    private BigDecimal allOrderOriginalAmount;

    //加菜位置  pos为pos端点菜   wechat为wehcat端点菜
    private String createOrderByAddress;

    //是否退菜光 包括自订单   默认否
    private Boolean isRefundOrder;

    private Integer isGetShareCoupon;

    //找零
    private BigDecimal giveChange;

    private Integer isPosPay;

    //0-未打印 1-打印异常 2-异常修正 3打印正常
    private Integer printFailFlag;

    //0-未打印 1-打印异常 2-异常修正 3打印正常
    private Integer printKitchenFlag;

    private Integer failPrintCount;

    /**
     * 订单支付项
     */
    private List<OrderPaymentItem> orderPaymentItems;

    private BigDecimal servicePrice;

    private BigDecimal mealFeePrice;

    private Integer mealAllNumber;

    /**
     * 原始餐盒总数（退菜前）
     */
    private Integer baseMealAllCount;

    /**
     * 退菜原因
     */
    private RefundRemark refundRemark;

    /**
     * 退菜原因补充
     */
    private String remarkSupply;

    private Integer type;

    private BigDecimal posDiscount;

    private BigDecimal eraseMoney;

    private BigDecimal noDiscountMoney;

    private Integer refundType;

    private Integer isConsumptionRebate;    //是否参与消费返利

    private Integer orderBefore;

    private String beforeId;

    //菜品库新增字段
    private Boolean openArticleLibrary;

    // 订单退款备注
    private List<OrderRefundRemark> orderRefundRemarks;
    // Pos 2.0 字段，用于备份原始订单
    private String posBackUps;

    //新版服务费的集合 包括：餐具费、纸巾费、酱料费
    private List<JSONObject> serviceList;

    private String groupId;

    private Integer sauceFeeCount;

    private BigDecimal sauceFeePrice;

    private Integer towelFeeCount;

    private BigDecimal towelFeePrice;

    private Integer tablewareFeeCount;

    private BigDecimal tablewareFeePrice;

    private Integer isUseNewService;

    private Integer dataOrigin;

    private BigDecimal orderPosDiscountMoney;

    private BigDecimal memberDiscountMoney;

    private BigDecimal memberDiscount;

    private Integer needConfirmOrderItem;

    private BigDecimal reduceMoney;

    private BigDecimal realEraseMoney;

    private BigDecimal exemptionMoney;

    private Integer syncState;

    private String lastSyncTime;

    private BigDecimal discountMoney;

    //pos退菜类型  有值则为pos2.0退菜
    private Integer posRefundArticleType;

    private List<OrderPaymentItem> refundPaymentList;

    //标识当前订单是否同步成功，需NewPos端根据该属性进行判断
    private Boolean synSuccess;

    //当前订单的子订单合集
    private List<Order> suborders;

    //是否使用产品券  默认0未使用   1使用
    private Integer useProductCoupon;

    //产品券菜品id
    private String productCouponArticleId;

    //产品券核销金额
    private BigDecimal productCouponMoney;

    private BigDecimal grantMoney;

    //借助order对象
    private String refundSourceId;

    public Order() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tag", tag)
                .append("id", id)
                .append("tableNumber", tableNumber)
                .append("customerCount", customerCount)
                .append("accountingTime", accountingTime)
                .append("orderState", orderState)
                .append("productionStatus", productionStatus)
                .append("originalAmount", originalAmount)
                .append("reductionAmount", reductionAmount)
                .append("paymentAmount", paymentAmount)
                .append("orderMoney", orderMoney)
                .append("aliPayDiscountMoney", aliPayDiscountMoney)
                .append("articleCount", articleCount)
                .append("serialNumber", serialNumber)
                .append("confirmTime", confirmTime)
                .append("printTimes", printTimes)
                .append("allowCancel", allowCancel)
                .append("allowAppraise", allowAppraise)
                .append("closed", closed)
                .append("remark", remark)
                .append("createTime", createTime)
                .append("operatorId", operatorId)
                .append("customerId", customerId)
                .append("distributionDate", distributionDate)
                .append("distributionTimeId", distributionTimeId)
                .append("deliveryPointId", deliveryPointId)
                .append("shopDetailId", shopDetailId)
                .append("distributionModeId", distributionModeId)
                .append("orderItems", orderItems)
                .append("verCode", verCode)
                .append("useAccount", useAccount)
                .append("useCoupon", useCoupon)
                .append("pushOrderTime", pushOrderTime)
                .append("printOrderTime", printOrderTime)
                .append("callNumberTime", callNumberTime)
                .append("orderMode", orderMode)
                .append("brandId", brandId)
                .append("amountWithChildren", amountWithChildren)
                .append("parentOrderId", parentOrderId)
                .append("allowContinueOrder", allowContinueOrder)
                .append("countWithChild", countWithChild)
                .append("lastOrderTime", lastOrderTime)
                .append("customer", customer)
                .append("appraise", appraise)
                .append("personCount", personCount)
                .append("payMode", payMode)
                .append("employeeId", employeeId)
                .append("articleNames", articleNames)
                .append("shopName", shopName)
                .append("timeOut", timeOut)
                .append("telephone", telephone)
                .append("payValue", payValue)
                .append("paymentModeId", paymentModeId)
                .append("orderCount", orderCount)
                .append("orderTotal", orderTotal)
                .append("waitMoney", waitMoney)
                .append("waitId", waitId)
                .append("isShare", isShare)
                .append("shareCustomer", shareCustomer)
                .append("ifUseCoupon", ifUseCoupon)
                .append("couponInfo", couponInfo)
                .append("childList", childList)
                .append("orderPaymentItem", orderPaymentItem)
                .append("totalCount", totalCount)
                .append("childCount", childCount)
                .append("baseMoney", baseMoney)
                .append("isPay", isPay)
                .append("childItems", childItems)
                .append("refundMoney", refundMoney)
                .append("baseCustomerCount", baseCustomerCount)
                .append("needScan", needScan)
                .append("payType", payType)
                .append("isRefund", isRefund)
                .append("isConfirm", isConfirm)
                .append("allOrderOriginalAmount", allOrderOriginalAmount)
                .append("createOrderByAddress", createOrderByAddress)
                .append("isRefundOrder", isRefundOrder)
                .append("baseMealAllCount", baseMealAllCount)
                .append("mealFeePrice", mealFeePrice)
                .append("mealAllNumber", mealAllNumber)
                .append("orderPaymentItems", orderPaymentItems)
                .append("servicePrice", servicePrice)
                .toString();
    }
}