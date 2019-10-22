package com.resto.shop.web.controller.scm;

import com.alibaba.dubbo.config.annotation.Reference;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.dto.CategoryOne;
import com.resto.shop.web.model.MdCategory;
import com.resto.shop.web.service.CategoryService;
import com.resto.shop.web.util.Assertion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("scmCategory")
public class ScmCategoryController extends GenericController {

    @Resource
    CategoryService categoryService;

    @Resource
    BrandSettingService brandSettingService;


    @RequestMapping("/list")
    public String list(){
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if (brandSetting.getIsOpenScm().equals(Common.YES)){
            return "scmCategory/list";
        }else {
            return "notopen";
        }
    }

    /***
     * @return
     */
    @RequestMapping("/query")
    @ResponseBody
    public Result queryAll(String shopId,Long supPriceId,Long pmsHeadId) {


        String shopDetailId = StringUtils.isEmpty(shopId)?getCurrentShopId():shopId;
        if(supPriceId== null){
            supPriceId =0l;
        }
        if(pmsHeadId== null){
            pmsHeadId =0l;
        }

        log.info("supPriceId====="+supPriceId);

        log.info("pmsHeadId====="+pmsHeadId);
        List<CategoryOne> list = categoryService.queryAll(shopDetailId,supPriceId,pmsHeadId);
        return getSuccessResult(list);
    }



    @RequestMapping("/list_categoryHierarchy")
    @ResponseBody
    public Result list_categoryHierarchy(Integer categoryHierarchy) {
        String shopId = this.getCurrentShopId();
        List<MdCategory> list;
        if (categoryHierarchy==3){
           list = categoryService.queryByCategoryHierarchy(categoryHierarchy,shopId);
        }else {
            list = categoryService.queryByCategoryHierarchy(categoryHierarchy,null);
        }

        return getSuccessResult(list);
    }


    @RequestMapping("/list_all")
    @ResponseBody
    public Result listData() {
        List<MdCategory> list = categoryService.queryCategories();
        return getSuccessResult(list);
    }


    @RequestMapping("/list_one")
    @ResponseBody
    public Result list_one(Long id) {
        MdCategory mdCategory = categoryService.queryById(id);
        return getSuccessResult(mdCategory);
    }


    @RequestMapping("look_down")
    @ResponseBody
    public Result look_down(Integer hierarchyId,Long id) {
        List<MdCategory> list;
        try{
            String shopId = this.getCurrentShopId();
            Assertion.isPositive(id,"id不能为空");
            Assertion.isPositive(hierarchyId,"hierarchyId不能为空");
            if (hierarchyId >= 3) {
                return new Result("已经是最小层级", 5000, false);
            } else if (hierarchyId == 2){
                list = categoryService.queryDown(hierarchyId + 1,id,shopId);
                return getSuccessResult(list);
            }else {
                list = categoryService.queryDown(hierarchyId + 1,id,null);
                return getSuccessResult(list);
            }
        }catch (Exception e){
            return new Result(e.getMessage(), 5000, false);
        }
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid @RequestBody MdCategory mdCategory) {
        try{
            Assertion.isTrue(mdCategory != null,"新增参数不能为空");
            Assertion.notEmpty(mdCategory.getCategoryName(),"名称不能为空");
            Assertion.isPositive(mdCategory.getParentId(),"父类id不能为空");
            Assertion.isPositive(mdCategory.getCategoryHierarchy(),"层级id不能为空");
            Assertion.isPositive(mdCategory.getSort(),"排序不能为空");

            mdCategory.setShopDetailId(this.getCurrentShopId());
            mdCategory.setBrandId(this.getCurrentBrandId());
            int i = categoryService.addCategory(mdCategory);
            if (i > 0) {
                return Result.getSuccess();
            }
        }catch (Exception e){
            return new Result(e.getMessage(), 5000, false);
        }
        return new Result("保存失败", 5000, false);
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(MdCategory mdCategory) {
        Integer row = categoryService.updateMdCategory(mdCategory);
        if (row>0){
            log.info("分类id：" + mdCategory.getId()  + "修改成功" + "操作用户id：" + this.getCurrentUserId());
            return Result.getSuccess();
        }
        return new Result("修改失败", 5000, false);
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(Long id) {
        if (id == null){
            return new Result("删除分类的id不能为空", 5000, false);
        }

        Integer row = categoryService.deleteById(id);
        if (row>0){
            log.info("分类id：" + id  + "删除成功" + "操作用户id：" + this.getCurrentUserId());
            return Result.getSuccess();
        }

        return new Result("删除失败", 5000, false);
    }

}
