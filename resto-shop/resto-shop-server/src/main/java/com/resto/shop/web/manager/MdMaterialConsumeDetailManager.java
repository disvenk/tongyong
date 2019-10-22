package com.resto.shop.web.manager;

import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdMaterialConsumeDetailDao;
import com.resto.shop.web.exception.ScmPersistenceException;
import com.resto.shop.web.model.MdMaterialConsumeDetail;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("mdMaterialConsumeDetailManager")
public class MdMaterialConsumeDetailManager extends BaseManager<MdMaterialConsumeDetail> {

    @Resource
    private MdMaterialConsumeDetailDao mdMaterialConsumeDetailDao;

    @Override
    public BaseDao<MdMaterialConsumeDetail> getDao() {
        return this.mdMaterialConsumeDetailDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdMaterialConsumeDetailDao.xml",
               MdMaterialConsumeDetailDao.class, MdMaterialConsumeDetail.class, "md_material_consume_detail",false);
    }
    /**
     * update 语句where条件后面的参数
     * @param whereMdMaterialConsumeDetail
     * update 语句set后面的参数
     * @param updateMdMaterialConsumeDetail
     */
     public Integer updateMdMaterialConsumeDetailOfCommon(MdMaterialConsumeDetail whereMdMaterialConsumeDetail,MdMaterialConsumeDetail updateMdMaterialConsumeDetail) {
       if(whereMdMaterialConsumeDetail == null || updateMdMaterialConsumeDetail == null){
             throw new ScmPersistenceException("whereMdMaterialConsumeDetail或者updateMdMaterialConsumeDetail不能为空");
        }
        Long id = whereMdMaterialConsumeDetail.getId();

        if(id == null){
            throw new ScmPersistenceException("修改实体id不能为空");
        }
        BaseQuery<MdMaterialConsumeDetail> query = BaseQuery.getInstance(whereMdMaterialConsumeDetail);

        UpdateByQuery<MdMaterialConsumeDetail> updateByQuery = new UpdateByQuery<>(query,updateMdMaterialConsumeDetail);
        return mdMaterialConsumeDetailDao.updateByQuery(updateByQuery);
     }

    /**
     * select 语句where条件后面的参数
     * @param mdMaterialConsumeDetail
     */
       public List<MdMaterialConsumeDetail> findMdMaterialConsumeDetailsByMdMaterialConsumeDetail(MdMaterialConsumeDetail mdMaterialConsumeDetail) {
            BaseQuery<MdMaterialConsumeDetail> baseQuery = BaseQuery.getInstance(mdMaterialConsumeDetail);
            return mdMaterialConsumeDetailDao.query(baseQuery);
        }

       public List<MdMaterialConsumeDetail> queryJoin4Page(String shopId){
        return mdMaterialConsumeDetailDao.queryJoin4Page(shopId);
       }



}

