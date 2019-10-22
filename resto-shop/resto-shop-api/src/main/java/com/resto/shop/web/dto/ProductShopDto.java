package com.resto.shop.web.dto;

import com.resto.shop.web.model.ArticleFamily;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-20 14:53
 */
@Data
public class ProductShopDto implements Serializable{
    public String shopId;
    public String shopName;
    public List<ArticleFamily> list;
}
