package com.resto.shop.web.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-09-17 15:41
 */
public class SejsUtil {

    public static Map<String,String> sejs(BigDecimal money){
        System.out.println("可开票金额"+money);

        //价税合计
        String jshj =money.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        System.out.println("价税合计"+jshj);

        //中间换算步骤,发票之间的误差不能超过6分钱
        BigDecimal zjhs = money.divide(BigDecimal.valueOf(1.06),2,BigDecimal.ROUND_HALF_UP);

        //税额
        BigDecimal hjse = zjhs.multiply(BigDecimal.valueOf(0.06)).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println("合计税额"+hjse);

        //合计金额
        BigDecimal hjje = money.subtract(hjse).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println("合计金额"+hjje);

        Map<String,String> map = new HashMap<>();
        map.put("hjse", hjse.toString());
        map.put("hjje", hjje.toString());
        return map;
    }
}
