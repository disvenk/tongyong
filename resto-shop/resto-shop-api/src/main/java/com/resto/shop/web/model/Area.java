package com.resto.shop.web.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by KONATA on 2017/4/5.
 */
public class Area implements Serializable {
    //主键
    private Long id;
    //区域名称
    private String name;
    //打印机id
    private Long printId;
    //打印机
    private Printer printer;
    //是否启用 0-启用 1-不启用
    private Integer isDelete;
    //创建时间
    private Date createTime;
    //店铺id
    private String shopDetailId;

    private String printerName;
    //品牌id
    private String brandId;

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

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

    public Long getPrintId() {
        return printId;
    }

    public void setPrintId(Long printId) {
        this.printId = printId;
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Area(){}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("printId", printId)
                .append("printer", printer)
                .append("isDelete", isDelete)
                .append("createTime", createTime)
                .append("shopDetailId", shopDetailId)
                .append("brandId", brandId)
                .toString();
    }
}
