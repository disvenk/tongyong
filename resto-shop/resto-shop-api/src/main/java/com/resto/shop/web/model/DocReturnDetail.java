package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class DocReturnDetail implements  Serializable  {

    private static final long serialVersionUID = 2999812223072710157L;
    private Long id;

    private Long returnHeaderId;

    private String returnHeaderCode;

    private String lineStatus;

    private Integer returnCount;

    private Long materialId;

    private String note;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private String updaterId;

    private String updaterName;

    private Date gmtModified;

    private String auditor;

    private Integer isDelete;

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Long getReturnHeaderId(){

        return this.returnHeaderId;
    }
    public void setReturnHeaderId(Long returnHeaderId){

        this.returnHeaderId = returnHeaderId;
    }
    public String getReturnHeaderCode(){

        return this.returnHeaderCode;
    }
    public void setReturnHeaderCode(String returnHeaderCode){

        this.returnHeaderCode = returnHeaderCode;
    }
    public String getLineStatus(){

        return this.lineStatus;
    }
    public void setLineStatus(String lineStatus){

        this.lineStatus = lineStatus;
    }
    public Integer getReturnCount(){

        return this.returnCount;
    }
    public void setReturnCount(Integer returnCount){

        this.returnCount = returnCount;
    }
    public Long getMaterialId(){

        return this.materialId;
    }
    public void setMaterialId(Long materialId){

        this.materialId = materialId;
    }
    public String getNote(){

        return this.note;
    }
    public void setNote(String note){

        this.note = note;
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
    public Date getGmtCreate(){

        return this.gmtCreate;
    }
    public void setGmtCreate(Date gmtCreate){

        this.gmtCreate = gmtCreate;
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
    public Date getGmtModified(){

        return this.gmtModified;
    }
    public void setGmtModified(Date gmtModified){

        this.gmtModified = gmtModified;
    }
    public String getAuditor(){

        return this.auditor;
    }
    public void setAuditor(String auditor){

        this.auditor = auditor;
    }
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
    }
}
