package com.resto.brand.core.meituanUtils.domain;

/**
 * 开放平台域名
 * <p>
 * Created by cuibaosen on 16/8/17.
 */
public enum RequestDomain {
    preUrl("http://api.open.cater.meituan.com");

    private String value;

    RequestDomain(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
