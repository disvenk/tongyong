package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DocStkInPlanHeaderDao;
import com.resto.shop.web.dto.DocStkInPlanHeaderDetailDo;
import com.resto.shop.web.model.DocStkInPlanHeader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component("docStkInPlanHeaderManager")
public class DocStkInPlanHeaderManager extends BaseManager<DocStkInPlanHeader> {
    @Resource
    private DocStkInPlanHeaderDao docStkInPlanHeaderDao;


    @Override
    public BaseDao<DocStkInPlanHeader> getDao() {
        return this.docStkInPlanHeaderDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DocStkInPlanHeaderDao.xml",
               DocStkInPlanHeaderDao.class, DocStkInPlanHeader.class, "doc_stk_in_plan_header",false);
    }

    public List<DocStkInPlanHeaderDetailDo> queryJoin4Page(String shopId) {
          return docStkInPlanHeaderDao.queryJoin4Page(shopId);
    }

    public void updateDocStkStatusById(Long id, String orderStatus,String auditName) {
        BaseQuery<DocStkInPlanHeader> query = BaseQuery.getInstance(new DocStkInPlanHeader());
        query.getData().setId(id);
        DocStkInPlanHeader updateDo = new DocStkInPlanHeader();
        updateDo.setOrderStatus(orderStatus);
        updateDo.setAuditName(auditName);
        updateDo.setAuditTime(new Date());
        UpdateByQuery<DocStkInPlanHeader> updateByQuery = new UpdateByQuery<>(query,updateDo);
        docStkInPlanHeaderDao.updateByQuery(updateByQuery);
    }

    public DocStkInPlanHeader queryJoinSupplier(Long id) {
        return docStkInPlanHeaderDao.queryJoinSupplier(id);
    }
}
