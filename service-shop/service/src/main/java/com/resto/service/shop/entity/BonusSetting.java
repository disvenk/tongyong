package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BonusSetting implements Serializable{

    private static final long serialVersionUID = 2279361793675959837L;

    private String id;

    private String chargeSettingId;

    private String shopDetailId;

    private String brandId;

    private Integer chargeBonusRatio;

    private Integer shopownerBonusRatio;

    private Integer employeeBonusRatio;

    private Boolean state;

    private Date createTime;

    private Date updateTime;

    private String shopName;

    private String chargeName;

    private String wishing;

}