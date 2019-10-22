package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.web.service.RedisService;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.PageResult;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.dto.CategoryOne;
import com.resto.shop.web.dto.CategoryThree;
import com.resto.shop.web.dto.CategoryTwo;
import com.resto.shop.web.dto.MaterialDo;
import com.resto.shop.web.manager.MdCategoryManager;
import com.resto.shop.web.manager.MdMaterialManager;
import com.resto.shop.web.model.MdCategory;
import com.resto.shop.web.service.CategoryService;
import com.resto.shop.web.util.log.LogBetter;
import com.resto.shop.web.util.log.LogLevel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import static com.resto.shop.web.util.log.TraceLoggerFactory.logger;
import static java.util.stream.Collectors.toList;

/**
 *
 * 类别管理
 */
@Component
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private MdCategoryManager mdCategoryManager;

    @Autowired
    private MdMaterialManager mdMaterialManager;

    @Autowired
    private RedisService redisService;

    @Override
    public Integer addCategory(MdCategory mdCategory) {
        mdCategory.setGmtCreate(new Date());
        //todo 实现分类编码按分类递增
        MdCategory parentCategory = queryById(mdCategory.getParentId());
        if (parentCategory == null){
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("通过该parentId找不到上一级分类")
                    .addParm("parentId:", mdCategory.getParentId())
                    .log();
        }else if (StringUtils.isBlank(parentCategory.getCategoryCode())){
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("通过该parentId找不到上一级分类parent categoryCode为空")
                    .log();

        }else {
            mdCategory.setCategoryCode(getCategoryCode(mdCategory.getCategoryHierarchy()
                    ,parentCategory.getCategoryCode(),mdCategory.getBrandId()));
        }
        if (mdCategory.getCategoryHierarchy() == 1 || mdCategory.getCategoryHierarchy() == 2){
            mdCategory.setShopDetailId(null);
        }
        return mdCategoryManager.insert(mdCategory);
    }

    @Override
    public PageResult<MdCategory> query4Page(MdCategory mdCategory) {
        return null;
    }

    @Override
    public MdCategory queryById(Long id) {
        return mdCategoryManager.getById(id);
    }

    @Override
    public List<MdCategory> queryByParentId(Long parentId) {
        MdCategory mdCategory = new MdCategory();
        mdCategory.setParentId(parentId);
        BaseQuery<MdCategory> o =BaseQuery.getInstance(mdCategory);
        return mdCategoryManager.query(o);
    }

    @Override
    public Integer updateMdCategory(MdCategory mdCategory){
        mdCategory.setGmtModified(new Date());
        BaseQuery<MdCategory> mm= BaseQuery.getInstance(new MdCategory());
        mm.getData().setId(mdCategory.getId());
        UpdateByQuery<MdCategory> o = new UpdateByQuery<>(mm,mdCategory);
        return mdCategoryManager.updateByQuery(o);
    }

    @Override
    public Integer deleteById(Long id) {
        MdCategory mdCategory = new MdCategory();
        mdCategory.setIsDelete(1);
        mdCategory.setGmtModified(new Date());

        BaseQuery<MdCategory> mm= BaseQuery.getInstance(new MdCategory());
        mm.getData().setId(id);

        UpdateByQuery<MdCategory> o = new UpdateByQuery<>(mm,mdCategory);
        return mdCategoryManager.updateByQuery(o);

    }

    @Override
    public List<MdCategory> queryByCategoryHierarchy(Integer categoryHierarchy,String shopId) {
        MdCategory mdCategory = new MdCategory();
        mdCategory.setCategoryHierarchy(categoryHierarchy);
        mdCategory.setShopDetailId(shopId);
        BaseQuery<MdCategory> o =BaseQuery.getInstance(mdCategory);
        return mdCategoryManager.query(o);
    }

    @Override
    public List<MdCategory> queryDown(Integer categoryHierarchy, Long id,String shopId) {
        MdCategory mdCategory = new MdCategory();
        mdCategory.setCategoryHierarchy(categoryHierarchy);
        mdCategory.setParentId(id);
        mdCategory.setShopDetailId(shopId);
        BaseQuery<MdCategory> o =BaseQuery.getInstance(mdCategory);
        return mdCategoryManager.query(o);
    }

    @Override
    public List<CategoryOne> queryAll(String shopId, Long supPriceId, Long pmsHeadId) {
        List<CategoryOne> list = mdCategoryManager.findCategoryList();
        for (CategoryOne one : list){
            for (CategoryTwo two : one.getTwoList()){
                List<CategoryThree> categoryThreeList = null;
                if(supPriceId.equals(0l) && pmsHeadId.equals(0l)){
                    categoryThreeList = mdCategoryManager.findCategoryThreeList(two.getId(), shopId);
                    categoryThreeList.forEach(s -> {
                        List<MaterialDo> mlist = mdMaterialManager.findMaterialByThirdId(s.getId(),shopId);
                        s.setMaterialList(mlist);
                    });
                }else if(supPriceId.equals(0l) && !pmsHeadId.equals(0l)){
                    categoryThreeList = mdCategoryManager.findCategoryThreeListWithPmsHeadId(two.getId(), shopId);
                    categoryThreeList.stream().forEach(it->{
                        it.setMaterialList(it.getMaterialList().stream().filter(each->pmsHeadId.equals(each.getPmsHeaderId())).collect(toList()));
                    });
                }else if(!supPriceId.equals(0l) && pmsHeadId.equals(0l)){
                    categoryThreeList = mdCategoryManager.findCategoryThreeListWithSupPriceId(two.getId(), shopId);
                    categoryThreeList.stream().forEach(it->{
                       it.setMaterialList(it.getMaterialList().stream().filter(each->supPriceId.equals(each.getSupPriceId())).collect(toList()));
                    });
                }else{
                    logger.warn("supPriceId: "+supPriceId+"和pmsHeadId:"+pmsHeadId+"都不为空");
                }
                two.setThreeList(categoryThreeList);
            }
        }
        return list;
    }

    @Override
    public List<MdCategory> queryCategories() {
        return mdCategoryManager.findCategories();
    }

    public String getCategoryCode(Integer categoryHierarchy, String categoryCode, String brandId) {
        String code = "";
        switch (categoryHierarchy) {
            case 1:
                code = (String) redisService.get("SCM_CATEGORY_ONE" + brandId);
                if (StringUtils.isBlank(code)) {
                    code = "1";
                } else {
                    code = String.valueOf(Long.parseLong(code) + 1);
                }
                redisService.set("SCM_CATEGORY_ONE" + brandId, code);
                DecimalFormat f1 = new DecimalFormat("0000");
                code = f1.format(Integer.parseInt(code)) + "00000" + "00000";
                break;
            case 2:
                code = (String) redisService.get("SCM_CATEGORY_TWO" + brandId);
                if (StringUtils.isBlank(code)) {
                    code = "1";
                } else {
                    code = String.valueOf(Long.parseLong(code) + 1);
                }
                redisService.set("SCM_CATEGORY_TWO" + brandId, code);
                DecimalFormat f2 = new DecimalFormat("00000");
                code = categoryCode.substring(1, 5) + f2.format(Integer.parseInt(code)) + "00000";
                break;
            case 3:
                code = (String) redisService.get("SCM_CATEGORY_THREE" + brandId);
                if (StringUtils.isBlank(code)) {
                    code = "1";
                } else {
                    code = String.valueOf(Long.parseLong(code) + 1);
                }
                redisService.set("SCM_CATEGORY_THREE" + brandId, code);
                DecimalFormat f3 = new DecimalFormat("00000");
                code = categoryCode.substring(1, 10) + f3.format(Integer.parseInt(code));
                break;
        }
        return categoryHierarchy + code;
    }
}
