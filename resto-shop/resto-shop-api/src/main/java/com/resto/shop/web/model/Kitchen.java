package com.resto.shop.web.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public class Kitchen implements Serializable {
    private Integer id;

    @NotBlank(message="{厨房名称   不能为空}")
    private String name;

    private String remark;

    
    private Integer printerId;

    private String shopDetailId;

    //关联查询 打印机的名称
    private String printerName;

    private String ciprinterId;

    private List<Integer> ciprinterList;

    private String beginTime;

    private String endTime;

    private Integer sort;

    private Integer status;

    private String brandId;

    private String enableTime;

    private String statusName;

    private List<Printer> printers;

    public List<Printer> getPrinters() {
        return printers;
    }

    public void setPrinters(List<Printer> printers) {
        this.printers = printers;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(String enableTime) {
        this.enableTime = enableTime;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Integer printerId) {
        this.printerId = printerId;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName == null ? null : printerName.trim();
	}

    public String getCiprinterId() {
        return ciprinterId;
    }

    public void setCiprinterId(String ciprinterId) {
        this.ciprinterId = ciprinterId;
    }

    public List<Integer> getCiprinterList() {
        return ciprinterList;
    }

    public void setCiprinterList(List<Integer> ciprinterList) {
        this.ciprinterList = ciprinterList;
    }
}