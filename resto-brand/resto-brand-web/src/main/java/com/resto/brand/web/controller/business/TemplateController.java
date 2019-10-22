package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.Template;
import com.resto.brand.web.service.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xielc on 2017/9/29.
 */
@Controller
@RequestMapping("template")
public class TemplateController extends GenericController {
    @Resource
    TemplateService templateService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<Template> listData(){
        List<Template> list = templateService.selectList();
        return list;
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(Template template){
        templateService.insertSelective(template);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(Template template){
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id){
        templateService.deleteByPrimaryKey(id);
        return Result.getSuccess();
    }
}
