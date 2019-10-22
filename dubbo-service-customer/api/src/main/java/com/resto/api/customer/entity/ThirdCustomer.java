package com.resto.api.customer.entity;

import com.resto.conf.db.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Table(name = "tb_third_customer")
public class ThirdCustomer extends BaseEntity implements Serializable {

    private String telephone;

    private BigDecimal money;

    @ApiModelProperty("领取人id")
    @Column(name = "customer_id")
    private String customerId;

    @ApiModelProperty("默认1  未使用   0表示已经被领取")
    private Integer type;

    private String remark;

    private static final long serialVersionUID = 1L;
}