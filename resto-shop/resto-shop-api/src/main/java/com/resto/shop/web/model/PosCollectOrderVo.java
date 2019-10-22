package com.resto.shop.web.model;

import java.io.Serializable;

public class PosCollectOrderVo implements Serializable {

   private LaifushiOrderVo laifushiOrderVo;

   private ViniuOrderVo viniuOrderVo;

   public LaifushiOrderVo getLaifushiOrderVo() {
      return laifushiOrderVo;
   }

   public void setLaifushiOrderVo(LaifushiOrderVo laifushiOrderVo) {
      this.laifushiOrderVo = laifushiOrderVo;
   }

   public ViniuOrderVo getViniuOrderVo() {
      return viniuOrderVo;
   }

   public void setViniuOrderVo(ViniuOrderVo viniuOrderVo) {
      this.viniuOrderVo = viniuOrderVo;
   }
}
