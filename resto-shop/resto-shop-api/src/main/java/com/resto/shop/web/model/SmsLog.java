package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class SmsLog implements Serializable {

    private Long id;

    private String phone;

    private String content;

    private Date createTime;

    private Integer smsType;

    private String smsResult;

    private String shopDetailId;

    private String brandId;

    private Boolean isSuccess;
    
    /**
     * 短信验证码类型名字
     */
    private String smsLogTyPeName;
    
    public String getSmsLogTyPeName() {
		return smsLogTyPeName;
	}

	public void setSmsLogTyPeName(String smsLogTyPeName) {
		this.smsLogTyPeName = smsLogTyPeName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSmsType() {
        return smsType;
    }

    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    public String getSmsResult() {
        return smsResult;
    }

    public void setSmsResult(String smsResult) {
        this.smsResult = smsResult == null ? null : smsResult.trim();
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

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}