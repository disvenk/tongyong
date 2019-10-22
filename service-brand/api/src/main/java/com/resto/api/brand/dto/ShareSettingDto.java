package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ShareSettingDto implements Serializable {

    private static final long serialVersionUID = -2988154838481062557L;

    private String id;

    @ApiModelProperty(value = "分享标题")
    private String shareTitle;

    @ApiModelProperty(value = "分享图标")
    private String shareIcon;

    @ApiModelProperty(value = "最小分享评分")
    private Integer minLevel;

    @ApiModelProperty(value = "最少分享字数")
    private Integer minLength;

    @ApiModelProperty(value = "红包返利比例")
    private BigDecimal rebate;

    @ApiModelProperty(value = "是否启用该活动")
    private Boolean isActivity;

    @ApiModelProperty(value = "品牌id")
    private String brandId;

    @ApiModelProperty(value = "分享弹窗内容")
    private String dialogText;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "最小金额")
    private BigDecimal minMoney;

    @ApiModelProperty(value = "最大金额")
    private BigDecimal maxMoney;

    private String registerButton;

    @ApiModelProperty(value = "延迟提醒时间（秒）")
    private Integer delayTime;

    @ApiModelProperty(value = "开启多次返利")
    private Integer openMultipleRebates;

    @ApiModelProperty(value = "以后订单红包返利比例")
    private BigDecimal afterRebate;

    @ApiModelProperty(value = "以后订单返利的下线")
    private BigDecimal afterMinMoney;

    @ApiModelProperty(value = "以后订单返利的上线")
    private BigDecimal afterMaxMoney;

}