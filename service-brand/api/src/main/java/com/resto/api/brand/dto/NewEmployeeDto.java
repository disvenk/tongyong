package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewEmployeeDto implements Serializable {

    private static final long serialVersionUID = 1348628309576484986L;

    private String id;

    @ApiModelProperty(value = "员工类型(1：员工   2：店长)")
    private Integer roleType;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "微信昵称")
    private String nickName;

    @ApiModelProperty(value = "微信头像")
    private String  wechatAvatar;

    @ApiModelProperty(value = "性别(1：男   2：女)")
    private Integer sex;

    @ApiModelProperty(value = "手机号码")
    private String telephone;

    @ApiModelProperty(value = "店铺Id")
    private String shopDetailId;

    @ApiModelProperty(value = "品牌Id")
    private String brandId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "状态(1：启用   0：不启用)")
    private Integer state;

    private String shopName;

    private String roleValue;

    private String sexValue;

    private String stateValue;

}