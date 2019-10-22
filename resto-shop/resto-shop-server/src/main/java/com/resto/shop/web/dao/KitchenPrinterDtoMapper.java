package com.resto.shop.web.dao;

import com.resto.shop.web.dto.kitchenPrinterDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KitchenPrinterDtoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(kitchenPrinterDto record);

    int insertSelective(kitchenPrinterDto record);

    kitchenPrinterDto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(kitchenPrinterDto record);

    int updateByPrimaryKey(kitchenPrinterDto record);

    List<kitchenPrinterDto> selectByKitchenAndShopId(@Param("kitchenId") Integer kitchenId,@Param("shopId") String shopId);

    List<kitchenPrinterDto> selectByKitchenAndShopIdAndStatus(@Param("kitchenId") Integer kitchenId,@Param("shopId") String shopId);

    List<kitchenPrinterDto> selectByKitchenAndShopIdAndArticleId(@Param("kitchenId") Integer kitchenId,@Param("shopId") String shopId,@Param("articleId")String articleId);

    List<kitchenPrinterDto> selectByShopId(@Param("shopId") String shopId);

    int deleteByShopIdAndKitchenId (@Param("kitchenId") Integer kitchenId,@Param("shopId") String shopId);

    void deleteByShopIdAndPrinterId (@Param("printerId") Integer printerId,@Param("shopId") String shopId);

}