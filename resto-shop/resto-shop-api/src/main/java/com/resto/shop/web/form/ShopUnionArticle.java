package com.resto.shop.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-23 17:29
 */
public class ShopUnionArticle implements Serializable{
    public String id;
    public String shopName;
    public List<Articles> articleList = new ArrayList<>();
}
