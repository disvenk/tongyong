package com.resto.shop.web.posDto;

import java.io.Serializable;

/**
 * Created by xielc on 2018/6/1.
 */
public class ShopMsgChangeDto implements Serializable {

    private static final long serialVersionUID = 1040633847172891125L;

    private  String brandId;

    private  String shopId;

    private String tableName;

    private String type;

    private String id;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
