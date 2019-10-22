package com.resto.shop.web;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xielc on 2018/11/12.
 */
@Data
public class CheckShopMenuDto implements Serializable{

    private static final long serialVersionUID = 8918078132048402296L;

    private List<String> shops;

    private Long menuId;

    private Integer type;
}
