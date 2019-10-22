package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by carl on 2017/7/14.
 */
@Data
public class ShopTvConfigDto implements Serializable {

    private static final long serialVersionUID = 6780807214273389017L;

    private Long id;

    @ApiModelProperty(value = "店铺id")
    private String shopDetailId;

    @ApiModelProperty(value = "品牌id")
    private String brandId;

    @ApiModelProperty(value = "准备中底色")
    private String readyBackColor;

    @ApiModelProperty(value = "请取餐底色")
    private String takeMealBackColor;

    @ApiModelProperty(value = "当前叫号底色")
    private String callBackColor;

    @ApiModelProperty(value = "背景图片")
    private String tvBackground;

    @ApiModelProperty(value = "数字的颜色")
    private String numberColor;

    @ApiModelProperty(value = "当前叫号的颜色")
    private String callNumberColor;

    @ApiModelProperty(value = "(请留意您的取餐信息)文本信息")
    private String text;

    @ApiModelProperty(value = "准备中 请取餐  当前叫号  标签上的字体颜色")
    private String labelColor;

}
