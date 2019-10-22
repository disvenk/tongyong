package com.resto.shop.web.baseScm.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by anna on 15-9-9.
 */
public class LongParam implements ISqlParam<Long> {
    private static final long serialVersionUID = 6763501151208170951L;
    private Long param;
    private int index;

    public LongParam() {
    }

    public LongParam(Long param) {
        this.param = param;
    }

    public LongParam(int index, Long param) {
        this.index = index;
        this.param = param;
    }

    @Override
    public void setParameter(Long p) {
        this.param = p;
    }

    @Override
    public void setPstParameter(PreparedStatement pst) throws SQLException {
        pst.setLong(index, param);
    }

    @Override
    public int getParamSize() {
        return 1;
    }


    @Override
    public void setParamIndex(int index) {
        this.index = index;
    }

    @Override
    public Long getParamValue() {
        return param;
    }

    public Long getParam() {
        return param;
    }

    public void setParam(Long param) {
        this.param = param;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
