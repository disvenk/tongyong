package com.resto.api.customer.entity;

import com.resto.conf.db.BaseEntityResto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_customer_detail")
public class CustomerDetail extends BaseEntityResto implements Serializable {
    @Column(name = "birth_date")
    private Date birthDate;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("星座")
    private String constellation;

    @ApiModelProperty("职业")
    private String vocation;

    @ApiModelProperty("公司")
    private String company;

    @ApiModelProperty("学校")
    private String school;

    @ApiModelProperty("个人说明")
    @Column(name = "personal_note")
    private String personalNote;

    @ApiModelProperty("录入生日信息时所在店铺")
    @Column(name = "shop_detail_id")
    private String shopDetailId;

    private static final long serialVersionUID = 1L;
}