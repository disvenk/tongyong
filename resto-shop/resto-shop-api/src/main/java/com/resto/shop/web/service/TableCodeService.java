package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.TableCode;

import java.util.List;

public interface TableCodeService extends GenericService<TableCode, String> {

    void insertTableCode(TableCode tablecode,String brandId,String shopDetailId);

    void updateTableCode(TableCode tablecode);

    TableCode selectByName(String name,String shopId);

    TableCode selectByCodeNumber(String codeNumber,String shopId);

    TableCode selectByPersonNumber(Integer personNumber,String shopId);

    List<TableCode> getTableList(String shopId);

    List<TableCode> selectListByShopId(String shopId);

    /**
     * 获取店铺的桌位信息和取号集合
     * @author lmx
     * @version 创建时间：2016年12月13日 下午7:00:13
     * @param shopId
     * @return
     */
    List<TableCode> selectTableAndGetNumbers(String shopId);
}
