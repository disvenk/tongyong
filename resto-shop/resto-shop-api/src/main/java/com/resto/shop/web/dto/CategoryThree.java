package com.resto.shop.web.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bruce on 2017-09-21 09:45
 */
public class CategoryThree implements Serializable{

    private static final long serialVersionUID = 4106137312912871354L;

    private Long id;
    private String name;
    private Long parentId;
    private String shopId;

    private List<MaterialDo> materialList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<MaterialDo> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<MaterialDo> materialList) {
        this.materialList = materialList;
    }


}
