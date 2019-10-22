package com.resto.brand.core.TxSmsMessage;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yz on 2017-05-19.
 *
 * 2 请求包体

 包体为json字符串
 */
public class TxSmsParam {

    private List<TxSmsTel> tel;

    private  String sign ;

    private  String tpl_id;

    private ArrayList<String> params;

    private String sig;

    private Long time ;

    private String extend;

    private String ext;

    public static void main(String[] args) {
        List<TxSmsTel> tels = new ArrayList<>();
        TxSmsTel tx1 = new TxSmsTel("86","13317182430");
        TxSmsTel tx2 = new TxSmsTel("86","13627626221");
        tels.add(tx1);
        tels.add(tx2);

        TxSmsParam txSmsParam = new TxSmsParam();
        txSmsParam.setSign("1234");
        txSmsParam.setTel(tels);
        txSmsParam.setTpl_id("122212");
        String json = JSONObject.toJSONString(txSmsParam);
        System.out.println(json);
    }

    public List<TxSmsTel> getTel() {
        return tel;
    }

    public void setTel(List<TxSmsTel> tel) {
        this.tel = tel;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTpl_id() {
        return tpl_id;
    }

    public void setTpl_id(String tpl_id) {
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
}
