package com.resto.shop.web.posDto;

import com.resto.shop.web.model.OrderItem;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by KONATA on 2017/8/16.
 */
@Data
public class OrderItemDto implements Serializable {
    private static final long serialVersionUID = -1364031233735055169L;

    public OrderItemDto() {
    }

    public OrderItemDto(OrderItem orderItem) {
        this.status = orderItem.getStatus() == null ? 0 : orderItem.getStatus();
        this.id = orderItem.getId() == null ? "" : orderItem.getId();
        this.createTime = orderItem.getCreateTime(true).getTime();
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
        this.kitchenId = "";
        this.parentId = orderItem.getParentId() == null ? "" : orderItem.getParentId();
        this.mealItemId = orderItem.getMealItemId();
        this.weight = orderItem.getWeight();
        this.needRemind = orderItem.getNeedRemind();
        this.grantCount = orderItem.getGrantCount();
    }

    //状态
    private Integer status;
    //主键
    private String id;
    //创建时间
    private Long createTime;
    //原始数量
    private Integer orginCount;
    //餐盒数量
    private Integer mealFeeNumber;
    //类型
    private Integer type;
    //排序
    private Integer sort;
    //折扣
    private String remark;
    //修改的数量
    private Integer changeCount;
    //最终金额
    private BigDecimal finalPrice;
    //菜品id
    private String articleId;
    //当前数量
    private Integer count;
    //赠菜数量
    private Integer grantCount;
    //订单id
    private String orderId;
    //原价
    private BigDecimal originalPrice;
    //菜品名称
    private String articleName;
    //退菜数量
    private Integer refundCount;
    //单价
    private BigDecimal unitPrice;
    //打印标记
    private Integer printFailFlag;
    //推荐菜品id
    private String recommendId;
    //厨房id
    private String kitchenId;
    //父id
    private String parentId;
    //套餐子项id
    private Integer mealItemId;

    private BigDecimal weight;

    private Integer needRemind;

    @Override
    public String toString() {
        return "OrderItemDto{" +
                "status=" + status +
                ", id='" + id + '\'' +
                ", createTime=" + createTime +
                ", orginCount=" + orginCount +
                ", mealFeeNumber=" + mealFeeNumber +
                ", type=" + type +
                ", sort=" + sort +
                ", remark='" + remark + '\'' +
                ", changeCount=" + changeCount +
                ", finalPrice=" + finalPrice +
                ", articleId='" + articleId + '\'' +
                ", count=" + count +
                ", orderId='" + orderId + '\'' +
                ", originalPrice=" + originalPrice +
                ", articleName='" + articleName + '\'' +
                ", refundCount=" + refundCount +
                ", unitPrice=" + unitPrice +
                ", printFailFlag=" + printFailFlag +
                ", recommendId='" + recommendId + '\'' +
                ", kitchenId='" + kitchenId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", mealItemId=" + mealItemId +
                ", needRemind=" + needRemind +
                '}';
    }
}
