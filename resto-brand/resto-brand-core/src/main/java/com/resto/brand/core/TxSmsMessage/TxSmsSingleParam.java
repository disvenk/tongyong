package com.resto.brand.core.TxSmsMessage;

import java.util.ArrayList;

/**
 * Created by yz on 2017-05-19.
 *
 * 指定模板单发短信 请求体

 包体为json字符串
 */
public class TxSmsSingleParam {

    private TxSmsTel tel;

    private  String sign ;

    private  Integer tpl_id;

    private ArrayList<String> params;

    private String sig;

    private Long time ;

    private String extend;

    private String ext;


    public TxSmsTel getTel() {
        return tel;
    }

    public void setTel(TxSmsTel tel) {
        this.tel = tel;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getTpl_id() {
        return tpl_id;
    }

    public void setTpl_id(Integer tpl_id) {
        this.tpl_id = tpl_id;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public TxSmsSingleParam(TxSmsTel tel, String sign, Integer tpl_id, ArrayList<String> params, String sig, Long time, String extend, String ext) {
        this.tel = tel;
        this.sign = sign;
        this.tpl_id = tpl_id;
        this.params = params;
        this.sig = sig;
        this.time = time;
        this.extend = extend;
        this.ext = ext;
    }

    public TxSmsSingleParam() {
    }
}
