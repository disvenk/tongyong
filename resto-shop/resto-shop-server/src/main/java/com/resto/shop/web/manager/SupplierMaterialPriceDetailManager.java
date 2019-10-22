package com.resto.shop.web.manager;



import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.SupplierMaterialPriceDetailDao;
import com.resto.shop.web.model.MdSupplierPriceDetail;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component("supplierMaterialPriceDetailManager")
public class SupplierMaterialPriceDetailManager extends BaseManager<MdSupplierPriceDetail> {
    @Resource
    private SupplierMaterialPriceDetailDao priceDetailDao;


    @Override
    public BaseDao<MdSupplierPriceDetail> getDao() {
        return this.priceDetailDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\SupplierMaterialPriceDetailDao.xml",
               SupplierMaterialPriceDetailDao.class, MdSupplierPriceDetail.class, "md_supplier_price_detail",false);
    }


    public List<MdSupplierPriceDetail> queryJoinMaterialView(Long supPriceId, Integer version) {
        return  priceDetailDao.queryJoinMaterialView(supPriceId,version);

    }
}
