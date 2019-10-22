package com.resto.shop.web.model;

import lombok.Data;
import com.resto.shop.web.posDto.AuditOrderItem;
import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/10/20/0020 16:06
 * @Description:
 */
@Data
public class DailyLogOperation implements Serializable {

   private String id;
   //店铺id
   private String shopId;
   //品牌id
   private String brandId;
   //操作人
   private String operator;
   //结店状态
   private Integer closeStatus;
   //时间
   private String createTime;

   private List<AuditOrderItem> auditOrderItems;

}
