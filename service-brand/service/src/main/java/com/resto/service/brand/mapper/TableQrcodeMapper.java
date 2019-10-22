package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.TableQrcode;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Created by carl on 2016/12/16.
 */
public interface TableQrcodeMapper  extends BaseDao<TableQrcode, Long> {

    List<TableQrcode> selectByShopId(String shopId);

    List<TableQrcode> selectUsedByShopId(String shopId);

    List<Long> selectArea(String shopId);

    void updateTable(@Param("areaId") Long areaId, @Param("areaName") String areaName);

    void deleteArea(@Param("areaId") Long areaId);

    TableQrcode selectByTableNumberShopId(@Param("shopId") String shopId, @Param("tableNumber") Integer tableNumber);
}
