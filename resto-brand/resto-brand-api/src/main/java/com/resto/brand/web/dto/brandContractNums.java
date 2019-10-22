package com.resto.brand.web.dto;

import java.io.Serializable;

/**
 *为了方便 选择品牌的时候 自动显示公司名称
 * 和多个合同编号
 */
public class brandContractNums implements Serializable {

    private String brandName; //品牌名

    private String bCompanyName;//公司名

    private String contractNums;//合同编号 从数据库查询完后如果有多个则会用，隔开


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getbCompanyName() {
        return bCompanyName;
    }

    public void setbCompanyName(String bCompanyName) {
        this.bCompanyName = bCompanyName;
    }

    public String getContractNums() {
        return contractNums;
    }

    public void setContractNums(String contractNums) {
        this.contractNums = contractNums;
    }
}


