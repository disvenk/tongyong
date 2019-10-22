package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.resto.shop.web.posDto.OrderItemDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.resto.brand.core.util.DateUtil;

@JsonInclude(Include.NON_EMPTY)
@Data
public class OrderItem implements Serializable {

    public OrderItem(){}
    public OrderItem(OrderItemDto orderItem){
        this.status = orderItem.getStatus() == null ? 0 : orderItem.getStatus();
        this.id = orderItem.getId() == null ? "" : orderItem.getId();
        this.createTime = new Date(orderItem.getCreateTime());
        this.orginCount = orderItem.getOrginCount() == null ? 0 : orderItem.getOrginCount();
        this.mealFeeNumber = orderItem.getMealFeeNumber() == null ? 0 : orderItem.getMealFeeNumber();
        this.type = orderItem.getType() == null ? 0 : orderItem.getType();
        this.sort = orderItem.getSort() == null ? 0 : orderItem.getSort();
        this.remark = orderItem.getRemark() == null ? "" : orderItem.getRemark();
        this.changeCount = orderItem.getChangeCount() == null ? 0 : orderItem.getChangeCount();
        this.finalPrice = orderItem.getFinalPrice() == null ? BigDecimal.valueOf(0) : orderItem.getFinalPrice();
        this.articleId = orderItem.getArticleId() == null ? "" : orderItem.getArticleId();
        this.count = orderItem.getCount() == null ? 0 : orderItem.getCount();
        this.orderId = orderItem.getOrderId() == null ? "" : orderItem.getOrderId();
        this.originalPrice = orderItem.getOriginalPrice() == null ? BigDecimal.valueOf(0) : orderItem.getOriginalPrice();
        this.articleName = orderItem.getArticleName() == null ? "" : orderItem.getArticleName();
        this.refundCount = orderItem.getRefundCount() == null ? 0 : orderItem.getRefundCount();
        this.unitPrice = orderItem.getUnitPrice() == null ? BigDecimal.valueOf(0) : orderItem.getUnitPrice();
        this.printFailFlag = orderItem.getPrintFailFlag() == null ? 0 : orderItem.getPrintFailFlag();
        this.recommendId = orderItem.getRecommendId() == null ? "" : orderItem.getRecommendId();
        this.parentId = orderItem.getParentId() == null ? "" : orderItem.getParentId();
        this.mealItemId = orderItem.getMealItemId() == null ? 0 : orderItem.getMealItemId();
        this.needRemind = orderItem.getNeedRemind() == null ? 0 : orderItem.getNeedRemind();
        this.grantCount = orderItem.getGrantCount() == null ? 0 : orderItem.getGrantCount();
    }


    private String id;

    private String articleName;

    private String articleDesignation;

    private Integer count;

    private Integer grantCount;

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

    private String customerId;

    /**
     * 此订单项，在前端传入的折扣百分比，用于在后台创建订单时做折扣值对比，判断订单是否可创建。
     */
    private Integer discount;

    //0-未打印 1-打印异常 2-异常修正 3打印正常
    private Integer printFailFlag;

    private Integer peference;

    private String verCode;

    private BigDecimal weight;

    private Integer needRemind;

    private Integer packageNumber;

    private OrderRefundRemark orderRefundRemark;

    private Boolean isAssignment;

    public Date getCreateTime(Boolean date) {
        return this.createTime;
    }


    @Override
    public String toString() {
        return "OrderItem{" +
                "id='" + id + '\'' +
                ", articleName='" + articleName + '\'' +
                ", articleDesignation='" + articleDesignation + '\'' +
                ", count=" + count +
                ", originalPrice=" + originalPrice +
                ", unitPrice=" + unitPrice +
                ", baseUnitPrice=" + baseUnitPrice +
                ", finalPrice=" + finalPrice +
                ", remark='" + remark + '\'' +
                ", posDiscount='" + posDiscount + '\'' +
                ", sort=" + sort +
                ", status=" + status +
                ", orderId='" + orderId + '\'' +
                ", articleId='" + articleId + '\'' +
                ", type=" + type +
                ", mealItems=" + Arrays.toString(mealItems) +
                ", recommendList=" + Arrays.toString(recommendList) +
                ", parentId='" + parentId + '\'' +
                ", createTime=" + createTime +
                ", children=" + children +
                ", articleSum=" + articleSum +
                ", articleFamily=" + articleFamily +
                ", shopId='" + shopId + '\'' +
                ", telephone='" + telephone + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", mealItemId=" + mealItemId +
                ", recommendId='" + recommendId + '\'' +
                ", refundCount=" + refundCount +
                ", orginCount=" + orginCount +
                ", mealFeeNumber=" + mealFeeNumber +
                ", extraPrice=" + extraPrice +
                ", changeCount=" + changeCount +
                ", discount=" + discount +
                ", printFailFlag=" + printFailFlag +
                ", peference=" + peference +
                ", packageNumber=" + packageNumber +
                ", orderRefundRemark=" + orderRefundRemark +
                ", needRemind=" + needRemind +
                '}';
    }
}