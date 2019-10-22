package com.resto.pos.web.entity;

import java.io.Serializable;

/**
 *  安卓Tv端，登录后的返回的数据
 * Created by Lmx on 2017/1/19.
 */
public class TvUserData implements Serializable{
//    private String ip;
//    private int port;
    private String shopId;
    private String brandId;
    private String url;
    private Integer tvShopMode;     //店铺开启的电视叫号模式

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTvShopMode() {
        return tvShopMode;
    }

    public void setTvShopMode(Integer tvShopMode) {
        this.tvShopMode = tvShopMode;
    }
}
