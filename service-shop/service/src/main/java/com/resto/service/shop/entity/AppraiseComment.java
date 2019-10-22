package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AppraiseComment implements Serializable {

    private static final long serialVersionUID = 4864271636777136512L;

    private String id;

    private String appraiseId;

    private Date createTime;

    private String customerId;

    private Integer isDel;

    private String shopDetailId;

    private String brandId;

    private String pid;

    private String content;

    private String nickName;

    /**
     * 回复的对象昵称
     */
    private String replyName;

}
