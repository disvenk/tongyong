package com.resto.shop.web.posDto;

import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.OrderRefundRemark;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by KONATA on 2017/8/11.
 */
@Data
public class OrderDto implements Serializable {
    private static final long serialVersionUID = -343174307546088227L;

    public OrderDto() {
    }

    public OrderDto(Order order) {
        this.id = order.getId() == null ? "" : order.getId();
        this.tableNumber = order.getTableNumber() == null ? "" : order.getTableNumber();
        this.customerCount = order.getCustomerCount() == null ? 0 : order.getCustomerCount();
        this.accountingTime = order.getAccountingTime() == null ? new Date() : order.getAccountingTime();
        this.orderState = order.getOrderState() == null ? 0 : order.getOrderState();
        this.productionStatus = order.getProductionStatus() == 0 ? 1 : order.getProductionStatus();
        this.originalAmount = order.getOriginalAmount() == null ? BigDecimal.valueOf(0) : order.getOriginalAmount();
        this.orderMoney = order.getOrderMoney() == null ? BigDecimal.valueOf(0) : order.getOrderMoney();
        this.articleCount = order.getArticleCount() == null ? 0 : order.getArticleCount();
        this.serialNumber = order.getSerialNumber() == null ? "" : order.getSerialNumber();
        this.allowCancel = order.getAllowCancel() == null ? 0 : order.getAllowCancel() ? 1 : 0;
        this.closed = order.getClosed() == null ? 0 : order.getClosed() ? 1 : 0;
        this.createTime = order.getCreateTime() == null ? new Date().getTime() : order.getCreateTime().getTime();
        this.pushOrderTime = order.getPushOrderTime() == null ? new Date().getTime() : order.getPushOrderTime().getTime();
        this.printOrderTime = order.getPrintOrderTime() == null ? new Date().getTime() : order.getPrintOrderTime().getTime();
        this.remark = order.getRemark() == null ? "" : order.getRemark();
        this.distributionModeId = order.getDistributionModeId() == null ? 0 : order.getDistributionModeId();
        this.amountWithChildren = order.getAmountWithChildren() == null ? BigDecimal.valueOf(0) : order.getAmountWithChildren();
        this.parentOrderId = order.getParentOrderId() == null ? "" : order.getParentOrderId();
        this.servicePrice = order.getServicePrice() == null ? BigDecimal.valueOf(0) : order.getServicePrice();
        this.shopDetailId = order.getShopDetailId() == null ? "" : order.getShopDetailId();
        this.payType = order.getPayType() == null ? 0 : order.getPayType();
        this.countWithChild = order.getCountWithChild() == null ? 0 : order.getCountWithChild();
        this.allowContinueOrder = order.getAllowContinueOrder() == null ? 0 : order.getAllowContinueOrder() ? 1 : 0;
        this.paymentAmount = order.getPaymentAmount() == null ? BigDecimal.valueOf(0) : order.getPaymentAmount();
        this.customerId = order.getCustomerId() == null ? "0" : order.getCustomerId();
        this.customerAddressId = order.getCustomerAddressId() == null ? "" : order.getCustomerAddressId();
        this.verCode = order.getVerCode() == null ? "" : order.getVerCode();
        this.payMode = order.getPayMode();
        this.mealAllNumber = order.getMealAllNumber();
        this.mealFeePrice = order.getMealFeePrice();
        this.isPosPay = order.getIsPosPay();
        this.allowAppraise = order.getAllowAppraise();
        this.needConfirmOrderItem = order.getNeedConfirmOrderItem();
        this.reduceMoney = order.getReduceMoney();
        this.realEraseMoney = order.getRealEraseMoney();
        this.exemptionMoney = order.getExemptionMoney();
        this.sauceFeeCount = order.getSauceFeeCount();
        this.sauceFeePrice = order.getSauceFeePrice();
        this.towelFeeCount = order.getTowelFeeCount();
        this.towelFeePrice = order.getTowelFeePrice();
        this.tablewareFeeCount = order.getTablewareFeeCount();
        this.tablewareFeePrice = order.getTablewareFeePrice();
        this.isUseNewService = order.getIsUseNewService();
    }

    //订单id
    private String id;
    //桌号
    private String tableNumber;
    //人数
    private Integer customerCount;
    //订单创建日期
    private Date accountingTime;
    //订单状态
    private Integer orderState;
    //生产状态
    private Integer productionStatus;
    //订单原价
    private BigDecimal originalAmount;
    //单比订单总金额
    private BigDecimal orderMoney;
    //单比订单菜品总数量
    private Integer articleCount;
    //订单序列号
    private String serialNumber;
    //是否允许取消订单
    private Integer allowCancel;
    //订单是否结束流程
    private Integer closed;
    //订单创建时间
    private Long createTime;
    //订单推送时间
    private Long pushOrderTime;
    //订单打印时间
    private Long printOrderTime;
    //备注
    private String remark;
    //订单类型 1堂吃 2 外卖 3 外带
    private Integer distributionModeId;
    //加菜后主订单的订单总金额
    private BigDecimal amountWithChildren;
    //父订单id
    private String parentOrderId;
    //服务费
    private BigDecimal servicePrice;
    //店铺id
    private String shopDetailId;
    //支付类型
    private Integer payType;
    //加菜后主订单的菜品总数量
    private Integer countWithChild;
    //是否允许加菜
    private Integer allowContinueOrder;
    //应付金额（扣除优惠券+余额）
    private BigDecimal paymentAmount;
    //用户id
    private String customerId;
    //是否是Pos支付
    private Integer isPosPay;

    private String customerAddressId;

    private List<OrderItemDto> orderItem;

    private List<OrderPaymentDto> orderPayment;

    private String verCode;

    private Integer payMode;

    private Integer mealAllNumber;

    private BigDecimal mealFeePrice;

    private Boolean allowAppraise;

    private List<OrderDto> childrenOrders;

    private List<OrderRefundRemark> orderRefundRemarks;

    private Integer dataOrigin;

    private Integer needConfirmOrderItem;

    private BigDecimal reduceMoney;

    private BigDecimal realEraseMoney;

    private BigDecimal exemptionMoney;

    private Integer syncState;

    private String lastSyncTime;

    private Integer sauceFeeCount;

    private BigDecimal sauceFeePrice;

    private Integer towelFeeCount;

    private BigDecimal towelFeePrice;

    private Integer tablewareFeeCount;

    private BigDecimal tablewareFeePrice;

    private Integer isUseNewService;
}
