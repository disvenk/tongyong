package com.resto.shop.web.util.keygenerate;

/**
 * 序列号前缀
 */
public enum KeyPrefix {
    TR("TR"),//交易日志
    MRQ("MRQ"),//领料单
    PD("PD"),//盘点单
    BOM("BOM"),//菜品BOM清单
    SUP("SUP"),//供应商
    SPL("SPL"),//供应商报价单
    MPR("MPR"),//入库计划单
    SCH("SCH"),//盘点单
    BILL("BIL"),//账单
    PUO("PUO"),//采购单
    STK("STK"),//库存
    MTA("MTA"),//原料编码
    MTC("MTC");//原料条形码
    private String prefix;
    KeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
