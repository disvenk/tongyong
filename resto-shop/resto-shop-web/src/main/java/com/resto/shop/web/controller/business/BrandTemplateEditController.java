 package com.resto.shop.web.controller.business;

 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.BrandTemplateEdit;
 import com.resto.brand.web.service.BrandTemplateEditService;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.form.IdForm;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.validation.Valid;
 import java.util.List;

 @Controller
 @RequestMapping("brandtemplateedit")
 public class BrandTemplateEditController extends GenericController {

     @Resource
     BrandTemplateEditService brandtemplateeditService;

     @RequestMapping("/list")
     public void list(){
     }

    /* @RequestMapping("/list_all")
     @ResponseBody
     public List<BrandTemplateEdit> listData(){
         return brandtemplateeditService.selectList();
     }*/

     @RequestMapping("/list_all")
     @ResponseBody
     public List<BrandTemplateEdit> listData(){
         List<BrandTemplateEdit> brandTemplateEdits = brandtemplateeditService.selectByBrandId(getCurrentBrandId());
         int sign=0;
         for(BrandTemplateEdit brandTemplateEdit : brandTemplateEdits){
             brandTemplateEdit.setIndex(++sign);
             String[] arr = brandTemplateEdit.getContent().split(",");
             StringBuilder content = new StringBuilder();
             content.append("<span style='color:red'>"+brandTemplateEdit.getStartSign()+"</span></br>");
             for(String str : arr){
                 content.append(str+"</br>");
             }
             content.append("<span style='color:red'>"+brandTemplateEdit.getEndSign()+"</span>");
            brandTemplateEdit.setContent(content.toString());
         }
         return brandTemplateEdits;
     }

     @RequestMapping("list_one")
     @ResponseBody
     public Result list_one(Integer id){
         BrandTemplateEdit brandtemplateedit = brandtemplateeditService.selectById(id);
         return getSuccessResult(brandtemplateedit);
     }

     @RequestMapping("create")
     @ResponseBody
     public Result create(@Valid BrandTemplateEdit brand){
         brandtemplateeditService.insert(brand);
         return Result.getSuccess();
     }

     @RequestMapping("modify")
     @ResponseBody
     public Result modify(@Valid BrandTemplateEdit brand){
         brandtemplateeditService.update(brand);
         return Result.getSuccess();
     }

     @RequestMapping("delete")
     @ResponseBody
     public Result delete(Integer id){
         brandtemplateeditService.delete(id);
         return Result.getSuccess();
     }

     @RequestMapping("reset")
     @ResponseBody
     public Result reset(@RequestBody IdForm form){
         int reset = brandtemplateeditService.reset(form.id,form.bigOpen);
         return Result.getSuccess();
     }

    /* @RequestMapping("startOrOpen")
     @ResponseBody
     public Result startOrOpen(@RequestBody IdForm form){
         int reset = brandtemplateeditService.startOrOpenById(form.id,form.bigOpen);
         return Result.getSuccess();
     }*/
 }
