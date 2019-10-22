package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExperienceDto implements Serializable {

    private static final long serialVersionUID = -5085282169459527992L;

    @ApiModelProperty(value = "体验id")
    private Integer id;

    @ApiModelProperty(value = "2好评  4差评 ")
    private Integer showType;

    @ApiModelProperty(value = "标题")
    private String title;

    private String picUrl;

    @ApiModelProperty(value = "店铺id")
    private String shopDetailId;

    private String photoSquare;

    @ApiModelProperty(value = "排序")
    private Integer showSort;

}