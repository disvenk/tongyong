package com.resto.shop.web.dto;

import com.resto.shop.web.model.NewAppraise;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xielc on 2018/6/7.
 */
public class NewAppraiseDto implements Serializable{

    private String name;

    private Integer appraiseNum;

    private String appraiseRatio;

    private BigDecimal totalMoney;

    private BigDecimal redMoney;

    private List<NewAppraise> newAppraises;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAppraiseNum() {
        return appraiseNum;
    }

    public void setAppraiseNum(Integer appraiseNum) {
        this.appraiseNum = appraiseNum;
    }

    public String getAppraiseRatio() {
        return appraiseRatio;
    }

    public void setAppraiseRatio(String appraiseRatio) {
        this.appraiseRatio = appraiseRatio;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }

    public List<NewAppraise> getNewAppraises() {
        return newAppraises;
    }

    public void setNewAppraises(List<NewAppraise> newAppraises) {
        this.newAppraises = newAppraises;
    }
}
