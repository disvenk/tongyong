package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/12/3.
 * 微信服务商配置
 */
@Data
public class WxServerConfigDto implements Serializable {

    private static final long serialVersionUID = 7030829964539197899L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "服务商名称")
    private String name;

    @ApiModelProperty(value = "appid")
    private String appid;

    @ApiModelProperty(value = "app密钥")
    private String appsecret;

    @ApiModelProperty(value = "支付id")
    private String mchid;

    @ApiModelProperty(value = "支付密钥")
    private String mchkey;

    @ApiModelProperty(value = "证书路径")
    private String payCertPath;

    @ApiModelProperty(value = "删除标记(1-未删除 0删除）")
    private Integer deletedFlag;

}
