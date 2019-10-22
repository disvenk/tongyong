package com.resto.shop.web.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MenuShop implements Serializable{

    private static final long serialVersionUID = 3615132039057316895L;

    private Long id;

    private String brandId;

    private String shopDetailId;

    private Long menuId;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private Boolean state;

    private Integer type;

    //类型描述
    private String typeDescribe;

    //菜单版本号
    private String menuVersion;

    //菜单名称
    private String menuName;

    //菜单有效期类型 1：长期有效    2：时间周期
    private Integer menuCycle;

    //店铺名称
    private String shopName;
}