package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RedPacketDto implements Serializable {

    private String shopDetailId;
    private String shopName;
    private BigDecimal redCount;
    private BigDecimal redMoney;
    private BigDecimal useRedCount;
    private BigDecimal useRedMoney;
    private String useRedCountRatio;
    private String useRedMoneyRatio;
    private BigDecimal useRedOrderCount;
    private BigDecimal useRedOrderMoney;
    private String redPacketId;
    private List<Map<String, Object>> shopRedInfoList;
    private Map<String, Object> brandRedInfo;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getRedCount() {
        return redCount;
    }

    public void setRedCount(BigDecimal redCount) {
        this.redCount = redCount;
    }

    public BigDecimal getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }

    public BigDecimal getUseRedCount() {
        return useRedCount;
    }

    public void setUseRedCount(BigDecimal useRedCount) {
        this.useRedCount = useRedCount;
    }

    public BigDecimal getUseRedMoney() {
        return useRedMoney;
    }

    public void setUseRedMoney(BigDecimal useRedMoney) {
        this.useRedMoney = useRedMoney;
    }

    public String getUseRedCountRatio() {
        return useRedCountRatio;
    }

    public void setUseRedCountRatio(String useRedCountRatio) {
        this.useRedCountRatio = useRedCountRatio;
    }

    public String getUseRedMoneyRatio() {
        return useRedMoneyRatio;
    }

    public void setUseRedMoneyRatio(String useRedMoneyRatio) {
        this.useRedMoneyRatio = useRedMoneyRatio;
    }

    public BigDecimal getUseRedOrderCount() {
        return useRedOrderCount;
    }

    public void setUseRedOrderCount(BigDecimal useRedOrderCount) {
        this.useRedOrderCount = useRedOrderCount;
    }

    public BigDecimal getUseRedOrderMoney() {
        return useRedOrderMoney;
    }

    public void setUseRedOrderMoney(BigDecimal useRedOrderMoney) {
        this.useRedOrderMoney = useRedOrderMoney;
    }

    public RedPacketDto() {
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public List<Map<String, Object>> getShopRedInfoList() {
        return shopRedInfoList;
    }

    public void setShopRedInfoList(List<Map<String, Object>> shopRedInfoList) {
        this.shopRedInfoList = shopRedInfoList;
    }

    public Map<String, Object> getBrandRedInfo() {
        return brandRedInfo;
    }

    public void setBrandRedInfo(Map<String, Object> brandRedInfo) {
        this.brandRedInfo = brandRedInfo;
    }

    public RedPacketDto(String shopDetailId, String shopName, BigDecimal redCount, BigDecimal redMoney, BigDecimal useRedCount, BigDecimal useRedMoney, String useRedCountRatio, String useRedMoneyRatio, BigDecimal useRedOrderCount, BigDecimal useRedOrderMoney) {
        this.shopDetailId = shopDetailId;
        this.shopName = shopName;
        this.redCount = redCount;
        this.redMoney = redMoney;
        this.useRedCount = useRedCount;
        this.useRedMoney = useRedMoney;
        this.useRedCountRatio = useRedCountRatio;
        this.useRedMoneyRatio = useRedMoneyRatio;
        this.useRedOrderCount = useRedOrderCount;
        this.useRedOrderMoney = useRedOrderMoney;
    }

}
