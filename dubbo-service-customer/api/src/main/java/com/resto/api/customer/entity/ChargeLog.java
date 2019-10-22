package com.resto.api.customer.entity;

import com.resto.conf.db.BaseEntityResto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "tb_charge_log")
public class ChargeLog extends BaseEntityResto implements Serializable {
    @ApiModelProperty("操作人手机号")
    @Column(name = "operation_phone")
    private String operationPhone;

    @ApiModelProperty("充值的用户手机号")
    @Column(name = "customer_phone")
    private String customerPhone;

    @ApiModelProperty("店铺Id")
    @Column(name = "shop_detail_id")
    private String shopDetailId;

    @ApiModelProperty("店铺名称")
    @Column(name = "shop_name")
    private String shopName;

    @ApiModelProperty("充值金额")
    @Column(name = "charge_money")
    private BigDecimal chargeMoney;

    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("charge_order外键")
    @Column(name = "charge_order_id")
    private String chargeOrderId;

    private static final long serialVersionUID = 1L;
}