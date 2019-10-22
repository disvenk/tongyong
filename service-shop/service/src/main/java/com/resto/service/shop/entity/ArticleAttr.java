package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleAttr implements Serializable{

    private static final long serialVersionUID = 1559072993714351550L;

    private Integer id;

    private String name;

    private Integer sort;

    private String shopDetailId;
    
    private String state;
    
    /**
     * 规格 ID集合
     */
    private String[] unitIds;
    /**
     * 规格名称 集合
     */
    private String[] units;
    
    /**
     * 规格 排序 集合
     */
    private String[] unitSorts;
    
    /**
     * 用于 查询时 保存 规格的集合
     */
    private List<ArticleUnit> articleUnits;
	
}