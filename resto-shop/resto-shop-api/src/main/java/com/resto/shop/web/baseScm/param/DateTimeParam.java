package com.resto.shop.web.baseScm.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by anna on 15-9-9.
 */
public class DateTimeParam implements ISqlParam<Date> {
    private Date param;
    private int index;

    public  DateTimeParam(){}

    public DateTimeParam(Date param) {
        this.param = param;
    }

    @Override
    public void setParameter(Date p) {
        this.param = p;
    }

    public DateTimeParam(int index, Date param) {
        this.index = index;
        this.param = param;
    }

    @Override
    public void setPstParameter(PreparedStatement pst) throws SQLException {
        Timestamp sqlTime = new Timestamp(param.getTime());
        pst.setTimestamp(index, sqlTime);
    }

    @Override
    public void setParamIndex(int index) {
        this.index = index;
    }

    @Override
    public Date getParamValue() {
        return param;
    }

    @Override
    public int getParamSize() {
        return 1;
    }



}
