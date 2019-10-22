package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by carl on 2016/11/20.
 */
public class AppraiseFile implements Serializable {

    private String id;

    private String appraiseId;

    private Date createTime;

    private String fileUrl;

    private String shopDetailId;

    private Integer sort;

    private String photoSquare;

    private String fileName;

    private Integer state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPhotoSquare() {
        return photoSquare;
    }

    public void setPhotoSquare(String photoSquare) {
        this.photoSquare = photoSquare;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
