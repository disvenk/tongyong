package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ReceiptTitle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReceiptTitleMapper extends BaseDao<ReceiptTitle,String> {

    int deleteByPrimaryKey(Long id);

    int insert(ReceiptTitle record);

    ReceiptTitle selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ReceiptTitle record);

    List<ReceiptTitle> selectOneList(@Param("customerId") String customerId);

    List<ReceiptTitle> selectTypeList(@Param("customerId")String customerId, @Param("type")Integer type);

    List<ReceiptTitle> selectDefaultList(@Param("customerId")String customerId);
}