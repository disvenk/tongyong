package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by KONATA on 2017/2/8.
 */
public class LRFExcelDto implements Serializable {
    private String posdate;

    private String addTime;

    private String addName;

    private String posId;

    private String tableNo;

    private String pfName;

    private String department;

    private String deputy;

    private String menuType;

    private String menuCode;

    private String menuName;

    private Integer quantity;

    private BigDecimal amount_1;

    private BigDecimal amount_2;

    private String accountName;

    private String pay_method;

    private String remark;

    public String getPosdate() {
        return posdate;
    }

    public void setPosdate(String posdate) {
        this.posdate = posdate;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAddName() {
        return addName;
    }

    public void setAddName(String addName) {
        this.addName = addName;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getPfName() {
        return pfName;
    }

    public void setPfName(String pfName) {
        this.pfName = pfName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount_1() {
        return amount_1;
    }

    public void setAmount_1(BigDecimal amount_1) {
        this.amount_1 = amount_1;
    }

    public BigDecimal getAmount_2() {
        return amount_2;
    }

    public void setAmount_2(BigDecimal amount_2) {
        this.amount_2 = amount_2;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
