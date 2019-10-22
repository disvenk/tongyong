package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/9/14/0014 14:46
 * @Description:
 */
@Data
public class SalesTotalPojo implements Serializable {
    //销售单主表
    private SalesTotal salesTotal;
    //销售单货品明细表
    private List<SalesItem> salesItem;
    //销售单付款明细表
    private List<SalesTender> salesTender;
    //销售单配送表
    private SalesDataDelivery salesDataDelivery;
}
