package com.resto.shop.web.baseScm.domain;

import java.util.List;

/**
 * Created by zhaojingyang on 2015/8/15.
 */
public class InOrNotin {
    private String column;
    private List<Object> values;

    public void setColumn(String column) {
        this.column = column;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public List<Object> getValues() {

        return values;
    }

    public String getColumn() {
        return column;
    }

}
