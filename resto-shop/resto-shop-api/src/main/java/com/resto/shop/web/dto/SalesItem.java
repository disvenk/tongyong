package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/9/14/0014 13:07
 * @Description:
 */
@Data
public class SalesItem implements Serializable {
    //是否专柜货号
    private String iscounteritemcode;
    //行号
    private Long lineno;
    //店铺号
    private String storecode;
    //货号
    private String mallitemcode;
    //专柜货号
    private String counteritemcode;
    //商品编号
    private String itemcode;
    //商品内部编号
    private String plucode;
    //商品颜色
    private String colorcode;
    //商品尺码
    private String sizecode;
    //商品批次
    private String itemlotnum;
    //序列号
    private Integer serialnum;
    //是否定金单
    private String isdeposit;
    //是否批发
    private String iswholesale;
    //库存类型
    private Integer invttype;
    //数量
    private BigDecimal qty;
    //库存销售比例
    private BigDecimal exstk2sales;
    //原始售价
    private BigDecimal originalprice;
    //售价
    private BigDecimal sellingprice;
    //价格模式
    private String pricemode;
    //允许改价
    private String priceapprove;
    //优惠劵号码
    private String couponnumber;
    //优惠劵组
    private String coupongroup;
    //优惠劵类型
    private String coupontype;
    //单品折扣信息
    private SalesDiscount itemdiscount;
    //VIP折扣率
    private BigDecimal vipdiscountpercent;
    //VIP折扣差额
    private BigDecimal vipdiscountless;
    //商品促销信息
    private SalesPromtion promotion;
    //整单折扣差额1
    private BigDecimal totaldiscountless1;
    //整单折扣差额2
    private BigDecimal totaldiscountless2;
    //整单折扣差额
    private BigDecimal totaldiscountless;
    //商品税信息
    private SalesTax tax;
    //净金额
    private BigDecimal netamount;
    //获得积分
    private BigDecimal bonusearn;
    //交易明细备注
    private String salesitemremark;
    //退货原因
    private String refundreasoncode;
    //扩展参数
    private String extendparam;
}
