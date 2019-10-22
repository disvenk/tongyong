package com.resto.shop.web.dto;

import java.io.Serializable;
import java.util.List;

public class KitchenDto  implements Serializable {

    private Integer jsonDtoId;

    private String name;

    private Integer kitchenId;

    public Integer getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(Integer kitchenId) {
        this.kitchenId = kitchenId;
    }

    List<PrinterDto> children;

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

    public List<PrinterDto> getChildren() {
        return children;
    }

    public void setChildren(List<PrinterDto> children) {
        this.children = children;
    }
}
