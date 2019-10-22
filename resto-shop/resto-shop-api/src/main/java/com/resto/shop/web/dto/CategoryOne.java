package com.resto.shop.web.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bruce on 2017-09-21 09:43
 */
public class CategoryOne implements Serializable{

    private static final long serialVersionUID = 5973996273004280813L;

    private Long id;
    private String name;
    private Long parentId;

    private List<CategoryTwo> twoList;

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

    public List<CategoryTwo> getTwoList() {
        return twoList;
    }

    public void setTwoList(List<CategoryTwo> twoList) {
        this.twoList = twoList;
    }
}
