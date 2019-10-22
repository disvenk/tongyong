package com.resto.brand.web.service;

import com.resto.brand.web.model.Template;
import com.resto.brand.web.model.TemplateFlow;

import java.util.List;

/**
 * Created by xielc on 2017/9/29.
 */
public interface TemplateService {

    List<Template> selectList();

    int insertSelective(Template record);

    int deleteByPrimaryKey(String id);

    List<TemplateFlow> selectTemplateId(String appid, String templateNumber);
}
