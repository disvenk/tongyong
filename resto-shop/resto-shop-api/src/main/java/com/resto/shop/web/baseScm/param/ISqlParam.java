package com.resto.shop.web.baseScm.param;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by zhaojingyang on 15-9-9.
 */
public interface ISqlParam<T> extends Serializable {
     void setParameter(T p);
     void setPstParameter(PreparedStatement pst) throws SQLException;
     void setParamIndex(int index);
     T getParamValue();
     int getParamSize();
}
