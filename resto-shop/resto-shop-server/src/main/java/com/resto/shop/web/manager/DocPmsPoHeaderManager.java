package com.resto.shop.web.manager;

import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DocPmsPoHeaderDao;
import com.resto.shop.web.dto.DocPmsPoDetailDo;
import com.resto.shop.web.dto.DocPmsPoHeaderDetailDo;
import com.resto.shop.web.exception.ScmPersistenceException;
import com.resto.shop.web.model.DocPmsPoHeader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("docPmsPoHeaderManager")
public class DocPmsPoHeaderManager extends BaseManager<DocPmsPoHeader> {

    @Resource
    private DocPmsPoHeaderDao docPmsPoHeaderDao;

    @Override
    public BaseDao<DocPmsPoHeader> getDao() {
        return this.docPmsPoHeaderDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DocPmsPoHeaderDao.xml",
               DocPmsPoHeaderDao.class, DocPmsPoHeader.class, "doc_pms_po_header",false);
    }


    /**
     * update 语句where条件后面的参数
     * @param whereDocPmsPoHeader
     * update 语句set后面的参数
     * @param updateDocPmsPoHeader
     */
     public Integer updateDocPmsPoHeaderOfCommon(DocPmsPoHeader whereDocPmsPoHeader,DocPmsPoHeader updateDocPmsPoHeader) {
       if(whereDocPmsPoHeader == null || updateDocPmsPoHeader == null){
             throw new ScmPersistenceException("whereDocPmsPoHeader或者updateDocPmsPoHeader不能为空");
        }
        Long id = whereDocPmsPoHeader.getId();

        if(id == null){
            throw new ScmPersistenceException("修改实体id不能为空");
        }
        BaseQuery<DocPmsPoHeader> query = BaseQuery.getInstance(whereDocPmsPoHeader);

        UpdateByQuery<DocPmsPoHeader> updateByQuery = new UpdateByQuery<>(query,updateDocPmsPoHeader);
        return docPmsPoHeaderDao.updateByQuery(updateByQuery);
     }

    /**
     * select 语句where条件后面的参数
     * @param docPmsPoHeader
     */
       public List<DocPmsPoHeader> findDocPmsPoHeadersByDocPmsPoHeader(DocPmsPoHeader docPmsPoHeader) {
            BaseQuery<DocPmsPoHeader> baseQuery = BaseQuery.getInstance(docPmsPoHeader);
            return docPmsPoHeaderDao.query(baseQuery);
        }

    public List<DocPmsPoHeaderDetailDo> queryJoin4Page(String shopId) {

        return docPmsPoHeaderDao.queryJoin4Page(shopId);
    }

    public List<DocPmsPoDetailDo> queryDocPmsPoDetailDos(String scmDocPmsPoHeaderId) {
        return docPmsPoHeaderDao.queryDocPmsPoDetailDos(scmDocPmsPoHeaderId);
    }
}

