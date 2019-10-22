package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.Receipt;
import com.resto.service.shop.entity.ReceiptOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReceiptMapper extends BaseDao<Receipt,String> {
    int deleteByPrimaryKey(Long id);

    int insert(Receipt record);

    Receipt selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Receipt record);

    int updateState(Receipt record);

    List<ReceiptOrder> selectApplyReceiptOrderList(@Param("customerId")String customerId, @Param("shopId")String shopId);

    ReceiptOrder selectReceiptMoney(@Param("orderNumber")String orderNumber);

    List<ReceiptOrder> selectReceiptOrderList(@Param("customerId")String customerId,@Param("shopId")String shopId);

}