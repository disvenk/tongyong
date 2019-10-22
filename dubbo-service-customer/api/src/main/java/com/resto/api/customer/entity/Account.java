package com.resto.api.customer.entity;

import com.resto.conf.db.BaseEntityResto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Table(name = "tb_account")
public class Account extends BaseEntityResto implements Serializable {
    @ApiModelProperty("账户余额")
    private BigDecimal remain;

    @ApiModelProperty("账户状态")
    private Byte status;

    @ApiModelProperty("冻结余额")
    @Column(name = "frozen_remain")
    private BigDecimal frozenRemain;

    /**
     * 用于保存 用户的交易明细
     */
    @Transient
    List<AccountLog> accountLogs;

    private static final long serialVersionUID = 1L;
}