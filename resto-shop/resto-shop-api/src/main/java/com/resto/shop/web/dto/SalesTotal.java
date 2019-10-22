package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/9/14/0014 12:57
 * @Description:
 */
@Data
public class SalesTotal implements Serializable {
    //本地店铺号
    private String localstorecode;
    //销售预留库存单号
    private String reservedocno;
    //交易日期
    private String txdate_yyyymmdd;
    //交易时间
    private String txtime_hhmmss;
    //商场编号
    private String mallid;
    //店铺号
    private String storecode;
    //收银机号
    private String tillid;
    //单据类型
    private String salestype;
    //销售单号
    private String txdocno;
    //原交易日期
    private String orgtxdate_yyyymmdd;
    //原交易店铺号
    private String orgstorecode;
    //原收银机号
    private String orgtillid;
    //原销售单号
    private String orgtxdocno;
    //RMS货号
    private String mallitemcode;
    //收银员编号
    private String cashier;
    //VIP卡号
    private String vipcode;
    //销售员
    private String salesman;
    //顾客统计代码
    private String demographiccode;
    //顾客统计值
    private String demographicdata;
    //净数量
    private BigDecimal netqty;
    //原始金额
    private BigDecimal originalamount;
    //销售金额
    private BigDecimal sellingamount;
    //优惠券号码
    private String couponnumber;
    //优惠券组
    private String coupongroup;
    //优惠券类型
    private String coupontype;
    //优惠券数量
    private Integer couponqty;
    //整单折扣信息
    private SalesDiscount totaldiscount;
    //总税额1
    private BigDecimal ttltaxamount1;
    //总税额2
    private BigDecimal ttltaxamount2;
    //净金额
    private BigDecimal netamount;
    //付款金额
    private BigDecimal paidamount;
    //找零金额
    private BigDecimal changeamount;
    //售价是否含税
    private String priceincludetax;
    //店铺税组
    private String shoptaxgroup;
    //扩展参数
    private String extendparam;
    //发票抬头
    private String invoicetitle;
    //发票内容
    private String invoicecontent;
    //创建人
    private String issueby;
    //创建日期
    private String issuedate_yyyymmdd;
    //创建时间
    private String issuetime_hhmmss;
    //网购订单号
    private String ecorderno;
    //卖家备注
    private String buyerremark;
    //交易备注
    private String orderremark;
    //状态
    private String status;
}
