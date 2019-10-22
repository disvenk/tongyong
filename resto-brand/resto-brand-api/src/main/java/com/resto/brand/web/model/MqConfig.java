package com.resto.brand.web.model;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/7/12.
 */
public class MqConfig implements Serializable {
    private static final long serialVersionUID = 5638032279138040439L;

    private Long id;

    private String topic;

    private String cidShop;

    private String pid;

    private String cidPos;

    private String mqName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCidShop() {
        return cidShop;
    }

    public void setCidShop(String cidShop) {
        this.cidShop = cidShop;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCidPos() {
        return cidPos;
    }

    public void setCidPos(String cidPos) {
        this.cidPos = cidPos;
    }

    public String getMqName() {
        return mqName;
    }

    public void setMqName(String mqName) {
        this.mqName = mqName;
    }
}
