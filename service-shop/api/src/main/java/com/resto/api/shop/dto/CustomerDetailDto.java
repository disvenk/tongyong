package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CustomerDetailDto implements Serializable {

    private static final long serialVersionUID = 5075499590209873444L;

    @ApiModelProperty(value = "会员详情id")
    private String id;

    @ApiModelProperty(value = "生日")
    private Date birthDate;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "星座")
    private String constellation;

    @ApiModelProperty(value = "职业")
    private String vocation;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "学校")
    private String school;

    @ApiModelProperty(value = "个人说明")
    private String personalNote;

    @ApiModelProperty(value = "录入生日信息时所在店铺")
    private String shopDetailId;

}
