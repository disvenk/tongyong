package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/9/14/0014 13:43
 * @Description:
 */
@Data
public class SalesDataDelivery implements Serializable {
    //收货人姓名
    private String receiver_name;
    //收货人所在国家
    private String receiver_country;
    //收货人所在省份
    private String receiver_province ;
    //收货人所在市
    private String receiver_city;
    //收货人所在区
    private String receiver_district;
    //收货人地址1
    private String receiver_address1;
    //收货人地址2
    private String receiver_address2;
    //收货人地址3
    private String receiver_address3;
    //收货人地址4
    private String receiver_address4;
    //收货人邮编
    private String receiver_postal;
    //收货人手机号码
    private String receiver_mobile;
    //收货人电话号码
    private String receiver_phone;
    //物流公司
    private String logisticscompany;
    //物流单号
    private String logisticsdocno;
    //期望送货日期
    private String expectdeliverydate_yyyymmdd;
    //送货备注
    private String deliveryremarks;
}
