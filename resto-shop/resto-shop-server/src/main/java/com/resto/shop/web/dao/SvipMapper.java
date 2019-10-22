package com.resto.shop.web.dao;

import com.resto.shop.web.dto.SvipActReportDto;
import com.resto.shop.web.dto.SvipActivityDto;
import com.resto.shop.web.model.Svip;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import javax.annotation.security.PermitAll;
import java.util.List;

public interface SvipMapper  extends GenericDao<Svip,String> {
    int deleteByPrimaryKey(String id);

    int insert(Svip record);

    int insertSelective(Svip record);

    Svip selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Svip record);

    int updateByPrimaryKey(Svip record);

    List<SvipActivityDto> getAtcReport(@Param("beginDate")String beginDate,@Param("endDate")String endDate);

    List<SvipActReportDto> selectListByActId(@Param("id")String id,@Param("beginDate")String beginDate,@Param("endDate")String endDate);

    Long isSvip(@Param("customerId")String customerId);
}
