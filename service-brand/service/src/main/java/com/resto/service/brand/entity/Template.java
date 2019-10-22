package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Template implements Serializable {

    private static final long serialVersionUID = -8861942174295654945L;

    private Long id;

    private String templateNumber;

    private String remark;

    private Date createTime;

    private Date updateTime;

}