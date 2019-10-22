package com.resto.brand.web.model;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/12/3.
 * 微信服务商配置
 */
public class WxServerConfig implements Serializable {

    //主键
    private Long id;
    //服务商名称
    private String name;
    //appid
    private String appid;
    //app密钥
    private String appsecret;
    //支付id
    private String mchid;
    //支付密钥
    private String mchkey;
    //证书路径
    private String payCertPath;
    //删除标记(1-未删除 0删除）
    private Integer deletedFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getMchkey() {
        return mchkey;
    }

    public void setMchkey(String mchkey) {
        this.mchkey = mchkey;
    }

    public String getPayCertPath() {
        return payCertPath;
    }

    public void setPayCertPath(String payCertPath) {
        this.payCertPath = payCertPath;
    }

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}
