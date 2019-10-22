package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by carl on 2016/9/5.
 */
@Data
public class VCode implements Serializable {

    private static final long serialVersionUID = 9109974029010356712L;
    private Long id;                   //序号
    private String vCode;              //验证码
    private String tel;                //手机号
    private Integer maxTime;           //超时时间
    private Date createTime;           //创建时间
}
