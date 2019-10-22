package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.web.dao.TemplateFlowMapper;
import com.resto.brand.web.dao.TemplateMapper;
import com.resto.brand.web.dao.WechatConfigMapper;
import com.resto.brand.web.model.Template;
import com.resto.brand.web.model.TemplateFlow;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.TemplateService;
import com.resto.brand.web.service.WeChatService;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xielc on 2017/9/29.
 */
@Component
@Service
public class TemplateServiceImpl implements TemplateService{

    @Resource
    private TemplateMapper templateMapper;

    @Resource
    private TemplateFlowMapper templateFlowMapper;

    @Resource
    private WechatConfigMapper wechatConfigMapper;

    @Resource
    private WeChatService weChatService;
    /**
     * 查询所有微信模板消息
     * @return
     */
    @Override
    public List<Template> selectList(){
        return templateMapper.selectList();
    }

    /**
     * 插入微信模板，并将模板编号,给每个品牌,生成发送模板消息的id
     * @param record
     * @return
     */
    @Override
    public int insertSelective(Template record){
        int count = templateMapper.insertSelective(record);
        List<WechatConfig> list=wechatConfigMapper.selectList();
        for(WechatConfig wechatConfig:list){
           String appid = wechatConfig.getAppid();
           String appsecret=wechatConfig.getAppsecret();
           String res = weChatService.getTemplate(record.getTemplateNumber(), appid, appsecret);
           //System.out.println("返回模板id的json："+res);
           if(!"".equals(res)){
                JSONObject access = new JSONObject(res);
                String templateId = access.optString("template_id");
                if(!"".equals(templateId)){
                    TemplateFlow templateFlow = new TemplateFlow();
                    templateFlow.setAppid(appid);
                    templateFlow.setTemplateNumber(record.getTemplateNumber());
                    templateFlow.setTemplateId(templateId);
                    templateFlowMapper.insertSelective(templateFlow);
                }
           }
        }
        return count;
    }

    /**
     * 删除微信模板,根据模板id删除模板，并且删除该模板,对应所有品牌下的模板消息的id
     * @param id
     * @return
     */
    @Override
    public int deleteByPrimaryKey(String id){
        Template template=templateMapper.selectByPrimaryKey(Long.parseLong(id));
        templateFlowMapper.deleteAllTemplateNumber(template.getTemplateNumber());
        int count = templateMapper.deleteByPrimaryKey(Long.parseLong(id));
        return count;
    }
    /**
     * 根据appid,模板编号查询模板id
     */
    @Override
    public List<TemplateFlow> selectTemplateId(String appid,String templateNumber){
        return templateFlowMapper.selectTemplateId(appid,templateNumber);
    }
}
