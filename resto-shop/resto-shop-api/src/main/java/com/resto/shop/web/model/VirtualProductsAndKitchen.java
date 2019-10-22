package com.resto.shop.web.model;

import java.io.Serializable;

/**
 * Created by yangwei on 2017/2/22.
 */
public class VirtualProductsAndKitchen implements Serializable {
    private int virtualId;
    private int kitchenId;

    public int getVirtualId() {
        return virtualId;
    }

    public void setVirtualId(int virtualId) {
        this.virtualId = virtualId;
    }

    public int getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(int kitchenId) {
        this.kitchenId = kitchenId;
    }
}
