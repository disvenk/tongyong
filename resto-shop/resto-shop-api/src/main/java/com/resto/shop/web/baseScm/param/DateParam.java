package com.resto.shop.web.baseScm.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by anna on 15-9-9.
 */
public class DateParam implements ISqlParam<Date> {
    private static final long serialVersionUID = -1720731199404512312L;
    private Date param;
    private int index;

    public DateParam(){}

    public DateParam(Date param) {
        this.param = param;
    }

    public DateParam(int index, Date param) {
        this.index = index;
        this.param = param;
    }

    @Override
    public void setParameter(Date p) {
        this.param = p;
    }

    @Override
    public void setPstParameter(PreparedStatement pst) throws SQLException {
        java.sql.Date sqlDate = new java.sql.Date(param.getTime());
        pst.setDate(index, sqlDate);
    }

    @Override
    public void setParamIndex(int index) {
        this.index = index;
    }

    @Override
    public int getParamSize() {
        return 1;
    }

    @Override
    public Date getParamValue() {
        return param;
    }

    public Date getParam() {
        return param;
    }

    public void setParam(Date param) {
        this.param = param;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
