package com.resto.brand.web.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/12/26.
 * 微信充值账户位置
 */
public class WechatChargeConfig implements Serializable {


    private static final long serialVersionUID = -2832455612206919794L;

    private String id;

    @NotBlank(message="微信appId不能为空")
    private String appid;

    @NotBlank(message="微信appSecret不能为空")
    private String appsecret;

    private String mchid;

    private String mchkey;

    private String payCertPath;

    private Long wxServerId;

    private Integer isSub;

    public Integer getIsSub() {
        return isSub;
    }

    public void setIsSub(Integer isSub) {
        this.isSub = isSub;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getWxServerId() {
        return wxServerId;
    }

    public void setWxServerId(Long wxServerId) {
        this.wxServerId = wxServerId;
    }
}
