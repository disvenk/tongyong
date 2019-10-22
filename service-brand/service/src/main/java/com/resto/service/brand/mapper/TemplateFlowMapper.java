package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.TemplateFlow;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TemplateFlowMapper extends BaseDao<TemplateFlow,Long> {

    int deleteByPrimaryKey(Long id);

    int insert(TemplateFlow record);

    int insertSelective(TemplateFlow record);

    TemplateFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TemplateFlow record);

    int updateByPrimaryKey(TemplateFlow record);

    int deleteAllTemplateNumber(String templateNumber);

    int deleteAllAppId(@Param("templateNumber") String templateNumber, @Param("appid") String appid);

    List<TemplateFlow> selectTemplateId(@Param("appid") String appid, @Param("templateNumber") String templateNumber);
}