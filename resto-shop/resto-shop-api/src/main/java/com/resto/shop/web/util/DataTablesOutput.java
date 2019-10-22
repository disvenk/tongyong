package com.resto.shop.web.util;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xielc on 2017/12/11.
 */
public class DataTablesOutput<T>
{

    @JsonView({View.class})
    private Integer draw;

    @JsonView({View.class})
    private Long recordsTotal;

    @JsonView({View.class})
    private Long recordsFiltered;

    @JsonView({View.class})
    private List<T> data;

    @JsonView({View.class})
    private String error;

    public DataTablesOutput(Integer draw)
    {
        this.draw = draw;
        this.recordsTotal = Long.valueOf(0L);
        this.recordsFiltered = Long.valueOf(0L);
        this.data = new ArrayList<>();
    }


    public DataTablesOutput() {
    }

    public Integer getDraw()
    {
        return this.draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Long getRecordsTotal() {
        return this.recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return this.recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String toString()
    {
        return "DataTablesOutput [draw=" + this.draw + ", recordsTotal=" + this.recordsTotal + ", recordsFiltered=" + this.recordsFiltered + ", error=" + this.error + "]";
    }

    public static abstract interface View
    {
    }
}
