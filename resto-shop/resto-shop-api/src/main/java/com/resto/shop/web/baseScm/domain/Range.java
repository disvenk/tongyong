package com.resto.shop.web.baseScm.domain;

import java.io.Serializable;

/**
 * Created by guchenglin on 2015/8/15.
 */
public class Range implements Serializable {
    private static final long serialVersionUID = -2512320010868075984L;

    private String column;
    /**
     * >= from
     */
    private Object from;
    private Object to;


    public void setColumn(String column) {
        this.column = column;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public void setTo(Object to) {
        this.to = to;
    }

    public String getColumn() {
        return column;
    }

    public Object getFrom() {
        return from;
    }

    public Object getTo() {
        return to;
    }

    public Range(String column, Object from, Object to) {
        this.column = column;
        this.from = from;
        this.to = to;
    }
}
