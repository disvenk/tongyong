package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.SupplierMaterialPriceHeadDao;
import com.resto.shop.web.dto.MdSupplierPriceHeadDo;
import com.resto.shop.web.model.MdSupplierPriceHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;


@Component("supplierMaterialPriceHeadManager")
public class SupplierMaterialPriceHeadManager extends BaseManager<MdSupplierPriceHead> {
    @Autowired
    private SupplierMaterialPriceHeadDao priceHeadDao;


    @Override
    public BaseDao<MdSupplierPriceHead> getDao() {
        return this.priceHeadDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\SupplierMaterialPriceHeadDao.xml",
               SupplierMaterialPriceHeadDao.class, MdSupplierPriceHead.class, "md_supplier_price_head",false);
    }


    public List<MdSupplierPriceHeadDo> queryJoin4Page(String shopDetailId) {
        return  priceHeadDao.queryJoin4Page(shopDetailId);
    }

    public List<MdSupplierPriceHeadDo> findEffectiveList(String shopDetailId,Long supplierId){
        return priceHeadDao.findEffectiveList(shopDetailId,supplierId);
    }

}
