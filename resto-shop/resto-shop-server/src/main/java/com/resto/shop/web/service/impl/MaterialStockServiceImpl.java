package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.dto.MaterialStockDo;
import com.resto.shop.web.manager.DocMaterialStockManager;
import com.resto.shop.web.manager.MdMaterialConsumeDetailManager;
import com.resto.shop.web.model.MdMaterialConsumeDetail;
import com.resto.shop.web.service.MaterialStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
@Service
public class MaterialStockServiceImpl implements MaterialStockService {


    @Autowired
    private DocMaterialStockManager docMaterialStockManager;

    @Autowired
    private MdMaterialConsumeDetailManager mdMaterialConsumeDetailManager;



    @Override
    public List<MaterialStockDo> queryJoin4Page(String shopDetailId, String startTime, String endTime) {
        return docMaterialStockManager.queryJoin4Page(shopDetailId,startTime,endTime);
    }

    @Override
    public MaterialStockDo queryById(Long id) {
        return null;
    }

    @Override
    public Integer addMaterial(MaterialStockDo materialStockDo) {
        return null;
    }

    @Override
    public int updateMaterial(MaterialStockDo materialStockDo) {
        return 0;
    }

    @Override
    public Integer deleteById(Long id) {
        return null;
    }

    @Override
    public List<MdMaterialConsumeDetail> findMaterialStockConsumeDetailByMaterialId(String shopId, Long materialId) {
        MdMaterialConsumeDetail materialConsumeDetail =  new MdMaterialConsumeDetail();
        materialConsumeDetail.setShopId(shopId);
        materialConsumeDetail.setMaterialId(materialId);
        return mdMaterialConsumeDetailManager.findMdMaterialConsumeDetailsByMdMaterialConsumeDetail(materialConsumeDetail);
    }
}

