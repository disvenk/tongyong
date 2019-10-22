package com.resto.shop.web.dao;

import com.resto.shop.web.dto.AritclekitchenPrinterDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AritclekitchenPrinterDtoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AritclekitchenPrinterDto record);

    int insertSelective(AritclekitchenPrinterDto record);

    AritclekitchenPrinterDto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AritclekitchenPrinterDto record);

    int updateByPrimaryKey(AritclekitchenPrinterDto record);

    List<AritclekitchenPrinterDto> selectByShopId(@Param("shopId") String shopId);

    void  deleteByArticleId(@Param("articleId")String articleId,@Param("shopId") String shopId);

    void  deleteByKitchenGroupId(@Param("kitchenGroupId")Integer kitchenGroupId,@Param("shopId") String shopId);
}