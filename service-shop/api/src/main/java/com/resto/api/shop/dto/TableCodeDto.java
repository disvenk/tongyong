package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TableCodeDto implements Serializable {

    private static final long serialVersionUID = 4889032229685541481L;

    @ApiModelProperty(value = "桌位id")
    private String id;

    @ApiModelProperty(value = "编号名称")
    private String name;

    @ApiModelProperty(value = "编号")
    private String codeNumber;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date endTime;

    @ApiModelProperty(value = "最大人数")
    private Integer maxNumber;

    @ApiModelProperty(value = "最小人数")
    private Integer minNumber;

    @ApiModelProperty(value = "是否启用")
    private Byte isUsed;

    @ApiModelProperty(value = "店铺id")
    private  String shopDetailId;

    @ApiModelProperty(value = "品牌id")
    private  String brandId;

    @ApiModelProperty(value = "等待位数")
    private Integer waitNumber;
    
    @ApiModelProperty(value = "该桌号类型，取号的集合")
    private List<GetNumberDto> getNumbers;

    @ApiModelProperty(value = "排序")
    private  Integer sort;

    @ApiModelProperty(value = "桌号")
    private String tableNumber;
}