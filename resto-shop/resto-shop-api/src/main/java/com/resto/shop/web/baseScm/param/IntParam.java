package com.resto.shop.web.baseScm.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by zhaojingyang on 15-9-9.
 */
public class IntParam implements ISqlParam<Integer> {
    private static final long serialVersionUID = 6763501151208170951L;
    private Integer param;
    private int index;

    public IntParam() {

    }

    public IntParam(Integer param) {
        this.param = param;
    }

    public IntParam(int index, Integer param) {
        this.index = index;
        this.param = param;
    }

    @Override
    public int getParamSize() {
        return 1;
    }



    @Override
    public void setParameter(Integer p) {
        this.param = p;
    }

    @Override
    public void setPstParameter(PreparedStatement pst) throws SQLException {
        pst.setInt(index, param);
    }

    @Override
    public void setParamIndex(int index) {
        this.index = index;
    }

    @Override
    public Integer getParamValue() {
        return param;
    }

}
