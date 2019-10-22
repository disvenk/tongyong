package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by carl on 2016/10/14.
 */
@Data
public class GetNumberDto implements Serializable {

    private static final long serialVersionUID = -5645463672026459317L;

    @ApiModelProperty(value = "取号id")
    private String id;

    @ApiModelProperty(value = "店铺Id")
    private String shopDetailId;

    @ApiModelProperty(value = "品牌Id")
    private String brandId;

    @ApiModelProperty(value = "状态（0 等待  1 就餐  2 过号）")
    private Integer state;

    @ApiModelProperty(value = "取号时间")
    private Date createTime;

    @ApiModelProperty(value = "就餐时间")
    private Date eatTime;

    @ApiModelProperty(value = "过号时间")
    private Date passNumberTime;

    @ApiModelProperty(value = "就餐人数")
    private Integer personNumber;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "等位人数")
    private Integer waitNumber;

    @ApiModelProperty(value = "桌号类型")
    private String tableType;

    @ApiModelProperty(value = "叫号次数")
    private Integer callNumber;

    @ApiModelProperty(value = "最后一次叫号时间")
    private Date callNumberTime;

    @ApiModelProperty(value = "每秒浮动金额")
    private BigDecimal flowMoney;

    @ApiModelProperty(value = "桌号类型总数")
    private Integer countByTableTpye;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "会员id")
    private String customerId;

    @ApiModelProperty(value = "最终等位红包金额")
    private BigDecimal finalMoney;

    @ApiModelProperty(value = "等位红包上线价")
    private BigDecimal highMoney;

    @ApiModelProperty(value = "当前取的号码")
    private String codeValue;

    @ApiModelProperty(value = "tb_table_code表字段id关联")
    private String codeId;

}
