package com.resto.service.shop.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ArticleUnit implements Serializable{

    private static final long serialVersionUID = -1250325649049585511L;

    private Integer id;

    private String name;

    private BigDecimal sort;

    private Integer tbArticleAttrId;

    private Integer count;

    private List<ArticlePrice> articlePrices;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        if (count == 0){
            this.count = 0;
        }
        this.count = count;
    }

    //构造方法
    public ArticleUnit(String name, BigDecimal sort, Integer tbArticleAttrId) {
		super();
		this.name = name;
		this.sort = sort;
		this.tbArticleAttrId = tbArticleAttrId;
	}
    //构造方法
    public ArticleUnit(String id, String name, BigDecimal sort, Integer tbArticleAttrId) {
		super();
        if(StringUtils.isEmpty(id)){
            this.id = null;
        }else{
            this.id = Integer.valueOf(id);
        }
		this.name = name;
		this.sort = sort;
		this.tbArticleAttrId = tbArticleAttrId;
	}
    //默认构造方法
    public ArticleUnit() {}
}