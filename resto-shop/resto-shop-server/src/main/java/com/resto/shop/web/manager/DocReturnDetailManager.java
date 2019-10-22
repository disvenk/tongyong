package com.resto.shop.web.manager;

import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DocReturnDetailDao;
import com.resto.shop.web.exception.ScmPersistenceException;
import com.resto.shop.web.model.DocReturnDetail;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("docReturnDetailManager")
public class DocReturnDetailManager extends BaseManager<DocReturnDetail> {

    @Resource
    private DocReturnDetailDao docReturnDetailDao;

    @Override
    public BaseDao<DocReturnDetail> getDao() {
        return this.docReturnDetailDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DocReturnDetailDao.xml",
               DocReturnDetailDao.class, DocReturnDetail.class, "doc_return_detail",false);
    }


    /**
     * update 语句where条件后面的参数
     * @param whereDocReturnDetail
     * update 语句set后面的参数
     * @param updateDocReturnDetail
     */
     public Integer updateDocReturnDetailOfCommon(DocReturnDetail whereDocReturnDetail,DocReturnDetail updateDocReturnDetail) {
       if(whereDocReturnDetail == null || updateDocReturnDetail == null){
             throw new ScmPersistenceException("whereDocReturnDetail或者updateDocReturnDetail不能为空");
        }
        Long id = whereDocReturnDetail.getId();

        if(id == null){
            throw new ScmPersistenceException("修改实体id不能为空");
        }
        BaseQuery<DocReturnDetail> query = BaseQuery.getInstance(whereDocReturnDetail);

        UpdateByQuery<DocReturnDetail> updateByQuery = new UpdateByQuery<>(query,updateDocReturnDetail);
        return docReturnDetailDao.updateByQuery(updateByQuery);
     }



    /**
     * select 语句where条件后面的参数
     * @param docReturnDetail
     */
       public List<DocReturnDetail> findDocReturnDetailsByDocReturnDetail(DocReturnDetail docReturnDetail) {
            BaseQuery<DocReturnDetail> baseQuery = BaseQuery.getInstance(docReturnDetail);
            return docReturnDetailDao.query(baseQuery);
        }

       public List<DocReturnDetail> queryJoin4Page(String shopId){
        return docReturnDetailDao.queryJoin4Page(shopId);
       }



}

