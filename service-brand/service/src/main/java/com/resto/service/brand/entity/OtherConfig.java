package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OtherConfig implements Serializable {

    private static final long serialVersionUID = -2530631097148797715L;

    private Long id;

    private String configSign;

    private String configName;

    private String configRemark;

    private Date createTime;

}