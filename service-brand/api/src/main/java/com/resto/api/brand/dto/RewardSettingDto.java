package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RewardSettingDto implements Serializable {

    private static final long serialVersionUID = 183943253095051767L;

    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "金额列表 默认 1,2,8,10,12,20 固定6个")
    private String moneyList;

    @ApiModelProperty(value = "最小评论分数  默认5星好评")
    private Integer minLevel;

    @ApiModelProperty(value = "最小评论长度，默认10个字")
    private Integer minLength;

    @ApiModelProperty(value = "是否启用，默认不启用")
    private Boolean isActivty;

    @ApiModelProperty(value = "品牌id")
    private String brandId;
}