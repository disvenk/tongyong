package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-11-13 17:45
 */
@Data
public class SvipActivityDto implements Serializable {
    private String id;
    private String activityName;
    private Integer num;
    private BigDecimal money;

}
