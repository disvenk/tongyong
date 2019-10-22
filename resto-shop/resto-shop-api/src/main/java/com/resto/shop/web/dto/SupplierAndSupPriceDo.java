package com.resto.shop.web.dto;

import java.io.Serializable;

/**
 * @author gcl
 * @create 2018/01/03 14:16
 * @desc
 **/
public class SupplierAndSupPriceDo implements Serializable{

    private Long supplierId;
    private String supName;
    private  Long supPriceId;
    private String priceName;
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

    public Long getSupPriceId() {
        return supPriceId;
    }

    public void setSupPriceId(Long supPriceId) {
        this.supPriceId = supPriceId;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
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

    @Override
    public String toString() {
        return "SupplierAndSupPriceDo{" +
                "supplierId=" + supplierId +
                ", supName='" + supName + '\'' +
                ", supPriceId=" + supPriceId +
                ", priceName='" + priceName + '\'' +
                ", topContact='" + topContact + '\'' +
                ", topEmail='" + topEmail + '\'' +
                ", topMobile='" + topMobile + '\'' +
                '}';
    }
}
