package com.resto.brand.web.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.BrandMapper;
import com.resto.brand.web.dao.BrandTemplateEditMapper;
import com.resto.brand.web.dao.BrandTemplateRootMapper;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandTemplateEdit;
import com.resto.brand.web.model.BrandTemplateRoot;
import com.resto.brand.web.service.BrandTemplateRootService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
@Component
public class BrandTemplateRootServiceImpl extends GenericServiceImpl<BrandTemplateRoot, Integer> implements BrandTemplateRootService {

    @Resource
    private BrandTemplateRootMapper brandtemplaterootMapper;
    @Resource
    private BrandTemplateEditMapper brandTemplateEditMapper;
    @Resource
    private BrandMapper brandMapper;

    @Override
    public GenericDao<BrandTemplateRoot, Integer> getDao() {
        return brandtemplaterootMapper;
    }

    @Override
    public int addToDoUpateRoot(BrandTemplateRoot brandTemplateRoot) {
        List<Brand> brands = brandMapper.selectBrandDetailInfo();
        List<BrandTemplateEdit> brandTemplateEdits = new ArrayList<>();
        for(Brand brand : brands){
            if(!brand.getDefinedSelf()){
                continue;
            }
            BrandTemplateEdit brandTemplateEdit = new BrandTemplateEdit();
            brandTemplateEdit.setTemplateNumber(brandTemplateRoot.getTemplateNumber());
            brandTemplateEdit.setAppid(brand.getWechatConfig().getAppid());
            brandTemplateEdit.setBrandId(brand.getId());
            brandTemplateEdit.setTemplateSign(brandTemplateRoot.getTemplateSign());
            brandTemplateEdit.setPattern(brandTemplateRoot.getPattern());
            brandTemplateEdit.setPushType(brandTemplateRoot.getPushType());
            brandTemplateEdit.setPushTitle(brandTemplateRoot.getPushTitle());
            brandTemplateEdit.setContent(brandTemplateRoot.getContent());
            brandTemplateEdit.setStartSign(brandTemplateRoot.getStartSign());
            brandTemplateEdit.setEndSign(brandTemplateRoot.getEndSign());
            brandTemplateEdit.setOldStartSign(brandTemplateRoot.getStartSign());
            brandTemplateEdit.setOldEndSign(brandTemplateRoot.getEndSign());
            brandTemplateEdits.add(brandTemplateEdit);
        }
        brandTemplateEditMapper.addToDoUpdateRoot(brandTemplateEdits);
        return 0;
    }

    @Override
    public int deleteToDoUpdataRoot(Integer id) {
        BrandTemplateRoot brandTemplateRoot = brandtemplaterootMapper.selectByPrimaryKey(id);
        List<Brand> brands = brandMapper.selectList();
        Map<String,Object> map = new HashMap<>();
        map.put("list",brands);
        map.put("templateSign",brandTemplateRoot.getTemplateSign());
        brandTemplateEditMapper.deleteToDoUpdataRoot(map);
        return 0;
    }

    @Override
    public int updateToDoUpdataRoot(BrandTemplateRoot brandTemplateRoot) {
        List<Brand> brands = brandMapper.selectList();

        BrandTemplateRoot brandTemplateRoots = brandtemplaterootMapper.selectByPrimaryKey(brandTemplateRoot.getId());
        Map<String,Object> map = new HashMap<>();
        map.put("templateNumber",brandTemplateRoot.getTemplateNumber());
        map.put("oldtemplateSign",brandTemplateRoots.getTemplateSign());
        map.put("newtemplateSign",brandTemplateRoot.getTemplateSign());
        map.put("pattern",brandTemplateRoot.getPattern());
        map.put("pushType",brandTemplateRoot.getPushType());
        map.put("pushTitle",brandTemplateRoot.getPushTitle());
        map.put("content",brandTemplateRoot.getContent());
        map.put("startSign",brandTemplateRoot.getStartSign());
        map.put("endSign",brandTemplateRoot.getEndSign());
        map.put("list",brands);
        brandTemplateEditMapper.updateToDoUpdataRoot(map);
        return 0;
    }
}
