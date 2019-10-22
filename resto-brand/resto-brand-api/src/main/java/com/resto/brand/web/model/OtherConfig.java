package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.Date;

public class OtherConfig implements Serializable {
    private Long id;

    private String configSign;

    private String configName;

    private String configRemark;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigSign() {
        return configSign;
    }

    public void setConfigSign(String configSign) {
        this.configSign = configSign == null ? null : configSign.trim();
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName == null ? null : configName.trim();
    }

    public String getConfigRemark() {
        return configRemark;
    }

    public void setConfigRemark(String configRemark) {
        this.configRemark = configRemark == null ? null : configRemark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}