package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class MdSupplierContact  implements  Serializable  {
    private Long id;

    private Long supplierId;

    private String supCode;

    private String contact;

    private String tel;

    private String mobile;

    private String email;

    private Integer isTop;

    private String createrId;

    private String createrName;

    private String updaterId;

    private String updaterName;

    private Date gmtCreate;

    private Integer isDelete;

    private Date gmtModified;

    private String position;

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Long getSupplierId(){

        return this.supplierId;
    }
    public void setSupplierId(Long supplierId){

        this.supplierId = supplierId;
    }
    public String getSupCode(){

        return this.supCode;
    }
    public void setSupCode(String supCode){

        this.supCode = supCode;
    }
    public String getContact(){

        return this.contact;
    }
    public void setContact(String contact){

        this.contact = contact;
    }
    public String getTel(){

        return this.tel;
    }
    public void setTel(String tel){

        this.tel = tel;
    }
    public String getMobile(){

        return this.mobile;
    }
    public void setMobile(String mobile){

        this.mobile = mobile;
    }
    public String getEmail(){

        return this.email;
    }
    public void setEmail(String email){

        this.email = email;
    }
    public Integer getIsTop(){

        return this.isTop;
    }
    public void setIsTop(Integer isTop){

        this.isTop = isTop;
    }
    public String getCreaterId(){

        return this.createrId;
    }
    public void setCreaterId(String createrId){

        this.createrId = createrId;
    }
    public String getCreaterName(){

        return this.createrName;
    }
    public void setCreaterName(String createrName){

        this.createrName = createrName;
    }
    public String getUpdaterId(){

        return this.updaterId;
    }
    public void setUpdaterId(String updaterId){

        this.updaterId = updaterId;
    }
    public String getUpdaterName(){

        return this.updaterName;
    }
    public void setUpdaterName(String updaterName){

        this.updaterName = updaterName;
    }
    public Date getGmtCreate(){

        return this.gmtCreate;
    }
    public void setGmtCreate(Date gmtCreate){

        this.gmtCreate = gmtCreate;
    }
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
    }
    public Date getGmtModified(){

        return this.gmtModified;
    }
    public void setGmtModified(Date gmtModified){

        this.gmtModified = gmtModified;
    }
    public String getPosition(){

        return this.position;
    }
    public void setPosition(String position){

        this.position = position;
    }
}
