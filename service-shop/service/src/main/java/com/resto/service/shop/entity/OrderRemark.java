package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderRemark implements Serializable {

    private static final long serialVersionUID = -3034349439591219893L;

    private String id;

    private String boOrderRemarkId;

    private Date createTime;

    private String shopDetailId;

    private String brandId;

}
