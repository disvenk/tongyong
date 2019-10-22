package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RecommendCategoryArticleDto implements Serializable {

    private static final long serialVersionUID = 6970009257365002231L;

    @ApiModelProperty(value = "菜品推荐id,主键")
    private String id;

    @ApiModelProperty(value = "菜品名")
    private String articleName;

    @ApiModelProperty(value = "菜品排序")
    private Integer recommendSort;

    @ApiModelProperty(value = "推荐类别id")
    private String recommendCategoryId;

    @ApiModelProperty(value = "菜品id")
    private String articleId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}