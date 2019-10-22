package com.resto.shop.web.service.impl;


import com.resto.brand.core.util.JsonUtils;
import com.resto.shop.web.dto.ArticleBomDo;
import com.resto.shop.web.dto.ArticleSellCountDto;
import com.resto.shop.web.manager.MdRulArticleBomHeadManager;
import com.resto.shop.web.service.ArticleSaleService;
import com.resto.shop.web.util.ListUtil;
import com.resto.shop.web.util.log.LogBetter;
import com.resto.shop.web.util.log.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.resto.shop.web.util.log.TraceLoggerFactory .logger;


/**
 *
 * 移动加权平均法计算公式是：
 * 移动平均单价=（本次进货的成本+原有库存的成本）/（本次进货数量+原有存货数量）
 * 本批发出存货成本=本批发出存货数量×存货当前移动平均单价
 * 菜品消耗计算
 */
@Component
@Service
public class ArticleSaleServiceImpl implements ArticleSaleService {


    @Autowired
    private MdRulArticleBomHeadManager mdRulArticleBomHeadManager;


    @Override
    public   Map<Long, List<ArticleBomDo>>  countMaterialStockSellGroupByMaterialId(String shopId, String lastCountTime, List<ArticleSellCountDto> articleSellCountDtos) {
        //查询菜品销售，原始数据统计 //数据库已经完成分组
        //List<ArticleSellCountDto> articleSellCountDtos = mdRulArticleBomHeadManager.findArticleByLastCountTime(shopId,lastCountTime);
        if(ListUtil.isNotEmpty(articleSellCountDtos)){
            return countStockByMaterialId(shopId, articleSellCountDtos);
        }
        return new HashMap<>();
    }


    private   Map<Long, List<ArticleBomDo>>  countStockByMaterialId(String shopId, List<ArticleSellCountDto> articleSellCountDtos) {
        //查询bom清单,菜品维度统计单个菜品消耗原材料
        List<ArticleBomDo> articleBomDos =mdRulArticleBomHeadManager.queryBom(shopId);
        //分组
        Map<String, List<ArticleBomDo>> articleBomDoMap = articleBomDos.stream().collect(Collectors.groupingBy(ArticleBomDo::getArticleId));
        //校验并记录日志
        checkSellArticleSizeGtArticleBomSize(articleSellCountDtos, articleBomDos);

        // 将菜品销量，扁平到具体的原料,并且以原料分组
        Map<Long, List<ArticleBomDo>> listMap = articleSellToflatMapMaterialGroupByMaterialId(articleSellCountDtos,articleBomDoMap);


        return listMap ;
    }



    private void checkSellArticleSizeGtArticleBomSize(List<ArticleSellCountDto> articleSellCountDtos, List<ArticleBomDo> articleBomDos) {
        if(articleSellCountDtos.size()>articleBomDos.size()){
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[SCM-菜品销售菜品种类大于新建bom清单上面的菜品种类，会引起销售的菜品没有bom清单，导致所卖的菜品，没有bom计算，引起误差]")
                    .log();
            List<String> articleIds = new ArrayList<>();
            //分组
            Map<String, List<ArticleBomDo>> articleBomDoMap = articleBomDos.stream().collect(Collectors.groupingBy(ArticleBomDo::getArticleId));
            for (ArticleSellCountDto articleSellCountDto:articleSellCountDtos) {
                if(ListUtil.isNullOrEmpty(articleBomDoMap.get(articleSellCountDto.getArticleId()))){
                    articleIds.add(articleSellCountDto.getArticleId());
                }
            }
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .addParm("没有设置清单的articleIds===", JsonUtils.objectToJson(articleIds))
                    .log();
        }
    }

    /***
     * map<String,List<ArticleBomDo>> maps
     * 2，将maps里面的values 转化成newDo   put 到一个新的list<newDo>中 newDos
     *             newDo
     *                          ---articleId
     *                          ---销量
     *                          --- 原料1------规格 单位
     *                              原料2------规格 单位
     *                              原料3------规格 单位
     * 3，对newDos进行分组 groupByMaterialId
     *
     * 4,统计同一个原料id消耗的总的数量
     *
     */


    private Map<Long, List<ArticleBomDo>> articleSellToflatMapMaterialGroupByMaterialId(List<ArticleSellCountDto> articleSellCounts, Map<String, List<ArticleBomDo>> articleBomDoMap) {
        // 1，将第一个map统计结束的销量   组装到第二个map中
        List<ArticleBomDo> allArticleBomDos = new ArrayList<>();

        for (ArticleSellCountDto articleSellCountDto:articleSellCounts){
            // 2,将maps里面的values 转化成newDo   put 到一个新的list<newDo>中 newDos
            List<ArticleBomDo> values = articleBomDoMap.get(articleSellCountDto.getArticleId());
//            List<ArticleBomDo> newValues = new ArrayList<>();
            if(ListUtil.isNotEmpty(values)){
                for (ArticleBomDo each:values) {
                    each.setArticleMealFeeNumber(new BigDecimal(articleSellCountDto.getMealFeeNumber()));
                    each.setArticleTotalCount(new BigDecimal(articleSellCountDto.getTotalCount()));
//                    newValues.add(each);
                }

                allArticleBomDos.addAll(values);
            }


        }
        //3,对newDos进行分组 groupByMaterialId
        return allArticleBomDos.stream().collect(Collectors.groupingBy(ArticleBomDo::getMaterialId));
    }


}
