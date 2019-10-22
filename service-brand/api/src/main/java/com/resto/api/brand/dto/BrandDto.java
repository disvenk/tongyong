package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class BrandDto implements Serializable {
    private static final long serialVersionUID = -2456907563102417924L;
    private String id;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("品牌标志")
    private String brandSign;

    @ApiModelProperty("微信公众号配置Id")
    private String wechatConfigId;

    @ApiModelProperty("数据库配置Id")
    private String databaseConfigId;

    @ApiModelProperty("品牌用户编号")
    private String brandUserId;

    @ApiModelProperty("品牌的剩余短信条数")
    private int smsCount;

    @ApiModelProperty("短信单价")
    private BigDecimal smsUnitPrice;

    @ApiModelProperty("添加品牌的用户")
    private String addUser;

    @ApiModelProperty("状态, 0删除，1正常")
    private Integer state;

    @ApiModelProperty("微信图片地址")
    private String wechatImgUrl;

    @ApiModelProperty("服务器IP地址")
    private String serverIp;

    @ApiModelProperty("品牌设置Id")
    private String brandSettingId;

    @ApiModelProperty("0-正常 1-异常")
    private Integer useState;

    @ApiModelProperty("白名单")
    private String whitePhoneList;

    @ApiModelProperty("队列id")
    private Long mqId;

    private String phoneList;

    private String contractId;

    //外键实体
    private WechatConfigDto wechatConfig;
    private DatabaseConfigDto databaseConfig;
    private List<ShopDetailDto> shopDetail;

}