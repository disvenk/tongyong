package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.DoOperateDao;
import com.resto.shop.web.model.DoOperateLog;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("doOperateDaoManager")
public class DoOperateManager extends BaseManager<DoOperateLog> {
    @Resource
    private DoOperateDao doOperateDao;


    @Override
    public BaseDao<DoOperateLog> getDao() {
        return this.doOperateDao;
    }


    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\DoOperateDao.xml",
               DoOperateDao.class, DoOperateLog.class, "do_operate_log",false);
    }


}
