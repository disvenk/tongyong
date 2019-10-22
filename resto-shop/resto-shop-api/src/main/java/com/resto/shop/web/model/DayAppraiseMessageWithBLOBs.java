package com.resto.shop.web.model;

import java.io.Serializable;

public class DayAppraiseMessageWithBLOBs extends DayAppraiseMessage implements Serializable {
    private String redList;

    private String badList;

    public String getRedList() {
        return redList;
    }

    public void setRedList(String redList) {
        this.redList = redList == null ? null : redList.trim();
    }

    public String getBadList() {
        return badList;
    }

    public void setBadList(String badList) {
        this.badList = badList == null ? null : badList.trim();
    }
}