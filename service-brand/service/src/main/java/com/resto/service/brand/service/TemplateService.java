package com.resto.service.brand.service;


import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.Template;
import com.resto.service.brand.entity.TemplateFlow;
import com.resto.service.brand.mapper.TemplateFlowMapper;
import com.resto.service.brand.mapper.TemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by xielc on 2017/9/29.
 */
@Service
public class TemplateService extends BaseService<Template, Long> {


    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private TemplateFlowMapper templateFlowMapper;

    public BaseDao<Template, Long> getDao() {
        return templateMapper;
    }

    /**
     * 根据appid,模板编号查询模板id
     */
    public List<TemplateFlow> selectTemplateId(String appid, String templateNumber){
        return templateFlowMapper.selectTemplateId(appid,templateNumber);
    }
}
