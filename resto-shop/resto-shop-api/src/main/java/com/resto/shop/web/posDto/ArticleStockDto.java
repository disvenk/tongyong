package com.resto.shop.web.posDto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/8/9.
 */
public class ArticleStockDto implements Serializable{
    private static final long serialVersionUID = 1235728432816907156L;
    private String id;

    private Integer currentWorkingStock;

    public ArticleStockDto(String id, Integer currentWorkingStock) {
        this.id = id;
        this.currentWorkingStock = currentWorkingStock;
    }

    public ArticleStockDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCurrentWorkingStock() {
        return currentWorkingStock;
    }

    public void setCurrentWorkingStock(Integer currentWorkingStock) {
        this.currentWorkingStock = currentWorkingStock;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("currentWorkingStock", currentWorkingStock)
                .toString();
    }
}
