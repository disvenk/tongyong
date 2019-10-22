package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by disvenk.dai on 2018-11-14 11:23
 */
@Data
public class SvipActReportDto implements Serializable {

    private String shopDetailId;
    private String shopName;
    private String nickName;
    private String tel;
    private BigDecimal money;
    private Date dataTime;
}
