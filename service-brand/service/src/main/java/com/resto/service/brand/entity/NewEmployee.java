package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewEmployee implements Serializable {

    private static final long serialVersionUID = 6591994307676225118L;

    private String id;

    private Integer roleType;

    private String name;

    private String nickName;

    private String  wechatAvatar;

    private Integer sex;

    private String telephone;

    private String shopDetailId;

    private String brandId;

    private Date createTime;

    private Integer state;

    private String shopName;

    private String roleValue;

    private String sexValue;

    private String stateValue;

}