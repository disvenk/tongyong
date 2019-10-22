package com.resto.shop.web.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存盘点单
 *
 * Created by bruce on 2017-09-15 11:45
 */
public class DocStockInput {

    private String orderName;
    private String createName;
    private String createId;
    private String startTime;
    private String endTime;
    private String shopDetailId;
    private List<DocStockCountDetailDo> docStockCountDetailDo =new ArrayList<>();

    public String getOrderName() {
        return orderName;
    }


    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<DocStockCountDetailDo> getDocStockCountDetailDo() {
        return docStockCountDetailDo;
    }

    public void setDocStockCountDetailDo(List<DocStockCountDetailDo> docStockCountDetailDo) {
        this.docStockCountDetailDo = docStockCountDetailDo;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }
}
