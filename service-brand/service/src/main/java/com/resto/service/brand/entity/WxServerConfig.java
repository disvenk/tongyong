package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/12/3.
 * 微信服务商配置
 */
@Data
public class WxServerConfig implements Serializable {

    private static final long serialVersionUID = 5924265095809657689L;

    //主键
    private Long id;
    //服务商名称
    private String name;
    //appid
    private String appid;
    //app密钥
    private String appsecret;
    //支付id
    private String mchid;
    //支付密钥
    private String mchkey;
    //证书路径
    private String payCertPath;
    //删除标记(1-未删除 0删除）
    private Integer deletedFlag;
}
