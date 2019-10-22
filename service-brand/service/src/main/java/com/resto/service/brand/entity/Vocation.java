package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by carl on 2016/12/28.
 */
@Data
public class Vocation implements Serializable {

    private static final long serialVersionUID = 5476506482775652609L;

    private Integer id;

    private String sign;

    private String content;

    private Integer state;

    private String color;
}
