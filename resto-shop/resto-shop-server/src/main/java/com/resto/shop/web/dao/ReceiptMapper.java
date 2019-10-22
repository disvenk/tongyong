package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.dto.ReceiptOrder;
import com.resto.shop.web.dto.ReceiptPos;
import com.resto.shop.web.dto.ReceiptPosOrder;
import com.resto.shop.web.model.Receipt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReceiptMapper extends GenericDao<Receipt,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Receipt record);

    int insertSelective(Receipt record);

    Receipt selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Receipt record);

    int updateByPrimaryKey(Receipt record);

    int updateState(Receipt record);

    int updateReceiptOrderNumber(Receipt record);

    List<ReceiptOrder> selectReceiptOrderList(@Param("customerId")String customerId,@Param("shopId")String shopId,@Param("ticketType")Integer ticketType);

    List<ReceiptOrder> selectApplyReceiptOrderList(@Param("customerId")String customerId,@Param("shopId")String shopId);

    ReceiptPosOrder getReceiptOrderList(@Param("receiptId")Integer receiptId);

    ReceiptPosOrder getReceiptOrderNumberList(@Param("orderNumber")String orderNumber);

    List<ReceiptPos> getNowReceiptList(@Param("shopId")String shopId,@Param("state")Integer state);

    List<ReceiptPos> getBeforeReceiptList(@Param("shopId")String shopId);

    ReceiptPos getPosReceiptList(@Param("orderNumber")String orderNumber);

    ReceiptOrder selectReceiptMoney(@Param("orderNumber")String orderNumber);

    ReceiptOrder selectReceiptOrderOneMoney(@Param("orderNumber")String orderNumber);

    int getReceiptOrderNumberCount(@Param("orderNumber")String orderNumber);

    int addTickets(List<Receipt> list);

    int updateStatusById(@Param("serialNo")String serialNo);

}