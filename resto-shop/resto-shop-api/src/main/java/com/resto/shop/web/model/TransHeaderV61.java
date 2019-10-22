package com.resto.shop.web.model;

import java.io.Serializable;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/8/27/0027 15:02
 * @Description:
 */
public class TransHeaderV61 implements Serializable {
    //交易日期yyyy-mm-dd，如无提供，则用LedgerDate代替
    private String txDate;
    //yyyy-mm-dd hh:mm:ss客戶端发生的日期及时间( System Clock )
    private String ledgerDatetime;
    //店铺编号
    private String storeCode;
    //收款机号
    private String tillId;
    //销售单号
    private String docNo;
    //被取消的原销售单号
    private String voidDocNo;
    //预留字段
    private String txAttrib;

    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
    }

    public String getLedgerDatetime() {
        return ledgerDatetime;
    }

    public void setLedgerDatetime(String ledgerDatetime) {
        this.ledgerDatetime = ledgerDatetime;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getTillId() {
        return tillId;
    }

    public void setTillId(String tillId) {
        this.tillId = tillId;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getVoidDocNo() {
        return voidDocNo;
    }

    public void setVoidDocNo(String voidDocNo) {
        this.voidDocNo = voidDocNo;
    }

    public String getTxAttrib() {
        return txAttrib;
    }

    public void setTxAttrib(String txAttrib) {
        this.txAttrib = txAttrib;
    }
}
