package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RefundRemarkDto implements Serializable{

    private static final long serialVersionUID = 5197441409518179020L;

    private Integer id;

    private String name;

    private Date createTime;

    private Integer sort;

    private Integer state;

}