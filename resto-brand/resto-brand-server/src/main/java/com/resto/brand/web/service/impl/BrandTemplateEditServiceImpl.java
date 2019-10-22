package com.resto.brand.web.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.BrandMapper;
import com.resto.brand.web.dao.BrandTemplateEditMapper;
import com.resto.brand.web.dao.BrandTemplateRootMapper;
import com.resto.brand.web.model.BrandTemplateEdit;
import com.resto.brand.web.model.BrandTemplateRoot;
import com.resto.brand.web.service.BrandTemplateEditService;
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
public class BrandTemplateEditServiceImpl extends GenericServiceImpl<BrandTemplateEdit, Integer> implements BrandTemplateEditService {

    @Resource
    private BrandTemplateEditMapper brandtemplateeditMapper;
    @Resource
    private BrandTemplateRootMapper brandTemplateRootMapper;
    @Resource
    private BrandMapper brandMapper;

    @Override
    public GenericDao<BrandTemplateEdit, Integer> getDao() {
        return brandtemplateeditMapper;
    }

    @Override
    public List<BrandTemplateEdit> selectByBrandId(String brandId) {
        return brandtemplateeditMapper.selectListByBrandId(brandId);
    }

    @Override
    public BrandTemplateEdit selectOneByManyTerm(String appId,String templateNum,Integer templateSign) {

        return brandtemplateeditMapper.selectOneByManyTerm(appId,templateNum,templateSign);
    }

    @Override
    public int reset(Integer id,Boolean bigOpen) {
        return brandtemplateeditMapper.resetById(id,bigOpen);
    }

    @Override
    public int startOrOpenById(Integer id,Integer bigOpen) {
        return brandtemplateeditMapper.startOrOpenById(id,bigOpen);
    }

    @Override
    public int distributionOrDelete(String BrandId, String appId,Integer flag) {
        //传0表示要置空
        if(flag==0){
            int distribution = brandtemplateeditMapper.deleteByBrandId(BrandId);
            Map<String,Object> map = new HashMap<>();
            map.put("id",BrandId);
            map.put("flag",0);
            brandMapper.definedById(map);
        }else {
            //传1表示要增加
            List<BrandTemplateRoot> brandTemplateRoots = brandTemplateRootMapper.selectList();
            List<BrandTemplateEdit> brandTemplateEdits = new ArrayList<>();
            for(BrandTemplateRoot brandTemplateRoot : brandTemplateRoots){
                BrandTemplateEdit brandTemplateEdit = new BrandTemplateEdit();
                brandTemplateEdit.setTemplateNumber(brandTemplateRoot.getTemplateNumber());
                brandTemplateEdit.setAppid(appId);
                brandTemplateEdit.setBrandId(BrandId);
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
            int distribution = brandtemplateeditMapper.distribution(brandTemplateEdits);
            Map<String,Object> map = new HashMap<>();
            map.put("id",BrandId);
            map.put("flag",1);
            brandMapper.definedById(map);
        }


        return 0;
    }
}
