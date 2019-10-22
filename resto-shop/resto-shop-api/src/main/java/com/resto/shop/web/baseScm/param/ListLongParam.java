package com.resto.shop.web.baseScm.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Long List
 * Created by zhaojingyang on 2015/9/28.
 */
public class ListLongParam  implements ISqlParam<List<Long>> {
    private static final long serialVersionUID = 4933904763608088159L;
    private List<Long> param;
    private int index;


    public ListLongParam() {
    }

    public ListLongParam(List<Long> param) {
        this.param = param;
    }

    public ListLongParam(int index, List<Long> param) {
        this.index = index;
        this.param = param;
    }

    public ListLongParam(Long... param) {
        List<Long> ps = new ArrayList<Long>(param.length);
        for (Long p : param) {
            ps.add(p);
        }
        this.param = ps;
    }

    public ListLongParam(int index, Long... param) {
        this.index = index;
        List<Long> ps = new ArrayList<Long>(param.length);
        for (Long p : param) {
            ps.add(p);
        }
        this.param = ps;
    }

    @Override
    public void setParameter(List<Long> p) {
       this.param = p;
    }


    @Override
    public void setPstParameter(PreparedStatement pst) throws SQLException {
        int tempIndex = index;
        for (Long p : param) {
            pst.setLong(tempIndex, p);
            tempIndex++;
        }
    }

    @Override
    public void setParamIndex(int index) {
        this.index = index;
    }

    @Override
    public List<Long> getParamValue() {
        return param;
    }

    @Override
    public int getParamSize() {
        return param.size();
    }
}
