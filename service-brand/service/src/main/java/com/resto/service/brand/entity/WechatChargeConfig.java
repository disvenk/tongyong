package com.resto.service.brand.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/12/26.
 * 微信充值账户位置
 */
@Data
public class WechatChargeConfig implements Serializable {

    private static final long serialVersionUID = -2832455612206919794L;

    private String id;

    @NotBlank(message="微信appId不能为空")
    private String appid;

    @NotBlank(message="微信appSecret不能为空")
    private String appsecret;

    private String mchid;

    private String mchkey;

    private String payCertPath;

    private Long wxServerId;

    private Integer isSub;

}
