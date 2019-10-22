package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdRulArticleBomDetailDao;
import com.resto.shop.web.model.MdRulArticleBomDetail;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component("mdRulArticleBomDetailManager")
public class MdRulArticleBomDetailManager extends BaseManager<MdRulArticleBomDetail> {
    @Resource
    private MdRulArticleBomDetailDao mdRulArticleBomDetailDao;


    @Override
    public BaseDao<MdRulArticleBomDetail> getDao() {
        return this.mdRulArticleBomDetailDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdRulArticleBomDetailDao.xml",
               MdRulArticleBomDetailDao.class, MdRulArticleBomDetail.class, "md_rul_article_bom_detail",false);
    }




    public List<MdRulArticleBomDetail> findEffectiveBomDetailByState(Long articleBomHeadId, Integer state) {
        BaseQuery<MdRulArticleBomDetail> baseQuery =BaseQuery.getInstance(new MdRulArticleBomDetail());
        baseQuery.getData().setArticleBomHeadId(articleBomHeadId);
        baseQuery.getData().setState(state);
        return mdRulArticleBomDetailDao.query(baseQuery);
    }
}
