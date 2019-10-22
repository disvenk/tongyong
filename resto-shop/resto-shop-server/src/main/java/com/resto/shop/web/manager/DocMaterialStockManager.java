package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DocMaterialStockDao;
import com.resto.shop.web.dto.DocStockCountDetailDo;
import com.resto.shop.web.dto.MaterialStockDo;
import com.resto.shop.web.model.DocMaterialStock;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component("docMaterialStockManager")
public class DocMaterialStockManager extends BaseManager<DocMaterialStock> {

    @Resource
    private DocMaterialStockDao docMaterialStockDao;

    @Override
    public BaseDao<DocMaterialStock> getDao() {
        return this.docMaterialStockDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DocMaterialStockDao.xml",
               DocMaterialStockDao.class, DocMaterialStock.class, "doc_material_stock",false);
    }

    public List<MaterialStockDo> queryJoin4Page(String shopDetailId, String startTime, String endTime) {
        return docMaterialStockDao.queryJoin4Page(shopDetailId,startTime,endTime);
    }

    public int updateMaterStock(DocStockCountDetailDo docStockCountDetailDo, String shopId){
        DocMaterialStock docMaterialStock = new DocMaterialStock();
        docMaterialStock.setGmtModified(new Date());
        docMaterialStock.setActStockCount(docStockCountDetailDo.getActStockCount());
        BaseQuery<DocMaterialStock> base = BaseQuery.getInstance(new DocMaterialStock());
        base.getData().setMaterialId(docStockCountDetailDo.getMaterialId());
        base.getData().setShopDetailId(shopId);
        UpdateByQuery<DocMaterialStock> query = new UpdateByQuery<>(base,docMaterialStock);
        return docMaterialStockDao.updateByQuery(query);
    }

    public DocMaterialStock findStockByShopId(String shopId,Long materialId){
        return docMaterialStockDao.findStockByShopId(shopId,materialId);
    }


}
