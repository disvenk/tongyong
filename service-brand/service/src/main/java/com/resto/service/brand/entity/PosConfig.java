package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/5/27.
 */
@Data
public class PosConfig implements Serializable {

    private static final long serialVersionUID = 2359683599129079849L;

    private Long id;

    private String clientIp;

    private String serverIp;

}
