package com.resto.brand.web.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class WechatConfig implements Serializable {
    private String id;

    @NotBlank(message="微信appId不能为空")
    private String appid;

    @NotBlank(message="微信appSecret不能为空")
    private String appsecret;

    private String cardId;//微信会员卡id

    private String mchid;

    private String mchkey;
    
    private String payCertPath;
    
    
    private Integer state;
    
    
    //关联查询的 品牌 名称
    private String brandName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret == null ? null : appsecret.trim();
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid == null ? null : mchid.trim();
    }

    public String getMchkey() {
        return mchkey;
    }

    public void setMchkey(String mchkey) {
        this.mchkey = mchkey == null ? null : mchkey.trim();
    }

    public Integer getState() {
		return (state == null || "".equals(state)) ? 1 : state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
    
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName == null ? null : brandName.trim();
	}

	public String getPayCertPath() {
		return payCertPath;
	}

	public void setPayCertPath(String payCertPath) {
		this.payCertPath = payCertPath;
	}

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}