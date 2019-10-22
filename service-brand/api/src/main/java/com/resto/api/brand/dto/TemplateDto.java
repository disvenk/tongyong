package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TemplateDto implements Serializable {

    private static final long serialVersionUID = 42877320685037711L;

    @ApiModelProperty(value = "微信编号id")
    private Long id;

    @ApiModelProperty(value = "微信模板编号id")
    private String templateNumber;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}