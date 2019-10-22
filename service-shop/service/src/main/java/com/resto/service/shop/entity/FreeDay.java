package com.resto.service.shop.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class FreeDay implements Serializable {

    private static final long serialVersionUID = -61735040407049198L;

    private String id;

    @DateTimeFormat(pattern="yy-MM-dd")
    private Date freeDay;
    @DateTimeFormat(pattern="yy-MM-dd")
    private Date begin;
    @DateTimeFormat(pattern="yy-MM-dd")
    private Date end;
    private String shopDetailId;

}