package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DocStockCountDetail  implements  Serializable  {
    private static final long serialVersionUID = 233389575928858215L;
    private Long id;

    private Long stockCountHeaderId;

    private String stockCountHeaderCode;

    private String lineStatus;

    private Long materialId;

    private Integer isDamaged;

    private BigDecimal theoryStockCount;

    private BigDecimal availableStockCount;

    private BigDecimal actStockCount;

    private BigDecimal defferentCount;//差异数量

    private String defferentReason;

    private String note;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private Long updaterId;

    private String updaterName;

    private Date gmtModified;

    private Integer isDelete;

    private Long stockCountUserId;

    private String stockCountUserName;



    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Long getStockCountHeaderId(){

        return this.stockCountHeaderId;
    }
    public void setStockCountHeaderId(Long stockCountHeaderId){

        this.stockCountHeaderId = stockCountHeaderId;
    }
    public String getStockCountHeaderCode(){

        return this.stockCountHeaderCode;
    }
    public void setStockCountHeaderCode(String stockCountHeaderCode){

        this.stockCountHeaderCode = stockCountHeaderCode;
    }
    public String getLineStatus(){

        return this.lineStatus;
    }
    public void setLineStatus(String lineStatus){

        this.lineStatus = lineStatus;
    }
    public Long getMaterialId(){

        return this.materialId;
    }
    public void setMaterialId(Long materialId){

        this.materialId = materialId;
    }
    public Integer getIsDamaged(){

        return this.isDamaged;
    }
    public void setIsDamaged(Integer isDamaged){

        this.isDamaged = isDamaged;
    }
    public BigDecimal getTheoryStockCount(){

        return this.theoryStockCount;
    }
    public void setTheoryStockCount(BigDecimal theoryStockCount){

        this.theoryStockCount = theoryStockCount;
    }
    public BigDecimal getAvailableStockCount(){

        return this.availableStockCount;
    }
    public void setAvailableStockCount(BigDecimal availableStockCount){

        this.availableStockCount = availableStockCount;
    }
    public BigDecimal getActStockCount(){

        return this.actStockCount;
    }
    public void setActStockCount(BigDecimal actStockCount){

        this.actStockCount = actStockCount;
    }
    public String getDefferentReason(){

        return this.defferentReason;
    }
    public void setDefferentReason(String defferentReason){

        this.defferentReason = defferentReason;
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
    public Long getUpdaterId(){

        return this.updaterId;
    }
    public void setUpdaterId(Long updaterId){

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
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
    }
    public Long getStockCountUserId(){

        return this.stockCountUserId;
    }
    public void setStockCountUserId(Long stockCountUserId){

        this.stockCountUserId = stockCountUserId;
    }
    public String getStockCountUserName(){

        return this.stockCountUserName;
    }
    public void setStockCountUserName(String stockCountUserName){

        this.stockCountUserName = stockCountUserName;
    }

    public BigDecimal getDefferentCount() {
        return defferentCount;
    }

    public void setDefferentCount(BigDecimal defferentCount) {
        this.defferentCount = defferentCount;
    }
}
