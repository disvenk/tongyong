package com.resto.shop.web.dto;

import java.io.Serializable;

/**
 * @author gcl
 * @create 2018/01/03 14:16
 * @desc
 **/
public class SupplierAndPmsHeadDo implements Serializable{

    private Long supplierId;
    private String supName;
    private  Long pmsHeadId;
    private String pmsOrderName;
    private String topContact;
    private String topEmail;
    private String topMobile;


    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getTopContact() {
        return topContact;
    }

    public void setTopContact(String topContact) {
        this.topContact = topContact;
    }


    public String getTopEmail() {
        return topEmail;
    }

    public void setTopEmail(String topEmail) {
        this.topEmail = topEmail;
    }

    public String getTopMobile() {
        return topMobile;
    }

    public void setTopMobile(String topMobile) {
        this.topMobile = topMobile;
    }

    public Long getPmsHeadId() {
        return pmsHeadId;
    }

    public void setPmsHeadId(Long pmsHeadId) {
        this.pmsHeadId = pmsHeadId;
    }

    public String getPmsOrderName() {
        return pmsOrderName;
    }

    public void setPmsOrderName(String pmsOrderName) {
        this.pmsOrderName = pmsOrderName;
    }

    @Override
    public String toString() {
        return "SupplierAndPmsHeadDo{" +
                "supplierId=" + supplierId +
                ", supName='" + supName + '\'' +
                ", pmsHeadId=" + pmsHeadId +
                ", pmsOrderName='" + pmsOrderName + '\'' +
                ", topContact='" + topContact + '\'' +
                ", topEmail='" + topEmail + '\'' +
                ", topMobile='" + topMobile + '\'' +
                '}';
    }
}
