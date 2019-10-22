package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.OrderBefore;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KONATA on 2017/11/1.
 */
public interface OrderBeforeMapper  extends GenericDao<OrderBefore,Long> {

    OrderBefore getOrderNoPay(@Param("tableNumber") String tableNumber,@Param("shopId") String shopId,
                              @Param("customerId") String customerId);

    void updateState(@Param("orderId") String orderId,@Param("state") Integer state);
}
