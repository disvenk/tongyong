package com.resto.shop.web.constant;

import com.resto.shop.web.model.ERole;

import java.util.List;

//封装一个店铺多个角色
public class ERoleDto {
    private String shopId;
    private String shopName;
    private List<ERole> eRolelist;


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<ERole> geteRolelist() {
        return eRolelist;
    }

    public void seteRolelist(List<ERole> eRolelist) {
        this.eRolelist = eRolelist;
    }
}