package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.TableCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TableCodeMapper extends BaseDao<TableCode,String> {
    int insert(TableCode record);

    int updateByPrimaryKey(TableCode record);

    TableCode selectByName(@Param("name") String name, @Param("shopId") String shopId);

    TableCode selectByCodeNumber(@Param("codeNumber") String codeNumber, @Param("shopId") String shopId);

    List<TableCode> selectListByShopId(String shopId);

    TableCode selectByPersonNumber(@Param("personNumber") Integer personNumber, @Param("shopId") String shopId);

    List<TableCode> getTableList(String shopId);
    
    /**
     * 获取店铺的桌位信息和取号集合
     * @author lmx
     * @version 创建时间：2016年12月13日 下午6:58:31
     * @param shopId
     * @return
     */
    List<TableCode> selectTableAndGetNumbers(String shopId);
}
