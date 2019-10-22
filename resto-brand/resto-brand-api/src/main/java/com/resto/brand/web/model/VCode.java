package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by carl on 2016/9/5.
 */
public class VCode implements Serializable {
    private Long id;                   //序号
    private String vCode;              //验证码
    private String tel;                //手机号
    private Integer maxTime;           //超时时间
    private Date createTime;           //创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
