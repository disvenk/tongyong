package com.resto.shop.web.manager;

import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdBillDao;
import com.resto.shop.web.dto.DocStkInPlanHeaderDetailDo;
import com.resto.shop.web.dto.MdBillDo;
import com.resto.shop.web.exception.ScmPersistenceException;
import com.resto.shop.web.model.MdBill;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("mdBillManager")
public class MdBillManager extends BaseManager<MdBill> {

    @Resource
    private MdBillDao mdBillDao;

    @Override
    public BaseDao<MdBill> getDao() {
        return this.mdBillDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdBillDao.xml",
               MdBillDao.class, MdBill.class, "md_bill",false);
    }


    /**
     * update 语句where条件后面的参数
     * @param whereMdBill
     * update 语句set后面的参数
     * @param updateMdBill
     */
     public Integer updateMdBillOfCommon(MdBill whereMdBill,MdBill updateMdBill) {
       if(whereMdBill == null || updateMdBill == null){
             throw new ScmPersistenceException("whereMdBill或者updateMdBill不能为空");
        }
        Long id = whereMdBill.getId();

        if(id == null){
            throw new ScmPersistenceException("修改实体id不能为空");
        }
        BaseQuery<MdBill> query = BaseQuery.getInstance(whereMdBill);

        UpdateByQuery<MdBill> updateByQuery = new UpdateByQuery<>(query,updateMdBill);
        return mdBillDao.updateByQuery(updateByQuery);
     }



    /**
     * select 语句where条件后面的参数
     * @param mdBill
     */
       public List<MdBill> findMdBillsByMdBill(MdBill mdBill) {
            BaseQuery<MdBill> baseQuery = BaseQuery.getInstance(mdBill);
            return mdBillDao.query(baseQuery);
        }

       public List<MdBillDo> queryJoin4Page(String shopId, String beginDate, String endDate){
        return mdBillDao.queryJoin4Page(shopId,beginDate,endDate);
       }


    public List<DocStkInPlanHeaderDetailDo> queryJoin4PageAndBill(String shopId, String beginDate, String endDate) {
        return mdBillDao.queryJoin4PageAndBill(shopId,beginDate,endDate);
    }
}

