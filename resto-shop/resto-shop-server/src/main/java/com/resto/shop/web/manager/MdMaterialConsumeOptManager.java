package com.resto.shop.web.manager;

import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdMaterialConsumeOptDao;
import com.resto.shop.web.exception.ScmPersistenceException;
import com.resto.shop.web.model.MdMaterialConsumeOpt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("mdMaterialConsumeOptManager")
public class MdMaterialConsumeOptManager extends BaseManager<MdMaterialConsumeOpt> {

    @Resource
    private MdMaterialConsumeOptDao mdMaterialConsumeOptDao;

    @Override
    public BaseDao<MdMaterialConsumeOpt> getDao() {
        return this.mdMaterialConsumeOptDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdMaterialConsumeOptDao.xml",
               MdMaterialConsumeOptDao.class, MdMaterialConsumeOpt.class, "md_material_consume_opt",false);
    }


    /**
     * update 语句where条件后面的参数
     * @param whereMdMaterialConsumeOpt
     * update 语句set后面的参数
     * @param updateMdMaterialConsumeOpt
     */
     public Integer updateMdMaterialConsumeOptOfCommon(MdMaterialConsumeOpt whereMdMaterialConsumeOpt,MdMaterialConsumeOpt updateMdMaterialConsumeOpt) {
       if(whereMdMaterialConsumeOpt == null || updateMdMaterialConsumeOpt == null){
             throw new ScmPersistenceException("whereMdMaterialConsumeOpt或者updateMdMaterialConsumeOpt不能为空");
        }
        Long id = whereMdMaterialConsumeOpt.getId();

        if(id == null){
            throw new ScmPersistenceException("修改实体id不能为空");
        }
        BaseQuery<MdMaterialConsumeOpt> query = BaseQuery.getInstance(whereMdMaterialConsumeOpt);

        UpdateByQuery<MdMaterialConsumeOpt> updateByQuery = new UpdateByQuery<>(query,updateMdMaterialConsumeOpt);
        return mdMaterialConsumeOptDao.updateByQuery(updateByQuery);
     }



    /**
     * select 语句where条件后面的参数
     * @param mdMaterialConsumeOpt
     */
       public List<MdMaterialConsumeOpt> findMdMaterialConsumeOptsByMdMaterialConsumeOpt(MdMaterialConsumeOpt mdMaterialConsumeOpt) {
            BaseQuery<MdMaterialConsumeOpt> baseQuery = BaseQuery.getInstance(mdMaterialConsumeOpt);
            return mdMaterialConsumeOptDao.query(baseQuery);
        }

       public List<MdMaterialConsumeOpt> queryJoin4Page(String shopId){
        return mdMaterialConsumeOptDao.queryJoin4Page(shopId);
       }


    public MdMaterialConsumeOpt findMdMaterialConsumeLimitOne(String shopId) {
        return mdMaterialConsumeOptDao.findMdMaterialConsumeLimitOne(shopId);
    }
}

