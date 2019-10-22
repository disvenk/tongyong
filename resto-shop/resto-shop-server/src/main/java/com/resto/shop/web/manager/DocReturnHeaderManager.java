package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DocReturnHeaderDao;
import com.resto.shop.web.dto.DocReturnHeaderDetailDo;
import com.resto.shop.web.exception.ScmPersistenceException;
import com.resto.shop.web.model.DocReturnHeader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("docReturnHeaderManager")
public class DocReturnHeaderManager extends BaseManager<DocReturnHeader> {

    @Resource
    private DocReturnHeaderDao docReturnHeaderDao;

    @Override
    public BaseDao<DocReturnHeader> getDao() {
        return this.docReturnHeaderDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DocReturnHeaderDao.xml",
               DocReturnHeaderDao.class, DocReturnHeader.class, "doc_return_header",false);
    }


    /**
     * update 语句where条件后面的参数
     * @param whereDocReturnHeader
     * update 语句set后面的参数
     * @param updateDocReturnHeader
     */
     public Integer updateDocReturnHeaderOfCommon(DocReturnHeader whereDocReturnHeader,DocReturnHeader updateDocReturnHeader) {
       if(whereDocReturnHeader == null || updateDocReturnHeader == null){
             throw new ScmPersistenceException("whereDocReturnHeader或者updateDocReturnHeader不能为空");
        }
        Long id = whereDocReturnHeader.getId();

        if(id == null){
            throw new ScmPersistenceException("修改实体id不能为空");
        }
        BaseQuery<DocReturnHeader> query = BaseQuery.getInstance(whereDocReturnHeader);

        UpdateByQuery<DocReturnHeader> updateByQuery = new UpdateByQuery<>(query,updateDocReturnHeader);
        return docReturnHeaderDao.updateByQuery(updateByQuery);
     }



    /**
     * select 语句where条件后面的参数
     * @param docReturnHeader
     */
       public List<DocReturnHeader> findDocReturnHeadersByDocReturnHeader(DocReturnHeader docReturnHeader) {
            BaseQuery<DocReturnHeader> baseQuery = BaseQuery.getInstance(docReturnHeader);
            return docReturnHeaderDao.query(baseQuery);
        }

       public List<DocReturnHeaderDetailDo> queryJoin4Page(String shopId){
        return docReturnHeaderDao.queryJoin4Page(shopId);
       }



}

