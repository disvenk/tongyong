package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TemplateFlowDto implements Serializable{

    private static final long serialVersionUID = 3027643097401528922L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "微信公众号appid")
    private String appid;

    @ApiModelProperty(value = "微信模板消息编号")
    private String templateNumber;

    @ApiModelProperty(value = "模板id")
    private String templateId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}