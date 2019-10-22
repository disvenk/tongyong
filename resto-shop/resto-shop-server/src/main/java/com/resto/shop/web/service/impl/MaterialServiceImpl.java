package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.PageResult;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.dto.MaterialDo;
import com.resto.shop.web.manager.MdMaterialManager;
import com.resto.shop.web.model.MdMaterial;
import com.resto.shop.web.service.MaterialService;
import com.resto.shop.web.util.keygenerate.KeyPrefix;
import com.resto.shop.web.util.keygenerate.UniqueCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/***
 * 原料清单管理
 */
@Component
@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MdMaterialManager mdMaterialManager;




    @Override
    public PageResult<MdMaterial> query4Page(MdMaterial mdMaterial) {
        BaseQuery<MdMaterial> baseQuery =BaseQuery.getInstance(mdMaterial);
        return mdMaterialManager.query4Page(baseQuery);
    }

    @Override
    public MdMaterial queryById(Long id) {
        return mdMaterialManager.getById(id);
    }

    @Override
    public Integer addMaterial(MdMaterial mdMaterial) {
        mdMaterial.setBarcode(UniqueCodeGenerator.generate(KeyPrefix.MTC.getPrefix(), "L"));
        mdMaterial.setMaterialCode(UniqueCodeGenerator.generate(KeyPrefix.MTA.getPrefix(), "L"));
        mdMaterial.setGmtCreate(new Date());
        mdMaterial.setCreaterId(mdMaterial.getCreaterId());
        mdMaterial.setCreaterName(mdMaterial.getCreaterName());
        mdMaterial.setMinUnitName(mdMaterial.getConvertUnitName());
        mdMaterial.setMinUnitId(mdMaterial.getConvertUnitId());
        mdMaterial.setDistrictName(mdMaterial.getDistrictName()==null?"":mdMaterial.getDistrictName());
       //parseFloat(this.m.measureUnit)*parseFloat(this.m.rate)/parseFloat(this.m.minMeasureUnit)
        if(mdMaterial.getCoefficient()== null){//前端没有计算,后台计算
            mdMaterial.setCoefficient(mdMaterial.getMeasureUnit().multiply(mdMaterial.getRate()).divide(mdMaterial.getMinMeasureUnit()));
        }

        return mdMaterialManager.insert(mdMaterial);
    }
    @Override
    public int updateMaterial(MdMaterial mdMaterial) {
        //查询符合条件的
        BaseQuery<MdMaterial> baseQuery = BaseQuery.getInstance(new MdMaterial());
        baseQuery.addEquals("id",mdMaterial.getId());
        //修改成现在的
        UpdateByQuery<MdMaterial> updateByQuery = new UpdateByQuery<MdMaterial>(baseQuery, mdMaterial);
        return mdMaterialManager.updateByQuery(updateByQuery);
    }

    @Override
    public Integer deleteById(Long id) {
        return mdMaterialManager.deleteById(id);
    }

    @Override
    public List<MaterialDo> queryJoin4Page(String shopId) {
        return mdMaterialManager.queryJoin4Page(shopId);
    }
}
