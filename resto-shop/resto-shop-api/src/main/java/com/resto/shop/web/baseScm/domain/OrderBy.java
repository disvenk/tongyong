package com.resto.shop.web.baseScm.domain;

import java.io.Serializable;

/**
 * Created by zhaojingyang on 2015/8/15.
 */
public class OrderBy implements Serializable {
    private static final long serialVersionUID = 3781499847681853145L;

    private String column;

    private String direction;

    public void setColumn(String column) {
        this.column = column;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public String getColumn() {
        return column;
    }
}
