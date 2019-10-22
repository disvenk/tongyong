package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class MdCategory  implements  Serializable  {
    private Long id;

    private String categoryCode;

    private String categoryName;

    private String categoryDesc;

    private Integer categoryHierarchy;

    private Long parentId;

    private Integer isLeaf;

    private Integer activeStatus;

    private Integer isDelete;

    private Date gmtCreate;

    private Date gmtModified;

    private String shopDetailId;

    private String brandId;

    private Integer sort;

    private String keyword;

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCategoryCode(){

        return this.categoryCode;
    }
    public void setCategoryCode(String categoryCode){

        this.categoryCode = categoryCode;
    }
    public String getCategoryName(){

        return this.categoryName;
    }
    public void setCategoryName(String categoryName){

        this.categoryName = categoryName;
    }
    public String getCategoryDesc(){

        return this.categoryDesc;
    }
    public void setCategoryDesc(String categoryDesc){

        this.categoryDesc = categoryDesc;
    }
    public Integer getCategoryHierarchy(){

        return this.categoryHierarchy;
    }
    public void setCategoryHierarchy(Integer categoryHierarchy){

        this.categoryHierarchy = categoryHierarchy;
    }
    public Long getParentId(){

        return this.parentId;
    }
    public void setParentId(Long parentId){

        this.parentId = parentId;
    }
    public Integer getIsLeaf(){

        return this.isLeaf;
    }
    public void setIsLeaf(Integer isLeaf){

        this.isLeaf = isLeaf;
    }
    public Integer getActiveStatus(){

        return this.activeStatus;
    }
    public void setActiveStatus(Integer activeStatus){

        this.activeStatus = activeStatus;
    }
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
    }
    public Date getGmtCreate(){

        return this.gmtCreate;
    }
    public void setGmtCreate(Date gmtCreate){

        this.gmtCreate = gmtCreate;
    }
    public Date getGmtModified(){

        return this.gmtModified;
    }
    public void setGmtModified(Date gmtModified){

        this.gmtModified = gmtModified;
    }
    public String getShopDetailId(){

        return this.shopDetailId;
    }
    public void setShopDetailId(String shopDetailId){

        this.shopDetailId = shopDetailId;
    }
    public Integer getSort(){

        return this.sort;
    }
    public void setSort(Integer sort){

        this.sort = sort;
    }
    public String getKeyword(){

        return this.keyword;
    }
    public void setKeyword(String keyword){

        this.keyword = keyword;
    }
}
