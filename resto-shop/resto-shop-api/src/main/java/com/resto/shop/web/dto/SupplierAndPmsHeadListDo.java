package com.resto.shop.web.dto;

import java.util.List;

/**
 * @author gcl
 * @create 2018/01/03 14:16
 * @desc
 **/
public class SupplierAndPmsHeadListDo {

    private Long supplierId;
    private String supName;
    private List<SupplierAndPmsHeadDo> supplierAndPmsHeadDos;

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

    public List<SupplierAndPmsHeadDo> getSupplierAndPmsHeadDos() {
        return supplierAndPmsHeadDos;
    }

    public void setSupplierAndPmsHeadDos(List<SupplierAndPmsHeadDo> supplierAndPmsHeadDos) {
        this.supplierAndPmsHeadDos = supplierAndPmsHeadDos;
    }
}
