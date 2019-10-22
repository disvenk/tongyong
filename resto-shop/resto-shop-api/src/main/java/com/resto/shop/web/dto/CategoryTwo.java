package com.resto.shop.web.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bruce on 2017-09-21 09:44
 */
public class CategoryTwo implements Serializable{

    private static final long serialVersionUID = 5087237024322127988L;

    private Long id;
    private String name;
    private Long parentId;

    private List<CategoryThree> threeList;

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

    public List<CategoryThree> getThreeList() {
        return threeList;
    }

    public void setThreeList(List<CategoryThree> threeList) {
        this.threeList = threeList;
    }
}
