package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 2671325482059322735L;

    private String id;

    private String articleName;

    private String articleDesignation;

    private Integer count;

    private BigDecimal originalPrice;

    private BigDecimal unitPrice;

    private BigDecimal baseUnitPrice;

    private BigDecimal finalPrice;

    private String remark;

    private String posDiscount;

    private Integer sort;

    private Integer status;

    private String orderId;

    private String articleId;

    private Integer type;

    private Integer[] mealItems;

    private Integer[] recommendList;

    private String parentId;

    private Date createTime;

    private List<OrderItem> children;

    private Integer articleSum;

    //关联菜品类别
    private ArticleFamily articleFamily;

    //关联店铺ID 用于中间数据库 报表问题
    private String shopId;
    //关联   用户电话 用于中间数据库 报表问题
    private String telephone;

    private String name;

    private BigDecimal price;

    private Integer mealItemId;

    private String recommendId;

    //退菜数量
    private Integer refundCount;

    //原始购买的菜品数量
    private Integer orginCount;

    //餐盒数量
    private Integer mealFeeNumber;

    private BigDecimal extraPrice;

    private Integer changeCount;

    public Integer getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }

    /**
     * 此订单项，在前端传入的折扣百分比，用于在后台创建订单时做折扣值对比，判断订单是否可创建。
     */
    private Integer discount;

    //0-未打印 1-打印异常 2-异常修正 3打印正常
    private Integer printFailFlag;

    private Integer peference;

    private OrderRefundRemark orderRefundRemark;

}