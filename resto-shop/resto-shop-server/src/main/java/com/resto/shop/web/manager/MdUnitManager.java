package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdUnitDao;
import com.resto.shop.web.model.MdUnit;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("mdUnitManager")
public class MdUnitManager extends BaseManager<MdUnit> {
    @Resource
    private MdUnitDao mdUnitDao;


    @Override
    public BaseDao<MdUnit> getDao() {
        return this.mdUnitDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdUnitDao.xml",
               MdUnitDao.class, MdUnit.class, "md_unit",false);
    }


}
