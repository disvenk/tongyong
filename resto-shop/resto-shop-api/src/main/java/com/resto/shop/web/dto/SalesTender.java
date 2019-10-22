package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/9/14/0014 13:40
 * @Description:
 */
@Data
public class SalesTender implements Serializable {
    //行号
    private Long lineno;
    //付款代码
    private String tendercode;
    //付款类型
    private Integer tendertype;
    //付款种类
    private Integer tendercategory;
    //付款金额
    private BigDecimal payamount;
    //本位币金额
    private BigDecimal baseamount;
    //超额金额
    private BigDecimal excessamount;
    //扩展参数
    private String extendparam;
    //备注
    private String remark;
}
