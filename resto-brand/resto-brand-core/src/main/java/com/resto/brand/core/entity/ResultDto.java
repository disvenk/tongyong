package com.resto.brand.core.entity;

import java.io.Serializable;

/**
 * Created by carl on 2017/8/8.
 */
public class ResultDto<T> implements Serializable{
    private T obj;
    private String errcode;
    private String errmsg;
    public T getObj() {
        return obj;
    }
    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
