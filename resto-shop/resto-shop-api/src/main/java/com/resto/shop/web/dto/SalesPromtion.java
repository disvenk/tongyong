package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/9/14/0014 13:26
 * @Description:
 */
@Data
public class SalesPromtion implements Serializable {
    //促销编号
    private String promotionid;
    //促销数量
    private BigDecimal promotionuseqty;
    //促销差额
    private BigDecimal promotionless;
    //中促销次数
    private BigDecimal promotionpkgcount;

}
