package com.resto.shop.web.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class ShopCart implements Serializable {
    private Integer id;

    private Integer number;

    private String customerId;

    private String articleId;

    private String shopDetailId;

    private Integer distributionModeId;

    private String userId;

    private String shopType;

    private Integer pid;

    private List<ShopCart> currentItem;

    private Integer attrId;

    private String recommendId;

    private String recommendArticleId;

    private String unitNewId;

    private List<ShopCart> unitList;

    private List<ShopCart> weightList;

    private String uuid;

    private String groupId;

    private Long weightPackageId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<ShopCart> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<ShopCart> unitList) {
        this.unitList = unitList;
    }

    public String getUnitNewId() {
        return unitNewId;
    }

    public void setUnitNewId(String unitNewId) {
        this.unitNewId = unitNewId;
    }

    public String getRecommendArticleId() {
        return recommendArticleId;
    }

    public void setRecommendArticleId(String recommendArticleId) {
        this.recommendArticleId = recommendArticleId;
    }

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public List<ShopCart> getCurrentItem() { return currentItem; }

    public void setCurrentItem(List<ShopCart> currentItem) { this.currentItem = currentItem; }

    public String getShopType() { return shopType; }

    public void setShopType(String shopType) { this.shopType = shopType; }

    public Integer getPid() { return pid; }

    public void setPid(Integer pid) { this.pid = pid; }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public Integer getDistributionModeId() {
        return distributionModeId;
    }

    public void setDistributionModeId(Integer distributionModeId) {
        this.distributionModeId = distributionModeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getWeightPackageId() {
        return weightPackageId;
    }

    public void setWeightPackageId(Long weightPackageId) {
        this.weightPackageId = weightPackageId;
    }

    public List<ShopCart> getWeightList() {
        return weightList;
    }

    public void setWeightList(List<ShopCart> weightList) {
        this.weightList = weightList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("number", number)
                .append("customerId", customerId)
                .append("articleId", articleId)
                .append("shopDetailId", shopDetailId)
                .append("distributionModeId", distributionModeId)
                .append("userId", userId)
                .append("shopType", shopType)
                .append("pid", pid)
                .append("currentItem", currentItem)
                .append("attrId", attrId)
                .append("recommendId", recommendId)
                .append("recommendArticleId", recommendArticleId)
                .append("unitNewId", unitNewId)
                .append("unitList", unitList)
                .append("weightList", weightList)
                .append("uuid", uuid)
                .append("groupId", groupId)
                .append("weightPackageId", weightPackageId)
                .toString();
    }
}