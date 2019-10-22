package com.resto.api.customer.entity;

import com.resto.conf.db.BaseEntityResto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_customer_address")
public class CustomerAddress extends BaseEntityResto implements Serializable {
    @ApiModelProperty("联系人")
    private String name;

    @ApiModelProperty("性别,0女1男2保密")
    private Integer sex;

    @ApiModelProperty("手机号")
    @Column(name = "mobile_no")
    private String mobileNo;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("具体配送地址")
    @Column(name = "address_reality")
    private String addressReality;

    private String label;

    @ApiModelProperty("是否默认,1默认，否则不默认")
    private Integer state;

    @ApiModelProperty("会员id")
    @Column(name = "customer_id")
    private String customerId;

    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @Column(name = "update_time")
    private Date updateTime;

    @ApiModelProperty("经度")
    private Double longitude;

    @ApiModelProperty("纬度")
    private Double latitude;

    private static final long serialVersionUID = 1L;
}