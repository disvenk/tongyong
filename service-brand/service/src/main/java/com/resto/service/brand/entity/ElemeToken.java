package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by carl on 2017/5/26.
 */
@Data
public class ElemeToken implements Serializable {

    private static final long serialVersionUID = 9153592431758325428L;

    private Long id;

    private String accessToken;

    private String tokenType;

    private Long expiresIn;

    private String refreshToken;

    private String scope;

    private Date updateTime;

    private String shopId;

    private String brandId;

    private String brandName;

    private String shopName;

}
