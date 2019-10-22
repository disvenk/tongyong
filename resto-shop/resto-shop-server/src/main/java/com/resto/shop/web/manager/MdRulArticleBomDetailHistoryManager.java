package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdRulArticleBomDetailHistoryDao;
import com.resto.shop.web.model.MdRulArticleBomDetailHistory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("mdRulArticleBomDetailHistoryManager")
public class MdRulArticleBomDetailHistoryManager extends BaseManager<MdRulArticleBomDetailHistory> {
    @Resource
    private MdRulArticleBomDetailHistoryDao mdRulArticleBomDetailHistoryDao;


    @Override
    public BaseDao<MdRulArticleBomDetailHistory> getDao() {
        return this.mdRulArticleBomDetailHistoryDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdRulArticleBomDetailHistoryDao.xml",
               MdRulArticleBomDetailHistoryDao.class, MdRulArticleBomDetailHistory.class, "md_rul_article_bom_detail_history",false);
    }


}
