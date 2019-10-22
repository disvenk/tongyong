package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdRulArticleBomHeadHistoryDao;
import com.resto.shop.web.dto.MdRulArticleBomHeadDtailDo;
import com.resto.shop.web.model.MdRulArticleBomHeadHistory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component("mdRulArticleBomHeadHistoryManager")
public class MdRulArticleBomHeadHistoryManager extends BaseManager<MdRulArticleBomHeadHistory> {
    @Resource
    private MdRulArticleBomHeadHistoryDao mdRulArticleBomHeadHistoryDao;


    @Override
    public BaseDao<MdRulArticleBomHeadHistory> getDao() {
        return this.mdRulArticleBomHeadHistoryDao;
    }



    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdRulArticleBomHeadHistoryDao.xml",
                MdRulArticleBomHeadHistoryDao.class, MdRulArticleBomHeadHistory.class, "md_rul_article_bom_head_history",false);
    }

    public List<MdRulArticleBomHeadDtailDo> queryJoin4Page(String shopDetailId, String articleId) {
        return  mdRulArticleBomHeadHistoryDao.queryJoin4Page(shopDetailId,articleId);
    }


    public List<MdRulArticleBomHeadHistory> findHistoryBomHeadByState(String shopDetailId, String articleId, Integer state) {

        BaseQuery<MdRulArticleBomHeadHistory> baseQuery = BaseQuery.getInstance(new MdRulArticleBomHeadHistory());
        baseQuery.getData().setArticleId(articleId);
        baseQuery.getData().setShopDetailId(shopDetailId);
        baseQuery.getData().setState(state);
        return mdRulArticleBomHeadHistoryDao.query(baseQuery);
    }
}
