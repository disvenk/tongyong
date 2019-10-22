package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TemplateFlow implements Serializable {

    private static final long serialVersionUID = 1238645193526425956L;

    private Long id;

    private String appid;

    private String templateNumber;

    private String templateId;

    private Date createTime;

    private Date updateTime;
}