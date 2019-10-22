package com.resto.service.brand.mapper;


import com.resto.api.brand.dto.brandContractNums;
import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Contract;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ContractMapper  extends BaseDao<Contract,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Contract record);

    int insertSelective(Contract record);

    Contract selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Contract record);

    int updateByPrimaryKeyWithBLOBs(Contract record);

    int updateByPrimaryKey(Contract record);

	int updateMoney(@Param("payMoney") BigDecimal payMoney, @Param("id") Long id);

    List<brandContractNums> selectGroupByBrandName();

    Contract selectByConstractNum(@Param("constractNum") String constractNum);
}
