package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DocPmsPoDetailDao;
import com.resto.shop.web.exception.ScmPersistenceException;
import com.resto.shop.web.model.DocPmsPoDetail;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("docPmsPoDetailManager")
public class DocPmsPoDetailManager extends BaseManager<DocPmsPoDetail> {

    @Resource
    private DocPmsPoDetailDao docPmsPoDetailDao;

    @Override
    public BaseDao<DocPmsPoDetail> getDao() {
        return this.docPmsPoDetailDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DocPmsPoDetailDao.xml",
               DocPmsPoDetailDao.class, DocPmsPoDetail.class, "doc_pms_po_detail",false);
    }


    /**
     * update 语句where条件后面的参数
     * @param whereDocPmsPoDetail
     * update 语句set后面的参数
     * @param updateDocPmsPoDetail
     */
     public Integer updateDocPmsPoDetailOfCommon(DocPmsPoDetail whereDocPmsPoDetail,DocPmsPoDetail updateDocPmsPoDetail) {
       if(whereDocPmsPoDetail == null || updateDocPmsPoDetail == null){
             throw new ScmPersistenceException("whereDocPmsPoDetail或者updateDocPmsPoDetail不能为空");
        }
        Long id = whereDocPmsPoDetail.getPmsHeaderId();

        if(id == null){
            throw new ScmPersistenceException("修改DocPmsPoDetail实体pmsHeaderId不能为空");
        }
        BaseQuery<DocPmsPoDetail> query = BaseQuery.getInstance(whereDocPmsPoDetail);



       updateDocPmsPoDetail.setId(null);

        UpdateByQuery<DocPmsPoDetail> updateByQuery = new UpdateByQuery<>(query,updateDocPmsPoDetail);
        return docPmsPoDetailDao.updateByQuery(updateByQuery);
     }



    /**
     * select 语句where条件后面的参数
     * @param docPmsPoDetail
     */
       public List<DocPmsPoDetail> findDocPmsPoDetailsByDocPmsPoDetail(DocPmsPoDetail docPmsPoDetail) {
            BaseQuery<DocPmsPoDetail> baseQuery = BaseQuery.getInstance(docPmsPoDetail);
            return docPmsPoDetailDao.query(baseQuery);
        }



}

