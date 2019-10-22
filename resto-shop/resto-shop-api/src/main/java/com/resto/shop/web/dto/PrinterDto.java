package com.resto.shop.web.dto;

import java.io.Serializable;

public class PrinterDto  implements Serializable {

    private Integer jsonDtoId;

    private String name;

    private Integer kitchenId;

    private String kitchenName;

    public String getKitchenName() {
        return kitchenName;
    }

    public void setKitchenName(String kitchenName) {
        this.kitchenName = kitchenName;
    }

    public Integer getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(Integer kitchenId) {
        this.kitchenId = kitchenId;
    }

    public Integer getJsonDtoId() {
        return jsonDtoId;
    }

    public void setJsonDtoId(Integer jsonDtoId) {
        this.jsonDtoId = jsonDtoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
