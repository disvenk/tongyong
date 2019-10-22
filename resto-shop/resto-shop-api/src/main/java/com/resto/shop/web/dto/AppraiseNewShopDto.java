package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/6/9.
 */
@Data
public class AppraiseNewShopDto implements Serializable{

    private static final long serialVersionUID = -3594834139306684926L;

    private String shopId;

    private String shopName;

    private Integer shopNum=0;

    private BigDecimal appraiseRatio = new BigDecimal(0);

    private BigDecimal allLevel = new BigDecimal(0);

    private BigDecimal service = new BigDecimal(0);

    private BigDecimal conditions = new BigDecimal(0);

    private BigDecimal price = new BigDecimal(0);

    private BigDecimal ambience = new BigDecimal(0);

    private BigDecimal exhibit = new BigDecimal(0);

}
