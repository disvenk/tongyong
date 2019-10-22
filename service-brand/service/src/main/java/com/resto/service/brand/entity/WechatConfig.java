package com.resto.service.brand.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class WechatConfig implements Serializable {

    private static final long serialVersionUID = 8526001994029875228L;

    private String id;

    @NotBlank(message="微信appId不能为空")
    private String appid;

    @NotBlank(message="微信appSecret不能为空")
    private String appsecret;

    private String cardId;//微信会员卡id

    private String mchid;

    private String mchkey;
    
    private String payCertPath;

    private Integer state;

    //关联查询的 品牌 名称
    private String brandName;

}