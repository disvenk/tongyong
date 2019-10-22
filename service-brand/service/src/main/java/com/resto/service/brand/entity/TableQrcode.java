package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by carl on 2016/12/16.
 */
@Data
public class TableQrcode implements Serializable {

    private static final long serialVersionUID = -339360643384677354L;

    private Long id;

    private String brandId;

    private String shopDetailId;

    private Integer tableNumber;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private String brandName;

    private String shopName;

    private Long areaId;

    private Integer customerCount;

    private String areaName;

}
