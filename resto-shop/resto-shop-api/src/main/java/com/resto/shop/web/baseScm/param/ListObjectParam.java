package com.resto.shop.web.baseScm.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anna on 15-9-9.
 */
public class ListObjectParam implements ISqlParam<List<Object>> {
    private static final long serialVersionUID = 5303135202816861511L;
    private List<Object> param;
    private int index;

    public ListObjectParam() {
    }

    public ListObjectParam(List<Object> param) {
        this.param = param;
    }

    public ListObjectParam(int index, List<Object> param) {
        this.index = index;
        this.param = param;
    }

    public ListObjectParam(Long... param) {
        List<Object> ps = new ArrayList<Object>(param.length);
        for (Long p : param) {
            ps.add(p);
        }
        this.param = ps;
    }

    public ListObjectParam(int index, Long... param) {
        this.index = index;
        List<Object> ps = new ArrayList<Object>(param.length);
        for (Long p : param) {
            ps.add(p);
        }
        this.param = ps;
    }

    @Override
    public void setParameter(List<Object> p) {
        this.param = p;
    }

    @Override
    public void setPstParameter(PreparedStatement pst) throws SQLException {
        int tempIndex = index;
        for (Object obj : param) {
            pst.setObject(tempIndex, obj);
            tempIndex ++;
        }
    }

    @Override
    public void setParamIndex(int index) {
        this.index = index;
    }

    @Override
    public List<Object> getParamValue() {
        return param;
    }

    @Override
    public int getParamSize() {
        return param.size();
    }
}
