package com.resto.brand.web.dao;

import com.resto.brand.web.dto.brandContractNums;
import com.resto.brand.web.model.Contract;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ContractMapper  extends GenericDao<Contract,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Contract record);

    int insertSelective(Contract record);

    Contract selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Contract record);

    int updateByPrimaryKeyWithBLOBs(Contract record);

    int updateByPrimaryKey(Contract record);

	int updateMoney(@Param("payMoney") BigDecimal payMoney, @Param("id")Long id);

    List<brandContractNums> selectGroupByBrandName();

    Contract selectByConstractNum(@Param("constractNum") String constractNum);
}
