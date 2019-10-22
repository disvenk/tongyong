package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by carl on 2017/8/25.
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -6623485128029671188L;

    private String telephone;

    private BigDecimal money;

    private String remark;

}
