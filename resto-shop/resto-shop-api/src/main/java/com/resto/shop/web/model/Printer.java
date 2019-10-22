package com.resto.shop.web.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Printer implements Serializable {
    private Integer id;

//    @NotBlank(message="{打印机名称 不能为空}")
    private String name;

//    @NotBlank(message="{IP 地址不能为空}")
    private String ip;
    
//    @NotBlank(message="{端口号 不能为空}")
    private String port;

    private String shopDetailId;
    
    private Integer printType;

    //小票类型 0小票 1贴纸
    private Integer ticketType;

    //打印机范围 0-前台打印机 1-区域打印机
    private Integer range;

    //是否关联钱箱 0-否 1-是
    private Integer receiveMoney;

    private Integer billOfAccount;

    private Integer billOfConsumption;

    private Integer supportTangshi;

    private Integer supportWaidai;

    private Integer supportWaimai;

    private Integer state;

    private String spareIp;

    public String getSpareIp() {
        return spareIp;
    }

    public void setSpareIp(String spareIp) {
        this.spareIp = spareIp;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getBillOfAccount() {
        return billOfAccount;
    }

    public void setBillOfAccount(Integer billOfAccount) {
        this.billOfAccount = billOfAccount;
    }

    public Integer getBillOfConsumption() {
        return billOfConsumption;
    }

    public void setBillOfConsumption(Integer billOfConsumption) {
        this.billOfConsumption = billOfConsumption;
    }

    private List<Map<String,Object>> taskList;

    public List<Map<String, Object>> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Map<String, Object>> taskList) {
        this.taskList = taskList;
    }

    public Integer getReceiveMoney() {
        return receiveMoney;
    }

    public void setReceiveMoney(Integer receiveMoney) {
        this.receiveMoney = receiveMoney;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }

    public Integer getPrintType() {
		return printType;
	}

	public void setPrintType(Integer printType) {
		this.printType = printType;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public Integer getSupportTangshi() {
        return supportTangshi;
    }

    public void setSupportTangshi(Integer supportTangshi) {
        this.supportTangshi = supportTangshi;
    }

    public Integer getSupportWaidai() {
        return supportWaidai;
    }

    public void setSupportWaidai(Integer supportWaidai) {
        this.supportWaidai = supportWaidai;
    }

    public Integer getSupportWaimai() {
        return supportWaimai;
    }

    public void setSupportWaimai(Integer supportWaimai) {
        this.supportWaimai = supportWaimai;
    }
}