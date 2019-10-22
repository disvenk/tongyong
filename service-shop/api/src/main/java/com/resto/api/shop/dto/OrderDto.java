package com.resto.api.shop.dto;

import com.resto.api.brand.dto.CouponDto;
import com.resto.api.brand.dto.RefundRemarkDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class OrderDto implements Serializable{

    private static final long serialVersionUID = -4217280413371133624L;

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

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String operatorId;

    private String customerId;

    private String customerAddressId;

    private Date distributionDate;

    private Integer distributionTimeId;

    private Integer deliveryPointId;

    private String shopDetailId;

    private Integer distributionModeId;

    private List<OrderItemDto> orderItems;

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
    private CustomerDto customer;

    //评价
    private AppraiseDto appraise;

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

    private  String shareCustomer;

    //该订单是否使用优惠券
    private Boolean ifUseCoupon;

    //该订单的优惠券信息
    private CouponDto couponInfo;

    private  List<OrderDto> childList; //子订单

    //新增微信支付单号
    public OrderPaymentItemDto orderPaymentItem;

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
    private Map<String,List<OrderItemDto>> childItems;

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
    private List<OrderPaymentItemDto> orderPaymentItems;

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
    private RefundRemarkDto refundRemark;

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

}