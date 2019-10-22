package com.resto.brand.web.model;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/6/9.
 */
public class PosLoginConfig implements Serializable {
    private static final long serialVersionUID = 7281549524005896944L;
    private Long id;

    private String ip;

    private String userName;

    private String passWord;

    private Integer savePwd;

    private Integer autoLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Integer getSavePwd() {
        return savePwd;
    }

    public void setSavePwd(Integer savePwd) {
        this.savePwd = savePwd;
    }

    public Integer getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(Integer autoLogin) {
        this.autoLogin = autoLogin;
    }
}
