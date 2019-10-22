package com.resto.shop.web.dto;

import java.util.List;

/**
 * @author gcl
 * @create 2018/01/03 14:16
 * @desc
 **/
public class SupplierAndSupPriceListDo {

    private Long supplierId;
    private String supName;
    private List<SupplierAndSupPriceDo> supplierAndSupPriceList;

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

    public List<SupplierAndSupPriceDo> getSupplierAndSupPriceList() {
        return supplierAndSupPriceList;
    }

    public void setSupplierAndSupPriceList(List<SupplierAndSupPriceDo> supplierAndSupPriceList) {
        this.supplierAndSupPriceList = supplierAndSupPriceList;
    }

    @Override
    public String toString() {
        return "SupplierAndSupPriceListDo{" +
                "supplierId=" + supplierId +
                ", supName='" + supName + '\'' +
                ", supplierAndSupPriceList=" + supplierAndSupPriceList +
                '}';
    }
}
