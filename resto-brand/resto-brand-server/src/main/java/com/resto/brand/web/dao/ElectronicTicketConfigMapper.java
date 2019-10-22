package com.resto.brand.web.dao;

import com.resto.brand.web.model.ElectronicTicketConfig;
import com.resto.brand.core.generic.GenericDao;
import com.sun.tracing.dtrace.ProviderAttributes;
import org.apache.ibatis.annotations.Param;

public interface ElectronicTicketConfigMapper  extends GenericDao<ElectronicTicketConfig,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(ElectronicTicketConfig record);

    int insertSelective(ElectronicTicketConfig record);

    ElectronicTicketConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ElectronicTicketConfig record);

    int updateByPrimaryKey(ElectronicTicketConfig record);

    ElectronicTicketConfig selectByBrandId(@Param("brandId")String brandId);
}
