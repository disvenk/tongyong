package com.resto.service.brand.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Brand implements Serializable {
    private static final long serialVersionUID = -2456907563102417924L;
    private String id;

    @NotBlank(message="品牌名称不能为空")
    private String brandName;

    @NotBlank(message="品牌标志不能为空")
    private String brandSign;

    private Date createTime;

    private String wechatConfigId;

    private String databaseConfigId;

    private String brandUserId;

    private int smsCount;//品牌的剩余短信条数

    private BigDecimal smsUnitPrice;//短信单价

    private String addUser;

    private String updateUser;

    private Integer state;

    private String wechatImgUrl;

    //服务器IP地址
    private String serverIp;

    //外键实体
    private WechatConfig wechatConfig;
    private DatabaseConfig databaseConfig;
    private SmsAcount smsAcount;
    private List<ShopDetail> shopDetail;

    private String brandSettingId;

    private Integer useState;

    private String phoneList;

    private String whitePhoneList;

    private Integer mqId;

    private String contractId;

}