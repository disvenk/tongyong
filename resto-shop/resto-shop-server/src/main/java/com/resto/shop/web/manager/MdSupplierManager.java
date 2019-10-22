package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdSupplierDao;
import com.resto.shop.web.dto.MdSupplierAndContactDo;
import com.resto.shop.web.dto.SupplierAndPmsHeadDo;
import com.resto.shop.web.dto.SupplierAndSupPriceDo;
import com.resto.shop.web.model.MdSupplier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("mdSupplierManager")
public class MdSupplierManager extends BaseManager<MdSupplier> {
    @Resource
    private MdSupplierDao mdSupplierDao;


    @Override
    public BaseDao<MdSupplier> getDao() {
        return this.mdSupplierDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdSupplierDao.xml",
               MdSupplierDao.class, MdSupplier.class, "md_supplier",false);
    }

    public List<MdSupplierAndContactDo> queryJoin4Page(String shopDetailId, Integer state) {
        return mdSupplierDao.queryJoin4Page(shopDetailId,state);
    }

    public List<SupplierAndSupPriceDo> querySupplierAndSupPrice(String shopId) {
        return mdSupplierDao.querySupplierAndSupPrice(shopId);
    }

    public List<SupplierAndPmsHeadDo> querySupplierAndPmsHeadDo(String shopId) {
        return mdSupplierDao.querySupplierAndPmsHeadDo(shopId);
    }
}
