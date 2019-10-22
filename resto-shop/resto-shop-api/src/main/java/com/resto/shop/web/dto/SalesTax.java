package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/9/14/0014 13:31
 * @Description:
 */
@Data
public class SalesTax implements Serializable {
    //商品税率
    private BigDecimal taxrate;
    //商品税额
    private BigDecimal taxamount;
}
