package com.resto.shop.web.posDto;

import com.resto.shop.web.model.Customer;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/8/17.
 */
public class CustomerDto implements Serializable {
    private static final long serialVersionUID = 9167711475915815091L;

    public CustomerDto() {
    }

    public CustomerDto(Customer customer) {
        this.id = customer.getId() == null ? "" : customer.getId();
        this.wechatId = customer.getWechatId() == null ? "" : customer.getWechatId();
        this.nickname = customer.getNickname() == null ? "" : customer.getNickname();
        this.telephone = customer.getTelephone() == null ? "" : customer.getTelephone();
        this.sex = customer.getSex() == null ? 0 : customer.getSex();
    }


    private String id;

    private String wechatId;

    private String nickname;

    private String telephone;

    private Integer sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
