package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoOrderRemarkDto implements Serializable {

    private static final long serialVersionUID = -1726159269647108318L;

    @ApiModelProperty(value = "bo订单备注id")
    private String id;

    @ApiModelProperty(value = "备注名称")
    private String remarkName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态(0:不开启 1:开启)")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
