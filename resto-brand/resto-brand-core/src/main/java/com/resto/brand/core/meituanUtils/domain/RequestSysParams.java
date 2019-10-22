package com.resto.brand.core.meituanUtils.domain;

/**
 * Created by cuibaosen on 16/8/16.
 */
public class RequestSysParams {
    /**
     * 开发者密钥
     */
    private String secret;
    /**
     * 开发者认证token
     */
    private String appAuthToken;
    /**
     * 编码
     */
    private String charset = "utf8";

    public RequestSysParams() {
    }

    public RequestSysParams(String secret, String appAuthToken) {
        this.secret = secret;
        this.appAuthToken = appAuthToken;
    }

    public RequestSysParams(String secret, String appAuthToken, String charset) {
        if (charset == null || charset.trim().isEmpty()) {
            throw new RuntimeException("charset不能为空");
        }
        String lowerCharset = charset.toLowerCase();
        boolean supported = lowerCharset.equals("utf8") || lowerCharset.equals("utf-8")
                || lowerCharset.equals("gbk") || lowerCharset.equals("gb2312");
        if (!supported) {
            throw new RuntimeException("不支持charset类型");
        }
        this.secret = secret;
        this.appAuthToken = appAuthToken;
        this.charset = charset;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public String getCharset() {
        return charset == null ? null : charset;
    }

    public void setCharset(String charset) {
        if (charset == null || charset.trim().isEmpty()) {
            throw new RuntimeException("charset不能为空");
        }
        String lowerCharset = charset.toLowerCase();
        boolean supported = lowerCharset.equals("utf8") || lowerCharset.equals("utf-8")
                || lowerCharset.equals("gbk") || lowerCharset.equals("gb2312");
        if (!supported) {
            throw new RuntimeException("不支持charset类型");
        }

        this.charset = charset;
    }
}
