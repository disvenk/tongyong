package com.resto.api.brand.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class WechatChargeConfigDto implements Serializable {


    private static final long serialVersionUID = -2832455612206919794L;

    private String id;

    private String appid;

    private String appsecret;

    private String mchid;

    private String mchkey;

    private String payCertPath;

    private Long wxServerId;

    private Integer isSub;

}
