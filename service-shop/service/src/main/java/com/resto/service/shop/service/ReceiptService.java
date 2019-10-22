package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.Receipt;
import com.resto.service.shop.entity.ReceiptOrder;
import com.resto.service.shop.mapper.ReceiptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptService extends BaseService<Receipt, String> {

    @Autowired
    ReceiptMapper receiptMapper;

    @Override
    public BaseDao<Receipt, String> getDao() {
        return receiptMapper;
    }

    public Receipt insertSelective(Receipt record) {
        int count = receiptMapper.insertSelective(record);
        if (count == 0) {
            return null;
        } else {
            return record;
        }
    }

    public List<ReceiptOrder> selectReceiptOrderList(String customerId, String shopId, String state) {
        if (state == null || state.equals("")) {
            List<ReceiptOrder> rlist = receiptMapper.selectApplyReceiptOrderList(customerId, shopId);
            if (rlist != null && !rlist.isEmpty()) {
                for (ReceiptOrder receiptOrder : rlist) {
                    ReceiptOrder r = receiptMapper.selectReceiptMoney(receiptOrder.getOrderNumber());
                    receiptOrder.setReceiptMoney(receiptOrder.getReceiptMoney().intValue() <= r.getReceiptMoney().intValue() ? receiptOrder.getReceiptMoney() : r.getReceiptMoney());
                }
            }
            return rlist;
        } else {
            return receiptMapper.selectReceiptOrderList(customerId, shopId);
        }
    }
}
