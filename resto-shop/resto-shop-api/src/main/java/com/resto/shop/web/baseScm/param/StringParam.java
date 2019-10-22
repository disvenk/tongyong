package com.resto.shop.web.baseScm.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by zhaojingyang on 15-9-9.
 */
public class StringParam implements ISqlParam<String> {
    private static final long serialVersionUID = 5303135202816861511L;
    private String param;
    private int index;

    public StringParam() {

    }

    public StringParam(String param) {
        this.param = param;
    }

    @Override
    public void setParameter(String p) {
        this.param = p;
    }

    public StringParam(int index, String param) {
        this.index = index;
        this.param = param;
    }

    @Override
    public void setPstParameter(PreparedStatement pst) throws SQLException {
        pst.setString(index, param);
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
    public String getParamValue() {
        return param;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
