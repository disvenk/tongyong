package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/9/14/0014 13:02
 * @Description:
 */
@Data
public class SalesDiscount implements Serializable {
    //折扣允许
    private String discountapprove;
    //折扣模式
    private String discountmode;
    //折扣值
    private BigDecimal discountvalue;
    //折扣金额
    private BigDecimal discountless;
}
