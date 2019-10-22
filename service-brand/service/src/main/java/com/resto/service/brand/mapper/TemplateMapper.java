package com.resto.service.brand.mapper;



import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Template;

public interface TemplateMapper extends BaseDao<Template,Long> {

    int insert(Template record);

    int updateByPrimaryKey(Template record);

}