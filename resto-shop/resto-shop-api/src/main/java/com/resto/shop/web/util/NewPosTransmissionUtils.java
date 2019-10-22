package com.resto.shop.web.util;

/**
 * Created by xielc on 2018/6/4.
 * 后台编辑通过消息队列发送消息通知newpos表名
 */
public class NewPosTransmissionUtils {

    public static final String TBAREA="tb_area"; //区域表--tb_area

    public static final String TBARTICLE="tb_article";//菜品表——tb_article

    public static final String TBARTICLEATTR="tb_article_attr";//菜品老规格属性表——tb_article_attr

    public static final String TBARTICLEFAMILY="tb_article_family";//菜品类别表tb_article_family

    public static final String TBARTICLEKITCHEN="tb_article_kitchen"; //菜品出餐厨房表——tb_article_kitchen

    public static final String TBARTICLEPRICE="tb_article_price";//菜品老规格详情表——tb_article_price

    public static final String TBARTICLESUPPORTTIME="tb_article_support_time";//菜品供应时间表——tb_article_support_time

    public static final String TBARTICLEUNIT="tb_article_unit"; //菜品老规格属性子项（详情）表——tb_article_unit

    public static final String TBARTICLEUNITDETAIL="tb_article_unit_detail";//菜品新规格属性子项关联表——tb_article_unit_detail

    public static final String TBARTICLEUNITNEW="tb_article_unit_new"; //菜品新规格属性关联表——tb_article_unit_new

    public static final String TBKITCHEN="tb_kitchen"; //出餐厨房--tb_kitchen

    public static final String TBMEALATTR="tb_meal_attr"; //套餐属性表——tb_meal_attr

    public static final String TBMEALITEM="tb_meal_item";  //套餐属性子项表——tb_meal_item

    public static final String TBPRINTER="tb_printer"; //打印机表--tb_printer

    public static final String SHOPDETAIL="tb_shop_detail";//店铺详情表——tb_shop_detail

    public static final String TBSUPPORTTIME="tb_support_time";//供应时间表——tb_support_time

    public static final String TBTABLEQRCODE="tb_table_qrcode";//桌位表--tb_table_qrcode

    public static final String TBUNIT="tb_unit"; //新规格属性表——tb_unit

    public static final String TBUNITDETAIL="tb_unit_detail";//新规格属性子项表——tb_unit_detail

    public static final String BRANDUSER="tb_user";//用户表——tb_user

    public static final String TBORDERREMARK="tb_order_remark";//订单备注表——tb_order_remark

    public static final String BRANDSETTING="tb_brand_setting"; //品牌设置表——tb_brand_setting

    public static final String SHOPTVCONFIG="tb_shop_tv_config";//电视配置信息——tb_shop_tv_config

    public static final String TBWEIGHTPACKAGE="tb_weight_package";//重量包——tb_weight_package

    public static final String TBWEIGHTPACKAGEDETAIL="tb_weight_package_detail";//重量包详情——tb_weight_package_detail

    public static final String TBVIRTUALPRODUCTS="tb_virtual_products";//获取虚拟餐包——tb_virtual_products

    public static final String TBVIRTUALPRODUCTSKITCHEN="tb_virtual_products_kitchen"; //获取虚拟餐包厨房信息——tb_virtual_products_kitchen

    public static final String TBKITCHENGROUP="tb_kitchen_group";

    public static final String  TBKITCHENGROUPDETAIL="tb_kitchen_group_detail";

    public static final String  RECOMMEND="tb_recommend";

    public static final String  RECOMMENDARTICLE="tb_recommend_article";

}
