package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdSupplierContactDao;
import com.resto.shop.web.model.MdSupplierContact;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("mdSupplierContactManager")
public class MdSupplierContactManager extends BaseManager<MdSupplierContact> {
    @Resource
    private MdSupplierContactDao mdSupplierContactDao;


    @Override
    public BaseDao<MdSupplierContact> getDao() {
        return this.mdSupplierContactDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdSupplierContactDao.xml",
               MdSupplierContactDao.class, MdSupplierContact.class, "md_supplier_contact",false);
    }


}
