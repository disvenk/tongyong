package com.resto.shop.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-23 17:27
 */
public class UnionArticleForm implements Serializable{
    public Long couponId;
    public List<ShopUnionArticle> shopUnionArticle = new ArrayList<>();
}
