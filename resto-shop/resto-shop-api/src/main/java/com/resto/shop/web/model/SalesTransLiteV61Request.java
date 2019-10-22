package com.resto.shop.web.model;

import java.io.Serializable;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/8/27/0027 15:01
 * @Description:
 */
public class SalesTransLiteV61Request implements Serializable {
    private String apiKey;
    private String signature;
    private String docKey;
    private TransHeaderV61 transHeader;
    private SalesTotalLiteV61 salesTotal;
    private SalesItemLiteV61 salesItem;
    private SalesTenderLiteV61 salesTender;
    private SalesMemoV61 SalesMemo;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public TransHeaderV61 getTransHeader() {
        return transHeader;
    }

    public void setTransHeader(TransHeaderV61 transHeader) {
        this.transHeader = transHeader;
    }

    public SalesTotalLiteV61 getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(SalesTotalLiteV61 salesTotal) {
        this.salesTotal = salesTotal;
    }

    public SalesItemLiteV61 getSalesItem() {
        return salesItem;
    }

    public void setSalesItem(SalesItemLiteV61 salesItem) {
        this.salesItem = salesItem;
    }

    public SalesTenderLiteV61 getSalesTender() {
        return salesTender;
    }

    public void setSalesTender(SalesTenderLiteV61 salesTender) {
        this.salesTender = salesTender;
    }

    public SalesMemoV61 getSalesMemo() {
        return SalesMemo;
    }

    public void setSalesMemo(SalesMemoV61 salesMemo) {
        SalesMemo = salesMemo;
    }
}
