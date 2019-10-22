package com.resto.shop.web.model;

import java.io.Serializable;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/8/27/0027 17:02
 * @Description:
 */
public class SalesMemoV61 implements Serializable {
    //交易日期yyyy-mm-dd
    private String txDate;
    //店铺编号
    private String storeCode;
    //收款机号
    private String tillId;
    //销售单号
    private String docNo;

    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
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
}
