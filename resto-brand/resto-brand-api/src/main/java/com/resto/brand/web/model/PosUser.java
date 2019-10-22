package com.resto.brand.web.model;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/6/20.
 */
public class PosUser implements Serializable {

    private Long id;

    private String ip;

    private String userName;

    private String passWord;

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
}
