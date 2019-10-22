package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/2/8.
 */
@Data
public class ThirdParam implements Serializable {

    private static final long serialVersionUID = -4713374550993090425L;

    private Long id;

    private String desc;

    private String remark;

    private String brandId;

    private String brandName;

    private String name;

}
