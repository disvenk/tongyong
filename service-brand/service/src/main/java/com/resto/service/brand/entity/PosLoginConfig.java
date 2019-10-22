package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/6/9.
 */
@Data
public class PosLoginConfig implements Serializable {

    private static final long serialVersionUID = 7281549524005896944L;

    private Long id;

    private String ip;

    private String userName;

    private String passWord;

    private Integer savePwd;

    private Integer autoLogin;

}
