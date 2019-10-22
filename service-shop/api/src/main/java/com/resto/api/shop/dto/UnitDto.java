package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UnitDto implements Serializable {

    private static final long serialVersionUID = -7681544141650292114L;

    private String id;

    private String name;

    private Integer sort;

    private String shopId;

    private Integer choiceType;

    private Integer isUsed;

    private List<UnitDetailDto> details;

}
