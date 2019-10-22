package com.resto.brand.web.dto;

import java.io.Serializable;

/**
 * Created by yz on 2017-04-18.
 */
public class ArticleTopDto implements Serializable {

    private  Integer id;
    private  String name;//名字
    private  Integer num;//个数
    private  Integer type;//星数 目前没用

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
