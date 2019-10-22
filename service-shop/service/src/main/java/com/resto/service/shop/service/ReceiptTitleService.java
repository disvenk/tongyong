package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ReceiptTitle;
import com.resto.service.shop.mapper.ReceiptTitleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptTitleService extends BaseService<ReceiptTitle,String> {

    @Autowired
    private ReceiptTitleMapper receiptTitleMapper;

    @Override
    public BaseDao<ReceiptTitle, String> getDao() {
        return receiptTitleMapper;
    }

    public List<ReceiptTitle> selectOneList(String customerId){
        return  receiptTitleMapper.selectOneList(customerId);
    }

    public List<ReceiptTitle> selectTypeList(String customerId,String type){
        return receiptTitleMapper.selectTypeList(customerId,Integer.parseInt(type));
    }

    public List<ReceiptTitle> selectDefaultList(String customerId){
        return receiptTitleMapper.selectDefaultList(customerId);
    }

}
