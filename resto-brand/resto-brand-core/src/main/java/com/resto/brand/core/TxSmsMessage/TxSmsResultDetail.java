package com.resto.brand.core.TxSmsMessage;

/**
 * Created by yz on 2017-05-19.
 */
public class TxSmsResultDetail {

    private String result; //0表示成功(计费依据)，非0表示失败
    private String errmsg;//result非0时的具体错误信息
    private String mobile;//手机号码
    private String nationcode;//国家码
    private String sid;//标识本次发送id，标识一次短信下发记录
    private String fee;//短信计费的条数

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNationcode() {
        return nationcode;
    }

    public void setNationcode(String nationcode) {
        this.nationcode = nationcode;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public TxSmsResultDetail() {
    }

    public TxSmsResultDetail(String result, String errmsg, String mobile, String nationcode, String sid, String fee) {
        this.result = result;
        this.errmsg = errmsg;
        this.mobile = mobile;
        this.nationcode = nationcode;
        this.sid = sid;
        this.fee = fee;
    }
}
