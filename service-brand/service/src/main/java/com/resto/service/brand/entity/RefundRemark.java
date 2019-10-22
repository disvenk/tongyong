package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RefundRemark implements Serializable {

    private static final long serialVersionUID = -1804990242271106173L;

    private Integer id;

    private String name;

    private Date createTime;

    private Integer sort;

    private Integer state;

}