package com.resto.api.brand.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class WechatConfigDto implements Serializable {

    private static final long serialVersionUID = -8893436941997201338L;

    private String id;

    private String appid;

    private String appsecret;

    private String cardId;//微信会员卡id

    private String mchid;

    private String mchkey;
    
    private String payCertPath;
    
    private Integer state;

    //关联查询的 品牌 名称
    private String brandName;

}