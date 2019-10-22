package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.dto.MdRulArticleBomDetailDo;
import com.resto.shop.web.dto.MdRulArticleBomHeadDo;
import com.resto.shop.web.dto.MdRulArticleBomHeadDtailDo;
import com.resto.shop.web.enums.MaterialTypeStatus;
import com.resto.shop.web.exception.ScmServiceException;
import com.resto.shop.web.manager.MdRulArticleBomDetailHistoryManager;
import com.resto.shop.web.manager.MdRulArticleBomDetailManager;
import com.resto.shop.web.manager.MdRulArticleBomHeadHistoryManager;
import com.resto.shop.web.manager.MdRulArticleBomHeadManager;
import com.resto.shop.web.model.MdRulArticleBomDetail;
import com.resto.shop.web.model.MdRulArticleBomDetailHistory;
import com.resto.shop.web.model.MdRulArticleBomHead;
import com.resto.shop.web.model.MdRulArticleBomHeadHistory;
import com.resto.shop.web.service.ArticleBomHeadHistoryService;
import com.resto.shop.web.util.DateUtil;
import com.resto.shop.web.util.ListUtil;
import com.resto.shop.web.util.log.LogBetter;
import com.resto.shop.web.util.log.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.resto.shop.web.util.log.TraceLoggerFactory .logger;



/**
 *
 * 菜品bom清单管理
 */
@Component
@Service
public class ArticleBomHeadHistoryServiceImpl implements ArticleBomHeadHistoryService {

    @Autowired
   private MdRulArticleBomDetailManager mdRulArticleBomDetailManager;


    @Autowired
    private MdRulArticleBomHeadManager mdRulArticleBomHeadManager;

    @Autowired
    private MdRulArticleBomHeadHistoryManager mdRulArticleBomHeadHistoryManager;
    @Autowired
    private MdRulArticleBomDetailHistoryManager mdRulArticleBomDetailHistoryManager;


    @Override
    public List<MdRulArticleBomHeadDo> queryJoin4Page(String shopDetailId, String articleId) {
        List<MdRulArticleBomHeadDtailDo> mdRulArticleBomHeadDtailDos = mdRulArticleBomHeadHistoryManager.queryJoin4Page(shopDetailId,articleId);
        Map<Long, List<MdRulArticleBomHeadDtailDo>> collect = mdRulArticleBomHeadDtailDos.stream().collect(Collectors.groupingBy(MdRulArticleBomHeadDtailDo::getId));
        List<MdRulArticleBomHeadDo> mdRulArticleBomHeadDos =new ArrayList<>(collect.size());
        for (Map.Entry entity:collect.entrySet()) {
            List<MdRulArticleBomHeadDtailDo> values =(List<MdRulArticleBomHeadDtailDo>)entity.getValue();
            MdRulArticleBomHeadDo mdRulArticleBomHeadDo = convertMdRulArticleBomHeadDo(values);
            if(mdRulArticleBomHeadDo !=null){
                mdRulArticleBomHeadDos.add(mdRulArticleBomHeadDo);
            }
        }
        return mdRulArticleBomHeadDos;
    }

    private MdRulArticleBomHeadDo convertMdRulArticleBomHeadDo(List<MdRulArticleBomHeadDtailDo> values) {
        MdRulArticleBomHeadDo mdRulArticleBomHeadDo =new MdRulArticleBomHeadDo();
        if(ListUtil.isNotEmpty(values)){
            MdRulArticleBomHeadDtailDo mdRulArticleBomHeadDtailDo = values.get(0);
            //组装头
            mdRulArticleBomHeadDo.setId(mdRulArticleBomHeadDtailDo.getId());
            mdRulArticleBomHeadDo.setArticleId(mdRulArticleBomHeadDtailDo.getArticleId());
            mdRulArticleBomHeadDo.setCreaterId(mdRulArticleBomHeadDtailDo.getCreaterId());
            mdRulArticleBomHeadDo.setArticleFamilyId(mdRulArticleBomHeadDtailDo.getArticleFamilyId());
            mdRulArticleBomHeadDo.setCreaterName(mdRulArticleBomHeadDtailDo.getCreaterName());
            mdRulArticleBomHeadDo.setMeasurementUnit(mdRulArticleBomHeadDtailDo.getMeasurementUnit());
            mdRulArticleBomHeadDo.setPriority(mdRulArticleBomHeadDtailDo.getPriority());
            mdRulArticleBomHeadDo.setProductCategory(mdRulArticleBomHeadDtailDo.getProductCategory());
            mdRulArticleBomHeadDo.setProductCode(mdRulArticleBomHeadDtailDo.getProductCode());
            mdRulArticleBomHeadDo.setProductName(mdRulArticleBomHeadDtailDo.getProductName());
            mdRulArticleBomHeadDo.setShopDetailId(mdRulArticleBomHeadDtailDo.getShopDetailId());
            mdRulArticleBomHeadDo.setVersion(mdRulArticleBomHeadDtailDo.getVersion());
            mdRulArticleBomHeadDo.setBomCode(mdRulArticleBomHeadDtailDo.getBomCode());
            mdRulArticleBomHeadDo.setSize(values.size());
            mdRulArticleBomHeadDo.setArticleName(mdRulArticleBomHeadDtailDo.getArticleName());
            mdRulArticleBomHeadDo.setFamilyName(mdRulArticleBomHeadDtailDo.getFamilyName());
            mdRulArticleBomHeadDo.setProducer(mdRulArticleBomHeadDtailDo.getProducer());
            mdRulArticleBomHeadDo.setEndEffect(DateUtil.formatDateStr(mdRulArticleBomHeadDtailDo.getEndEffect(),DateUtil.DATE_PATTERN));
            mdRulArticleBomHeadDo.setStartEffect(DateUtil.formatDateStr(mdRulArticleBomHeadDtailDo.getStartEffect(),DateUtil.DATE_PATTERN));
            mdRulArticleBomHeadDo.setState(mdRulArticleBomHeadDtailDo.getState());
            mdRulArticleBomHeadDo.setMaxVersion(mdRulArticleBomHeadDtailDo.getMaxVersion());
            List<MdRulArticleBomDetailDo> bomDetailDoList = new ArrayList<>();
            //组装detail
            for (MdRulArticleBomHeadDtailDo each:values) {
                bomDetailDoList.add(convertMdRulArticleBomDetailDo(each,mdRulArticleBomHeadDtailDo.getVersion()));
            }
            mdRulArticleBomHeadDo.setBomDetailDoList(bomDetailDoList);
        }

        return mdRulArticleBomHeadDo;
    }

    private MdRulArticleBomDetailDo convertMdRulArticleBomDetailDo(MdRulArticleBomHeadDtailDo each,Integer version) {
        MdRulArticleBomDetailDo bomDetail = new MdRulArticleBomDetailDo();
        bomDetail.setId(each.getBomDetailId());
        bomDetail.setArticleBomHeadId(each.getId());
        bomDetail.setCreaterId(each.getCreaterId());
        bomDetail.setCreaterName(each.getCreaterName());
        bomDetail.setMaterialCount(each.getMaterialCount());
        bomDetail.setMaterialId(each.getMaterialId());
        bomDetail.setMaterialCode(each.getMaterialCode());
        bomDetail.setMaterialName(each.getMaterialName());
        bomDetail.setMaterialType(each.getMaterialType());
        bomDetail.setMinMeasureUnit(each.getMinMeasureUnit());
        bomDetail.setSpecName(each.getSpecName());
        bomDetail.setVersion(version);
        bomDetail.setUnitName(each.getUnitName());
        bomDetail.setMinUnitName(each.getMinUnitName());
        bomDetail.setMeasureUnit(each.getMeasureUnit());
        bomDetail.setMaterialTypeShow(MaterialTypeStatus.valueOf(each.getMaterialType()).getDescription());
        bomDetail.setRate(each.getRate());
        bomDetail.setCoefficient(each.getCoefficient());
        return bomDetail;
    }





    @Override
    public Long updateRulArticleBomHead(MdRulArticleBomHeadDo mdRulArticleBomHeadDo) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[SCM-修改菜品BOM清单开始]")
                .addParm("菜品BOM清单信息", mdRulArticleBomHeadDo)
                .log();
        if (null == mdRulArticleBomHeadDo || mdRulArticleBomHeadDo.getId() == null || ListUtil.isNullOrEmpty(mdRulArticleBomHeadDo.getBomDetailDoList())) {
            throw new ScmServiceException("SCM 修改BOM清单为空或者headerId 为空或明细不存在");
        }
        /**
         *  检查是否要保存备份（草稿）是否启用
         *           是---将之前启用状态的bom的移入历史表,标记未启用，并将其从主表删除 ,将修改后的记录存入当前表
         *           否---修改后的记录存入当前表,标记未启用
         */
        boolean isUpdateVersionFlag = false;
        if(Integer.valueOf(1).equals(mdRulArticleBomHeadDo.getState())){//启用
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[编辑的时候直接启用]")
                    .addParm("菜品BOM清单信息", mdRulArticleBomHeadDo)
                    .log();

            isUpdateVersionFlag =checkBomAndSysnHistoryAndRemove(mdRulArticleBomHeadDo.getShopDetailId(), mdRulArticleBomHeadDo.getArticleId(), mdRulArticleBomHeadDo.getState());
        }

        return updateBom(mdRulArticleBomHeadDo,isUpdateVersionFlag);

    }



    /**
     * @param shopDetailId
     * @param articleId
     * @param state
     * @return 返回标示符-------是否修改版本号
     * 当修改的时候，只是修改启用状态的时候，是不需要修改版本号的 用于编辑的时候使用
     */
    private Boolean checkBomAndSysnHistoryAndRemove(String shopDetailId,String articleId, Integer state) {
        //查询所以状态的bom
        List<MdRulArticleBomHead> mdRulArticleBomHeads =mdRulArticleBomHeadManager.findBomHeadByState(shopDetailId,articleId,null);
        if(ListUtil.isNotEmpty(mdRulArticleBomHeads)&&Integer.valueOf(1).equals(state)){
            //把当前启用状态的记录存入历史表 并将其从当前表中删除

            MdRulArticleBomHead mdRulArticleBomHead = findEffectiveMdRulArticleBomHead(mdRulArticleBomHeads);

            MdRulArticleBomHeadHistory history = convertMdBomHeadHistory(mdRulArticleBomHead);
            mdRulArticleBomHeadHistoryManager.insert(history);
            List<MdRulArticleBomDetail> mdRulArticleBomDetails =mdRulArticleBomDetailManager.findEffectiveBomDetailByState(mdRulArticleBomHead.getId(),state);
            mdRulArticleBomDetails.forEach(it->{
                mdRulArticleBomDetailHistoryManager.insert(convertMdBomDetailHistory(it,history.getId()));
                if(mdRulArticleBomHeads.size()>1){
                    mdRulArticleBomDetailManager.deletePhysicalById(it.getId());
                }
            });
           if(mdRulArticleBomHeads.size()>1){
                mdRulArticleBomHeadManager.deletePhysicalById(mdRulArticleBomHead.getId());
            }
            return true;
        }

        return false;
    }

    private MdRulArticleBomHead findEffectiveMdRulArticleBomHead(List<MdRulArticleBomHead> mdRulArticleBomHeads) {
        mdRulArticleBomHeads.stream().filter(it -> {
            return  (Integer.valueOf(1).equals(it.getState()));
        });

        return null;
    }

    private MdRulArticleBomDetailHistory convertMdBomDetailHistory(MdRulArticleBomDetail it, Long articleBomHeadId) {
        MdRulArticleBomDetailHistory history =  new MdRulArticleBomDetailHistory();
        history.setLossFactor(it.getLossFactor());
        history.setActLossFactor(it.getActLossFactor());
        history.setArticleBomHeadId(articleBomHeadId);
        history.setCreaterId(it.getCreaterId());
        history.setCreaterName(it.getCreaterName());
        history.setMaterialCount(it.getMaterialCount());
        history.setMaterialId(it.getMaterialId());
        history.setMaterialCode(it.getMaterialCode());
        history.setMaterialName(it.getMaterialName());
        history.setMaterialType(it.getMaterialType());
        history.setMinMeasureUnit(it.getMinMeasureUnit());
        history.setSpecName(it.getSpecName());
        history.setUnitName(it.getUnitName());
        history.setVersion(it.getVersion());
        history.setStartEffect(it.getStartEffect());
        history.setEndEffect(it.getEndEffect());
        history.setState(0);
        history.setGmtCreate(new Date());
        history.setGmtModified(new Date());
        return history;
    }

    private MdRulArticleBomHeadHistory convertMdBomHeadHistory(MdRulArticleBomHead it) {
        MdRulArticleBomHeadHistory history =new MdRulArticleBomHeadHistory();
        history.setArticleId(it.getArticleId());
        history.setCreaterId(it.getCreaterId());
        history.setCreaterId(it.getShopDetailId());
        history.setArticleFamilyId(it.getArticleFamilyId());
        history.setCreaterName(it.getCreaterName());
        history.setMeasurementUnit(it.getMeasurementUnit());
        history.setPriority(it.getPriority());
        history.setProductCategory(it.getProductCategory());
        history.setProductCode(it.getProductCode());
        history.setProductName(it.getProductName());
        history.setShopDetailId(it.getShopDetailId());
        history.setVersion(it.getVersion());
        history.setMaxVersion(it.getMaxVersion()+1);
        history.setBomCode(it.getBomCode());
        history.setStartEffect(it.getStartEffect());
        history.setEndEffect(it.getEndEffect());
        history.setState(0);
        history.setProducer(it.getProducer());
        history.setGmtCreate(new Date());
        history.setGmtModified(new Date());
        return history;
    }




    private Long updateBom(MdRulArticleBomHeadDo mdRulArticleBomHeadDo, boolean isUpdateVersionFlag) {
        //更新头部
        BaseQuery<MdRulArticleBomHead> baseQuery= BaseQuery.getInstance(new MdRulArticleBomHead());
        baseQuery.getData().setId(mdRulArticleBomHeadDo.getId());
        UpdateByQuery<MdRulArticleBomHead> bomHeadQuery =new UpdateByQuery<>(baseQuery,generateArticleBomDoHeaderForUpdate(mdRulArticleBomHeadDo,isUpdateVersionFlag));
        mdRulArticleBomHeadManager.updateByQuery(bomHeadQuery);
        //更新明细  有移除的先删除 MdRulArticleBomDetail
        List<Long> bomDetailDeleteIds = mdRulArticleBomHeadDo.getBomDetailDeleteIds();

        if(ListUtil.isNotEmpty(bomDetailDeleteIds)){
            for (Long id:bomDetailDeleteIds) {
                mdRulArticleBomDetailManager.deletePhysicalById(id);
            }
        }
        //在修改
        List<MdRulArticleBomDetailDo> bomDetailDoList = mdRulArticleBomHeadDo.getBomDetailDoList();
        for (MdRulArticleBomDetailDo each: bomDetailDoList) {
            if(each.getId() != null) {
                BaseQuery<MdRulArticleBomDetail> baseQueryDetail = BaseQuery.getInstance(new MdRulArticleBomDetail());
                baseQueryDetail.getData().setArticleBomHeadId(mdRulArticleBomHeadDo.getId());
                baseQueryDetail.getData().setId(each.getId());
                UpdateByQuery<MdRulArticleBomDetail> updateBaseQueryDetail = new UpdateByQuery<>(baseQueryDetail, generateArticleBomDoDetailForUpdate(each,isUpdateVersionFlag));
                mdRulArticleBomDetailManager.updateByQuery(updateBaseQueryDetail);
            }else {
                MdRulArticleBomDetail bomDetail = new MdRulArticleBomDetail();
                bomDetail.setLossFactor(each.getLossFactor());
                bomDetail.setActLossFactor(each.getActLossFactor());
                bomDetail.setArticleBomHeadId(mdRulArticleBomHeadDo.getId());
                bomDetail.setCreaterId(mdRulArticleBomHeadDo.getCreaterId());
                bomDetail.setCreaterName(mdRulArticleBomHeadDo.getCreaterName());
                bomDetail.setMaterialCount(each.getMaterialCount());
                bomDetail.setMaterialId(each.getMaterialId());
                bomDetail.setMaterialCode(each.getMaterialCode());
                bomDetail.setMaterialName(each.getMaterialName());
                bomDetail.setMinMeasureUnit(each.getMinMeasureUnit());
                bomDetail.setSpecName(each.getSpecName());
                bomDetail.setUnitName(each.getUnitName());
                bomDetail.setVersion(each.getVersion());
                bomDetail.setMaterialType(each.getMaterialType());
                if(isUpdateVersionFlag){
                    bomDetail.setVersion(mdRulArticleBomHeadDo.getVersion()+1);
                }else{
                    bomDetail.setVersion(mdRulArticleBomHeadDo.getVersion());
                }
                bomDetail.setStartEffect(DateUtil.parseDate(mdRulArticleBomHeadDo.getStartEffect(),DateUtil.DATE_PATTERN));
                bomDetail.setEndEffect((DateUtil.parseDate(mdRulArticleBomHeadDo.getEndEffect(),DateUtil.DATE_PATTERN)));
                bomDetail.setState(mdRulArticleBomHeadDo.getState());
                mdRulArticleBomDetailManager.insert(bomDetail);
            }
        }
        return mdRulArticleBomHeadDo.getId();
    }


    private MdRulArticleBomDetail generateArticleBomDoDetailForUpdate(MdRulArticleBomDetailDo mdRulArticleBomDetailDo, boolean isUpdateVersionFlag) {
        MdRulArticleBomDetail bomDetail = new MdRulArticleBomDetail();
        bomDetail.setMinMeasureUnit(mdRulArticleBomDetailDo.getMinMeasureUnit());
        bomDetail.setMaterialCount(mdRulArticleBomDetailDo.getMaterialCount());
        bomDetail.setState(mdRulArticleBomDetailDo.getState());
        if(isUpdateVersionFlag){
            bomDetail.setVersion(mdRulArticleBomDetailDo.getVersion()+1);
        }else{
            bomDetail.setVersion(mdRulArticleBomDetailDo.getVersion());
        }

        return bomDetail;
    }

    private MdRulArticleBomHead generateArticleBomDoHeaderForUpdate(MdRulArticleBomHeadDo mdRulArticleBomHeadDo, boolean isUpdateVersionFlag) {
        MdRulArticleBomHead mdRulArticleBomHead =new MdRulArticleBomHead();
        mdRulArticleBomHead.setArticleId(mdRulArticleBomHeadDo.getArticleId());
        mdRulArticleBomHead.setCreaterId(mdRulArticleBomHeadDo.getCreaterId());
        mdRulArticleBomHead.setGmtModified(new Date());
        mdRulArticleBomHead.setArticleFamilyId(mdRulArticleBomHeadDo.getArticleFamilyId());
        mdRulArticleBomHead.setCreaterName(mdRulArticleBomHeadDo.getCreaterName());
        mdRulArticleBomHead.setMeasurementUnit(mdRulArticleBomHeadDo.getMeasurementUnit());
        mdRulArticleBomHead.setPriority(mdRulArticleBomHeadDo.getPriority());
        mdRulArticleBomHead.setProductCategory(mdRulArticleBomHeadDo.getProductCategory());
        mdRulArticleBomHead.setProductCode(mdRulArticleBomHeadDo.getProductCode());
        mdRulArticleBomHead.setProductName(mdRulArticleBomHeadDo.getProductName());
        if(isUpdateVersionFlag){
            mdRulArticleBomHead.setVersion(mdRulArticleBomHeadDo.getVersion()+1);
            mdRulArticleBomHead.setMaxVersion(mdRulArticleBomHeadDo.getMaxVersion()+1);
        }else {
            mdRulArticleBomHead.setVersion(mdRulArticleBomHeadDo.getVersion());
            mdRulArticleBomHead.setMaxVersion(mdRulArticleBomHeadDo.getMaxVersion());
        }
        mdRulArticleBomHead.setStartEffect(DateUtil.parseDate(mdRulArticleBomHeadDo.getStartEffect(),DateUtil.DATE_PATTERN));
        mdRulArticleBomHead.setEndEffect(DateUtil.parseDate(mdRulArticleBomHeadDo.getEndEffect(),DateUtil.DATE_PATTERN));
        mdRulArticleBomHead.setState(mdRulArticleBomHeadDo.getState());
        mdRulArticleBomHead.setProducer(mdRulArticleBomHeadDo.getProducer());
        return  mdRulArticleBomHead;

    }




}
