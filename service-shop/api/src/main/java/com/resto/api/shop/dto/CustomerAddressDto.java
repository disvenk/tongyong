package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CustomerAddressDto implements Serializable {

    private static final long serialVersionUID = -9064034895515236465L;

    @ApiModelProperty(value = "配送地址id,主键")
    private String id;

    @ApiModelProperty(value = "联系人")
    private String name;

    @ApiModelProperty(value = "性别,0女1男2保密")
    private Integer sex;

    @ApiModelProperty(value = "手机号")
    private String mobileNo;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "具体配送地址")
    private String addressReality;

    private String label;

    @ApiModelProperty(value = "是否默认,1默认，否则不默认")
    private Integer state;

    @ApiModelProperty(value = "会员id")
    private String customerId;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}