package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class GetNumber implements Serializable {

    private static final long serialVersionUID = 8844299583892363928L;

    private String id;

    private String shopDetailId;

    private String brandId;

    private Integer state;

    private Date createTime;

    private Date eatTime;

    private Date passNumberTime;

    private Integer personNumber;

    private String phone;

    private Integer waitNumber;

    private String tableType;

    private Integer callNumber;

    private Date callNumberTime;

    private BigDecimal flowMoney;

    private Integer countByTableTpye;

    private String imgUrl;

    private String shopName;

    private String customerId;

    private BigDecimal finalMoney;

    private BigDecimal highMoney;
    
    //当前取的号码
    private String codeValue;

    private String codeId;

}
