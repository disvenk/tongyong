package com.resto.brand.core.TxSmsMessage;

import java.io.Serializable;

/**
 单发短信 应答包体
 */
public class TxSmsSingleResult implements Serializable {


    private Integer result;//0表示成功，非0表示失败，失败后没有detail列表信息

    private String errmsg;//result非0时的具体错误信息

    private String ext;//用户的session内容，腾讯server回包中会原样返回

    private String sid; //标识本次发送id，标识一次短信下发记录

    private Integer fee;//短信计费的条数

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public TxSmsSingleResult(Integer result, String errmsg, String ext, String sid, Integer fee) {
        this.result = result;
        this.errmsg = errmsg;
        this.ext = ext;
        this.sid = sid;
        this.fee = fee;
    }

    public TxSmsSingleResult() {
    }
}
