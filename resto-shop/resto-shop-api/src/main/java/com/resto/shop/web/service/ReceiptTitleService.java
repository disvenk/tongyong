package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ReceiptTitle;

import java.util.List;

/**
 * Created by xielc on 2017/9/5.
 */
public interface ReceiptTitleService extends GenericService<ReceiptTitle, String> {

    int insertSelective(ReceiptTitle record);

    int updateByPrimaryKeySelective(ReceiptTitle record);

    ReceiptTitle selectByPrimaryKey(String id);

    List<ReceiptTitle> selectOneList(String customerId);

    List<ReceiptTitle> selectTypeList(String customerId,String type);

    List<ReceiptTitle> selectDefaultList(String customerId);

}
