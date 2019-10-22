package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.InputMismatchException;

public class ShareSetting implements Serializable {
    private String id;

    private String shareTitle;

    private String shareIcon;

    private Integer minLevel;

    private Integer minLength;

    private BigDecimal rebate;

    private Boolean isActivity;

    private String brandId;

    private String dialogText;

    private String brandName;

    private BigDecimal minMoney;

    private BigDecimal maxMoney;

    private String registerButton;

    private Integer delayTime;

    private Integer openMultipleRebates;

    private BigDecimal afterRebate;

    private BigDecimal afterMinMoney;

    private BigDecimal afterMaxMoney;

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle == null ? null : shareTitle.trim();
    }

    public String getShareIcon() {
        return shareIcon;
    }

    public void setShareIcon(String shareIcon) {
        this.shareIcon = shareIcon == null ? null : shareIcon.trim();
    }

    public Integer getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public Boolean getIsActivity() {
        return isActivity;
    }

    public void setIsActivity(Boolean isActivity) {
        this.isActivity = isActivity;
    }


    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getDialogText() {
        return dialogText;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText == null ? null : dialogText.trim();
    }

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public String getRegisterButton() {
		return registerButton;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public void setRegisterButton(String registerButton) {
		this.registerButton = registerButton;
	}

    public Boolean getActivity() {
        return isActivity;
    }

    public void setActivity(Boolean activity) {
        isActivity = activity;
    }

    public Integer getOpenMultipleRebates() {
        return openMultipleRebates;
    }

    public void setOpenMultipleRebates(Integer openMultipleRebates) {
        this.openMultipleRebates = openMultipleRebates;
    }

    public BigDecimal getAfterRebate() {
        return afterRebate;
    }

    public void setAfterRebate(BigDecimal afterRebate) {
        this.afterRebate = afterRebate;
    }

    public BigDecimal getAfterMinMoney() {
        return afterMinMoney;
    }

    public void setAfterMinMoney(BigDecimal afterMinMoney) {
        this.afterMinMoney = afterMinMoney;
    }

    public BigDecimal getAfterMaxMoney() {
        return afterMaxMoney;
    }

    public void setAfterMaxMoney(BigDecimal afterMaxMoney) {
        this.afterMaxMoney = afterMaxMoney;
    }
}