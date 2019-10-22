package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class RedConfig implements Serializable {
    private Long id;
    
    @NotNull(message="{延迟发送时间   不能为空}")
    @Range(min=0,message="{延迟发送时间     不能小于0}")
    private Integer delay;
    
    @NotNull(message="{单个红包最小比例   不能为空}")
    @Range(min=0,message="{单个红包最小比例    不能小于0}")
    private Integer minRatio;

    @NotNull(message="{单个红包最大比例   不能为空}")
    @Range(min=0,message="{单个红包最大比例   不能小于0}")
    private Integer maxRatio;

    @NotNull(message="{单个最大红包   不能为空}")
    @Range(min=0,message="{单个最大红包   不能小于0}")
    private BigDecimal maxSingleRed;
    
    @NotNull(message="{单个最小红包   不能为空}")
    @Range(min=0,message="{单个最小红包   不能小于0}")
    private BigDecimal minSignleRed;

    @NotBlank(message="{红包标题   不能为空}")
    private String title;

    @NotBlank(message="{红包备注   不能为空}")
    private String remark;

    @NotNull(message="{是否可以叠加   不能为空}")
    private Byte isAddRatio;

    private BigDecimal minTranslateMoney;

    @NotNull(message="{是否启用   不能为空}")
    private Integer isActivity;

    private String shopDetailId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getMinRatio() {
        return minRatio;
    }

    public void setMinRatio(Integer minRatio) {
        this.minRatio = minRatio;
    }

    public Integer getMaxRatio() {
        return maxRatio;
    }

    public void setMaxRatio(Integer maxRatio) {
        this.maxRatio = maxRatio;
    }

    public BigDecimal getMaxSingleRed() {
        return maxSingleRed;
    }

    public void setMaxSingleRed(BigDecimal maxSingleRed) {
        this.maxSingleRed = maxSingleRed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public BigDecimal getMinSignleRed() {
        return minSignleRed;
    }

    public void setMinSignleRed(BigDecimal minSignleRed) {
        this.minSignleRed = minSignleRed;
    }

    public Byte getIsAddRatio() {
        return isAddRatio;
    }

    public void setIsAddRatio(Byte isAddRatio) {
        this.isAddRatio = isAddRatio;
    }

    public BigDecimal getMinTranslateMoney() {
        return minTranslateMoney;
    }

    public void setMinTranslateMoney(BigDecimal minTranslateMoney) {
        this.minTranslateMoney = minTranslateMoney;
    }

    public Integer getIsActivity() {
        return isActivity;
    }

    public void setIsActivity(Integer isActivity) {
        this.isActivity = isActivity;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }
}