package com.resto.service.brand.entity;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Examine implements Serializable {

    private static final long serialVersionUID = -8865693209946626888L;

    private Long id;

    private Long contractId;

    private Date createTime;

    private String department;

    private String applicant;

    private String email;

    private String remark;

    private BigDecimal money;

    private Integer status;

    private Integer type;

    private String brandName; //合同中 的 品牌名称 为方便写入这里

    private String constractNum;//合同编号

    private String review;
}