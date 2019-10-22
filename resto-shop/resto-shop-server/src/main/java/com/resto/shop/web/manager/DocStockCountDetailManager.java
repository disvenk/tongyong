package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DocStockCountDetailDao;
import com.resto.shop.web.dto.DocStockCountDetailDo;
import com.resto.shop.web.model.DocStockCountDetail;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("docStockCountDetailManager")
public class DocStockCountDetailManager extends BaseManager<DocStockCountDetail> {
    @Resource
    private DocStockCountDetailDao docStockCountDetailDao;


    @Override
    public BaseDao<DocStockCountDetail> getDao() {
        return this.docStockCountDetailDao;
    }

    public List<DocStockCountDetailDo> findStockDetailListById(Long stockId){
        return docStockCountDetailDao.findStockDetailListById(stockId);
    }

    public List<DocStockCountDetail> selectMaterialId(Long materialId){
        return docStockCountDetailDao.selectMaterialId(materialId);
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DocStockCountDetailDao.xml",
               DocStockCountDetailDao.class, DocStockCountDetail.class, "doc_stock_count_detail",false);
    }


}
