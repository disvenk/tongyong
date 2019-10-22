package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdRulArticleBomHeadDao;
import com.resto.shop.web.dto.ArticleBomDo;
import com.resto.shop.web.dto.ArticleSellCountDto;
import com.resto.shop.web.dto.MdRulArticleBomHeadDtailDo;
import com.resto.shop.web.model.MdRulArticleBomHead;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component("mdRulArticleBomHeadManager")
public class MdRulArticleBomHeadManager extends BaseManager<MdRulArticleBomHead> {
    @Resource
    private MdRulArticleBomHeadDao mdRulArticleBomHeadDao;


    @Override
    public BaseDao<MdRulArticleBomHead> getDao() {
        return this.mdRulArticleBomHeadDao;
    }

   public List<MdRulArticleBomHeadDtailDo> queryJoin4Page(String shopDetailId){


       return mdRulArticleBomHeadDao.queryJoin4Page(shopDetailId);
    }

    public static void main(String[] args) {

        DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdRulArticleBomHeadDao.xml",
                MdRulArticleBomHeadDao.class, MdRulArticleBomHead.class, "md_rul_article_bom_head",false);
    }


    public List<ArticleBomDo> queryBom(String shopId) {

        return  mdRulArticleBomHeadDao.queryBom(shopId);
    }

    public List<ArticleSellCountDto> findArticleByLastCountTime(String shopId, String lastCountTime){
        return  mdRulArticleBomHeadDao.findArticleByLastCountTime(shopId,lastCountTime);
    }

    public List<MdRulArticleBomHead> findBomHeadByState(String shopDetailId, String articleId, Integer state) {
        BaseQuery<MdRulArticleBomHead> baseQuery = BaseQuery.getInstance(new MdRulArticleBomHead());
        baseQuery.getData().setArticleId(articleId);
        baseQuery.getData().setShopDetailId(shopDetailId);
        baseQuery.getData().setState(state);
        return mdRulArticleBomHeadDao.query(baseQuery);
    }


}
