package com.resto.brand.core.TxSmsMessage;

import java.io.Serializable;
import java.util.List;

/**
 应答包体
 */
public class TxSmsResult implements Serializable {


    private String result;//0表示成功，非0表示失败，失败后没有detail列表信息

    private String errmsg;//result非0时的具体错误信息

    private String ext;//用户的session内容，腾讯server回包中会原样返回

    private List<TxSmsResultDetail> txSmsResultDetailList;

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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public List<TxSmsResultDetail> getTxSmsResultDetailList() {
        return txSmsResultDetailList;
    }

    public void setTxSmsResultDetailList(List<TxSmsResultDetail> txSmsResultDetailList) {
        this.txSmsResultDetailList = txSmsResultDetailList;
    }

    public TxSmsResult() {
    }

    public TxSmsResult(String result, String errmsg, String ext, List<TxSmsResultDetail> txSmsResultDetailList) {
        this.result = result;
        this.errmsg = errmsg;
        this.ext = ext;
        this.txSmsResultDetailList = txSmsResultDetailList;
    }
}
