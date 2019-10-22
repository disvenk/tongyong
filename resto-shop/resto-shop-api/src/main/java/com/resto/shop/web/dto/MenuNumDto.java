package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xielc on 2018/11/6.
 */
@Data
public class MenuNumDto implements Serializable{

    private static final long serialVersionUID = -6017198080748218367L;

    private Integer enableNum;

    private Integer notEnableNum;

    private Integer allNum;
}
