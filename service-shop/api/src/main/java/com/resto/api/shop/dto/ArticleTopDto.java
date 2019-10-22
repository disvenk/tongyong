package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleTopDto implements Serializable {

    private static final long serialVersionUID = -806137783849514831L;

    @ApiModelProperty(value = "红黑榜id")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "菜名 或者服务名称")
    private String name;

    @ApiModelProperty(value = "1,2,3,4是差 5好")
    private Integer type;

    @ApiModelProperty(value = "店铺id")
    private String shopDetailId;

}