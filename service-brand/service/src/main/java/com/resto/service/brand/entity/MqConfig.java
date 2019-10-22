package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/7/12.
 */
@Data
public class MqConfig implements Serializable {

    private static final long serialVersionUID = 5638032279138040439L;

    private Long id;

    private String topic;

    private String cidShop;

    private String pid;

    private String cidPos;

    private String mqName;

}
