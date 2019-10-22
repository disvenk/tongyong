package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DocStkInPlanDetailDao;
import com.resto.shop.web.model.DocStkInPlanDetail;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component("docStkInPlanDetailManager")
public class DocStkInPlanDetailManager extends BaseManager<DocStkInPlanDetail> {
    @Resource
    private DocStkInPlanDetailDao docStkInPlanDetailDao;

    @Override
    public BaseDao<DocStkInPlanDetail> getDao() {
        return this.docStkInPlanDetailDao;
    }

    public List<DocStkInPlanDetail> selectMaterialId(Long materialId){
        return docStkInPlanDetailDao.selectMaterialId(materialId);
    }
    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DocStkInPlanDetailDao.xml",
               DocStkInPlanDetailDao.class, DocStkInPlanDetail.class, "doc_stk_in_plan_detail",false);
    }


}
