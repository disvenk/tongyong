package com.resto.shop.web.baseScm.domain;

import java.io.Serializable;

/**
 * Created by zhaojingyang on 2015/8/15.
 */
public class Parm implements Serializable {

    private String column;

    private Object value;

    public void setColumn(String column) {
        this.column = column;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public Parm(String column, Object value) {
        this.column = column;
        this.value = value;
    }
}