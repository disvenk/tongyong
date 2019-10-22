package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class BonusSetting implements Serializable {
    private String id;

    private String chargeSettingId;

    private String shopDetailId;

    private String brandId;

    private Integer chargeBonusRatio;

    private Integer shopownerBonusRatio;

    private Integer employeeBonusRatio;

    private Boolean state;

    private Date createTime;

    private Date updateTime;

    private String shopName;

    private String chargeName;

    private String wishing;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getChargeSettingId() {
        return chargeSettingId;
    }

    public void setChargeSettingId(String chargeSettingId) {
        this.chargeSettingId = chargeSettingId == null ? null : chargeSettingId.trim();
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public Integer getChargeBonusRatio() {
        return chargeBonusRatio;
    }

    public void setChargeBonusRatio(Integer chargeBonusRatio) {
        this.chargeBonusRatio = chargeBonusRatio;
    }

    public Integer getShopownerBonusRatio() {
        return shopownerBonusRatio;
    }

    public void setShopownerBonusRatio(Integer shopownerBonusRatio) {
        this.shopownerBonusRatio = shopownerBonusRatio;
    }

    public Integer getEmployeeBonusRatio() {
        return employeeBonusRatio;
    }

    public void setEmployeeBonusRatio(Integer employeeBonusRatio) {
        this.employeeBonusRatio = employeeBonusRatio;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }
}