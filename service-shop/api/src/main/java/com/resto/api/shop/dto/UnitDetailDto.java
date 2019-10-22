package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UnitDetailDto implements Serializable {

    private static final long serialVersionUID = 2490011578194664150L;

    private String id;

    private String name;

    private Integer sort;

    private Integer isUsed;

    private BigDecimal price ;

}
