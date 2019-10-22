package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RecommendCategoryDto implements Serializable {

    private static final long serialVersionUID = -3687364004793088952L;

    @ApiModelProperty(value = "菜品推荐类型id,主键")
    private String id;

    @ApiModelProperty(value = "推荐类型   0代表热销，1代表新品")
    private Integer type;

    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "推荐排序")
    private Integer sort;

    @ApiModelProperty(value = "是否启用")
    private Integer state;

    @ApiModelProperty(value = "店铺id")
    private String shopDetailId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private List<RecommendCategoryArticleDto> articles;

}