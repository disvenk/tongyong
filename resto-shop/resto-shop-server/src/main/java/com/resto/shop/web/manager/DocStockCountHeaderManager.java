package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DocStockCountHeaderDao;
import com.resto.shop.web.dto.DocStockCountHeaderDo;
import com.resto.shop.web.model.DocStockCountHeader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component("docStockCountHeaderManager")
public class DocStockCountHeaderManager extends BaseManager<DocStockCountHeader> {
    @Resource
    private DocStockCountHeaderDao docStockCountHeaderdao;


    @Override
    public BaseDao<DocStockCountHeader> getDao() {
        return this.docStockCountHeaderdao;
    }

    public List<DocStockCountHeaderDo> findStockList(String shopId){
        return docStockCountHeaderdao.findStockList(shopId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DocStockCountHeaderDao.xml",
                DocStockCountHeaderDao.class, DocStockCountHeader.class, "doc_stock_count_header",false);
    }

    public String findLastStockCountOfTime(String shopId) {
        return docStockCountHeaderdao.findLastStockCountOfTime(shopId);
    }

    public DocStockCountHeaderDo findStockListById(String shopId, Long id) {
        return docStockCountHeaderdao.findStockListById(shopId,id);
    }
}
