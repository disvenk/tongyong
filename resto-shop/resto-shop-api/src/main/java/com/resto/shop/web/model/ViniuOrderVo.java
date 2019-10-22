package com.resto.shop.web.model;


import java.io.Serializable;

public class ViniuOrderVo implements Serializable{

    private ViniuOrderDetailVo viniuOrderDetailVo;

    private ViniuFinalSaleVo viniuFinalSaleVo;

    public ViniuOrderDetailVo getViniuOrderDetailVo() {
        return viniuOrderDetailVo;
    }

    public void setViniuOrderDetailVo(ViniuOrderDetailVo viniuOrderDetailVo) {
        this.viniuOrderDetailVo = viniuOrderDetailVo;
    }

    public ViniuFinalSaleVo getViniuFinalSaleVo() {
        return viniuFinalSaleVo;
    }

    public void setViniuFinalSaleVo(ViniuFinalSaleVo viniuFinalSaleVo) {
        this.viniuFinalSaleVo = viniuFinalSaleVo;
    }
}
