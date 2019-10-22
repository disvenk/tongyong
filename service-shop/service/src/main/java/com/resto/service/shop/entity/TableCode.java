package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
public class TableCode implements Serializable {

    private static final long serialVersionUID = -7515482023643590363L;

    private String id;

    //编号名称
    private String name;

    //编号
    private String codeNumber;

    //创建时间
    private Date createTime;

    //最后修改时间
    private Date endTime;

    //最大人数
    private Integer maxNumber;

    //最小人数
    private Integer minNumber;

    //是否启用
    private Byte isUsed;

    private  String shopDetailId;

    private  String brandId;

    //等待位数
    private Integer waitNumber;
    
    //该桌号类型，取号的集合
    private List<GetNumber> getNumbers;

    //排序
    private  Integer sort;
}