package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ReceiptTitle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReceiptTitleMapper extends GenericDao<ReceiptTitle,String> {
    int deleteByPrimaryKey(Long id);

    int insert(ReceiptTitle record);

    int insertSelective(ReceiptTitle record);

    ReceiptTitle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReceiptTitle record);

    int updateByPrimaryKey(ReceiptTitle record);

    int updateByState(ReceiptTitle record);

    List<ReceiptTitle> selectOneList(@Param("customerId")String customerId);

    List<ReceiptTitle> selectTypeList(@Param("customerId")String customerId, @Param("type")Integer type);

    List<ReceiptTitle> selectDefaultList(@Param("customerId")String customerId);
}