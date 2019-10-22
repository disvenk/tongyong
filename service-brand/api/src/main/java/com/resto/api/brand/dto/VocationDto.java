package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by carl on 2016/12/28.
 */
@Data
public class VocationDto implements Serializable {

    private static final long serialVersionUID = 9168602008071727463L;

    private Integer id;

    private String sign;

    private String content;

    private Integer state;

    private String color;
}
