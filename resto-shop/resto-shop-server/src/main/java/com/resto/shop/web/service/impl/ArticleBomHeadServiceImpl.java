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
import com.resto.shop.web.service.ArticleBomHeadService;
import com.resto.shop.web.util.DateUtil;
import com.resto.shop.web.util.ListUtil;
import com.resto.shop.web.util.keygenerate.KeyPrefix;
import com.resto.shop.web.util.keygenerate.UniqueCodeGenerator;
import com.resto.shop.web.util.log.LogBetter;
import com.resto.shop.web.util.log.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;
import static com.resto.shop.web.util.log.TraceLoggerFactory .logger;


/**
 *
 * 菜品bom清单管理
 */
@Component
@Service
public class ArticleBomHeadServiceImpl implements ArticleBomHeadService {

    @Autowired
   private MdRulArticleBomDetailManager mdRulArticleBomDetailManager;


    @Autowired
    private MdRulArticleBomHeadManager mdRulArticleBomHeadManager;

    @Autowired
    private MdRulArticleBomHeadHistoryManager mdRulArticleBomHeadHistoryManager;

    @Autowired
    private MdRulArticleBomDetailHistoryManager mdRulArticleBomDetailHistoryManager;

    @Override
    public Long addArticleBomHead(MdRulArticleBomHeadDo mdRulArticleBomHeadDo) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[SCM-新增菜品BOM清单开始]")
                .addParm("菜品BOM清单信息", mdRulArticleBomHeadDo)
                .log();
        if (null == mdRulArticleBomHeadDo || ListUtil.isNullOrEmpty(mdRulArticleBomHeadDo.getBomDetailDoList())) {
            throw new ScmServiceException("SCM 新增BOM清单为空或明细不存在");
        }

        /**
         * 1. 根据articleId检查是否存在bom
         *        存在
         *             检查是否要保存备份（草稿）
         *                     是---存入当前表
         *                     否---存入当前表 把当前的记录存历史表 并将其从当前表中删除
         *        不存在   存入当前表
         */
        List<MdRulArticleBomHead> mdRulArticleBomHeads =mdRulArticleBomHeadManager.findBomHeadByState(mdRulArticleBomHeadDo.getShopDetailId(), mdRulArticleBomHeadDo.getArticleId(),null);
        Integer maxVersion = 0;
        if(ListUtil.isNotEmpty(mdRulArticleBomHeads)){
            if(Integer.valueOf(1).equals(mdRulArticleBomHeadDo.getState())){
                List<MdRulArticleBomHeadHistory> mdRulArticleHistoryBomHeads =mdRulArticleBomHeadHistoryManager.findHistoryBomHeadByState(mdRulArticleBomHeadDo.getShopDetailId(), mdRulArticleBomHeadDo.getArticleId(),null);
                //找出最大版本号
                maxVersion  = findMaxVersionMdRulArticleBomHead(mdRulArticleBomHeads,mdRulArticleHistoryBomHeads);
                checkBomAndSysnHistoryAndRemoveForAdd(mdRulArticleBomHeads,maxVersion);
            }else{
                MdRulArticleBomHead mdRulArticleBomHead = mdRulArticleBomHeads.stream().filter(it -> Integer.valueOf(0).equals(it.getState())).findFirst().orElse(null);
                if(mdRulArticleBomHead !=null){
                    LogBetter.instance(logger)
                            .setLevel(LogLevel.INFO)
                            .setMsg("[SCM-新增菜品草稿bom已存在,请前去编辑草稿bom]")
                            .addParm("当前存在草稿bom,忽略", mdRulArticleBomHead)
                            .log();

                    return 1L;
                }
            }
        }
        //存入当前表
        return saveCurentBom(mdRulArticleBomHeadDo,maxVersion);
    }

    private Integer findMaxVersionMdRulArticleBomHead(List<MdRulArticleBomHead> mdRulArticleBomHeads, List<MdRulArticleBomHeadHistory> mdRulArticleHistoryBomHeads) {
        Integer maxVersion =0;
        for (MdRulArticleBomHead it:mdRulArticleBomHeads) {
            if(maxVersion.compareTo(it.getVersion())<0){
                maxVersion =it.getVersion();
            }
        }

        for (MdRulArticleBomHeadHistory it:mdRulArticleHistoryBomHeads) {
            if(maxVersion.compareTo(it.getVersion())<0){
                maxVersion =it.getVersion();
            }
        }
        return maxVersion;
    }

    /**
     * @return
     */
    private void checkBomAndSysnHistoryAndRemoveForAdd(List<MdRulArticleBomHead> mdRulArticleBomHeads, Integer maxVersion) {
            //把当前启用状态的记录存入历史表 并将其从当前表中删除
           // MdRulArticleBomHead mdRulArticleBomHead = findEffectiveMdRulArticleBomHead(mdRulArticleBomHeads);
            MdRulArticleBomHead mdRulArticleBomHead = mdRulArticleBomHeads.get(0);
            if(mdRulArticleBomHead != null){
                MdRulArticleBomHeadHistory history = convertMdBomHeadHistory(mdRulArticleBomHead,maxVersion);
                mdRulArticleBomHeadHistoryManager.insert(history);
                List<MdRulArticleBomDetail> mdRulArticleBomDetails =mdRulArticleBomDetailManager.findEffectiveBomDetailByState(mdRulArticleBomHead.getId(),1);
                mdRulArticleBomDetails.forEach(it->{
                    mdRulArticleBomDetailHistoryManager.insert(convertMdBomDetailHistory(it,history.getId()));
                    mdRulArticleBomDetailManager.deleteById(it.getId());

                });
                    mdRulArticleBomHeadManager.deleteById(mdRulArticleBomHead.getId());

            }
    }




//
//    private MdRulArticleBomHead findEffectiveMdRulArticleBomHead(List<MdRulArticleBomHead> mdRulArticleBomHeads) {
//        List<MdRulArticleBomHead> collect = mdRulArticleBomHeads.stream().filter(it -> {
//            return (Integer.valueOf(1).equals(it.getState()));
//        }).collect(Collectors.toList());
//
//         if(ListUtil.isNotEmpty(collect)){
//            return collect.get(0);
//         }
//        return null;
//    }


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

    private MdRulArticleBomHeadHistory convertMdBomHeadHistory(MdRulArticleBomHead it,Integer maxVersion) {
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
        history.setMaxVersion(maxVersion);
        history.setBomCode(it.getBomCode());
        history.setStartEffect(it.getStartEffect());
        history.setEndEffect(it.getEndEffect());
        history.setState(0);
        history.setProducer(it.getProducer());
        history.setGmtCreate(new Date());
        history.setGmtModified(new Date());
        return history;
    }

    private Long saveCurentBom(MdRulArticleBomHeadDo mdRulArticleBomHeadDo, Integer maxVersion) {
        try {
            MdRulArticleBomHead mdRulArticleBomHead = generateArticleBomDoHeaderForInsert(mdRulArticleBomHeadDo,maxVersion);
            mdRulArticleBomHeadManager.insert(mdRulArticleBomHead);
            for (MdRulArticleBomDetailDo mdRulArticleBomDetailDo : mdRulArticleBomHeadDo.getBomDetailDoList()) {
                MdRulArticleBomDetail mdRulArticleBomDetail = generateOmsDocDoDetailForInsert(mdRulArticleBomHead, mdRulArticleBomDetailDo);
                mdRulArticleBomDetailManager.insert(mdRulArticleBomDetail);
            }
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[SCM-新增菜品BOM清单成功]")
                    .log();
            return mdRulArticleBomHead.getId();
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[SCM-菜品bom清单异常]")
                    .addParm("SCM bom清单信息", mdRulArticleBomHeadDo)
                    .setException(e)
                    .log();
            throw new ScmServiceException("菜品bom清单异常" + e.getMessage());
        }
    }

    @Override
    public List<MdRulArticleBomHeadDo> queryJoin4Page(String shopDetailId) {
        List<MdRulArticleBomHeadDtailDo> mdRulArticleBomHeadDtailDos = mdRulArticleBomHeadManager.queryJoin4Page(shopDetailId);
        Map<Long, List<MdRulArticleBomHeadDtailDo>> collect = mdRulArticleBomHeadDtailDos.stream().collect(Collectors.groupingBy(MdRulArticleBomHeadDtailDo::getId));
        List<MdRulArticleBomHeadDo> mdRulArticleBomHeadDos =new ArrayList<>(collect.size());
        for (Map.Entry entity:collect.entrySet() ) {
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

            mdRulArticleBomHeadDo.setStartEffect(DateUtil.formatDateStr(mdRulArticleBomHeadDtailDo.getStartEffect(),DateUtil.DATE_PATTERN));
            mdRulArticleBomHeadDo.setEndEffect(DateUtil.formatDateStr(mdRulArticleBomHeadDtailDo.getEndEffect(),DateUtil.DATE_PATTERN));
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
    public MdRulArticleBomHead queryById(Long id) {
        return mdRulArticleBomHeadManager.getById(id);
    }

    @Override
    public List<MdRulArticleBomDetail> queryByArticleBomHeadId(Long articleBomHeadId) {
        BaseQuery<MdRulArticleBomDetail> baseQuery =BaseQuery.getInstance(new MdRulArticleBomDetail());
        baseQuery.getData().setArticleBomHeadId(articleBomHeadId);
        return  mdRulArticleBomDetailManager.query(baseQuery);
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
        List<MdRulArticleBomHead> mdRulArticleBomHeads =mdRulArticleBomHeadManager.findBomHeadByState(mdRulArticleBomHeadDo.getShopDetailId(), mdRulArticleBomHeadDo.getArticleId(),null);
        boolean isUpdateVersionFlag =false;

        List<MdRulArticleBomHeadHistory> mdRulArticleHistoryBomHeads =mdRulArticleBomHeadHistoryManager.findHistoryBomHeadByState(mdRulArticleBomHeadDo.getShopDetailId(), mdRulArticleBomHeadDo.getArticleId(),null);
        //找出最大版本号
        int  maxVersion  = findMaxVersionMdRulArticleBomHead(mdRulArticleBomHeads,mdRulArticleHistoryBomHeads);

        if(isChanage(mdRulArticleBomHeads,mdRulArticleBomHeadDo,maxVersion)){
            isUpdateVersionFlag =true;
            checkBomAndSysnHistoryAndRemoveForUpdate(mdRulArticleBomHeads,mdRulArticleBomHeadDo.getState(),mdRulArticleBomHeadDo.getId(),maxVersion);
        }
        return updateBom(mdRulArticleBomHeadDo,isUpdateVersionFlag,maxVersion);

    }


    private boolean isChanage(List<MdRulArticleBomHead> mdRulArticleBomHeads , MdRulArticleBomHeadDo newRulArticleBomHeadDo, int maxVersion) {
        BaseQuery<MdRulArticleBomDetail> queryArticleBomDetail = BaseQuery.getInstance(new MdRulArticleBomDetail());
        queryArticleBomDetail.getData().setArticleBomHeadId(mdRulArticleBomHeads.get(0).getId());
        List<MdRulArticleBomDetail> oldRulArticleBomHeads = mdRulArticleBomDetailManager.query(queryArticleBomDetail);

        List<MdRulArticleBomDetailDo> newBomDetailDoList = newRulArticleBomHeadDo.getBomDetailDoList();
        Map<String, List<MdRulArticleBomDetailDo>> newBomDetailDoMap = newBomDetailDoList.stream().collect(Collectors.groupingBy(MdRulArticleBomDetailDo::getMaterialCode));
        //明细是否发生变化
        if (isChangeDetail(oldRulArticleBomHeads, newBomDetailDoMap)) {
            return true;
        }

        return false;
    }





    private boolean isChangeDetail(List<MdRulArticleBomDetail> oldRulArticleBomHeads, Map<String, List<MdRulArticleBomDetailDo>> newBomDetailDoMap) {
        //如果明细数量发生变化
        if(newBomDetailDoMap.size()!=oldRulArticleBomHeads.size()){
            return true;
        }
        Set<String> materialCodes = newBomDetailDoMap.keySet();
        //如果明细数量未发生变化，具体细节是否发生变化

        for (MdRulArticleBomDetail each :oldRulArticleBomHeads){
            if(!materialCodes.contains(each.getMaterialCode())){
                return true;
            }
        }
        //如果原料未发生变化，配方发生变化
        for (MdRulArticleBomDetail each :oldRulArticleBomHeads){
            MdRulArticleBomDetailDo mdRulArticleBomDetailDo = newBomDetailDoMap.get(each.getMaterialCode()).get(0);
            if(each.getMaterialCount().compareTo(mdRulArticleBomDetailDo.getMaterialCount())!=0){
                return true;
            }

        }
        return false;
    }



    private void checkBomAndSysnHistoryAndRemoveForUpdate(List<MdRulArticleBomHead> mdRulArticleBomHeads, Integer state, Long id, int maxVersion) {
        //把当前启用状态的记录存入历史表 并将其从当前表中删除
       // MdRulArticleBomHead mdRulArticleBomHead = findEffectiveMdRulArticleBomHead(mdRulArticleBomHeads);
        MdRulArticleBomHead mdRulArticleBomHead = mdRulArticleBomHeads.get(0);
        if(mdRulArticleBomHead != null){
            MdRulArticleBomHeadHistory history = convertMdBomHeadHistory(mdRulArticleBomHead,maxVersion);
            mdRulArticleBomHeadHistoryManager.insert(history);
            List<MdRulArticleBomDetail> mdRulArticleBomDetails =mdRulArticleBomDetailManager.findEffectiveBomDetailByState(mdRulArticleBomHead.getId(),null);
            mdRulArticleBomDetails.forEach(it->{
                mdRulArticleBomDetailHistoryManager.insert(convertMdBomDetailHistory(it,history.getId()));
                if(mdRulArticleBomHeads.size()>1 && !id.equals(mdRulArticleBomHead.getId())){
                    mdRulArticleBomDetailManager.deleteById(it.getId());
                }
            });
            if(mdRulArticleBomHeads.size()>1 && !id.equals(mdRulArticleBomHead.getId())){
                mdRulArticleBomHeadManager.deleteById(mdRulArticleBomHead.getId());
            }
        }
    }


    private Long updateBom(MdRulArticleBomHeadDo mdRulArticleBomHeadDo, boolean isUpdateVersionFlag, int maxVersion) {
        //更新头部
        BaseQuery<MdRulArticleBomHead> baseQuery= BaseQuery.getInstance(new MdRulArticleBomHead());
        baseQuery.getData().setId(mdRulArticleBomHeadDo.getId());
        UpdateByQuery<MdRulArticleBomHead> bomHeadQuery =new UpdateByQuery<>(baseQuery,generateArticleBomDoHeaderForUpdate(mdRulArticleBomHeadDo,isUpdateVersionFlag,maxVersion));
        mdRulArticleBomHeadManager.updateByQuery(bomHeadQuery);
        //更新明细  有移除的先删除 MdRulArticleBomDetail
        List<Long> bomDetailDeleteIds = mdRulArticleBomHeadDo.getBomDetailDeleteIds();

        if(ListUtil.isNotEmpty(bomDetailDeleteIds)){
            for (Long id:bomDetailDeleteIds) {
                mdRulArticleBomDetailManager.deleteById(id);
            }
        }
        //在修改
        List<MdRulArticleBomDetailDo> bomDetailDoList = mdRulArticleBomHeadDo.getBomDetailDoList();
        for (MdRulArticleBomDetailDo each: bomDetailDoList) {
            if(each.getId() != null) {
                BaseQuery<MdRulArticleBomDetail> baseQueryDetail = BaseQuery.getInstance(new MdRulArticleBomDetail());
                baseQueryDetail.getData().setArticleBomHeadId(mdRulArticleBomHeadDo.getId());
                baseQueryDetail.getData().setId(each.getId());
                UpdateByQuery<MdRulArticleBomDetail> updateBaseQueryDetail = new UpdateByQuery<>(baseQueryDetail, generateArticleBomDoDetailForUpdate(each,isUpdateVersionFlag,mdRulArticleBomHeadDo.getState()));
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


    private MdRulArticleBomDetail generateArticleBomDoDetailForUpdate(MdRulArticleBomDetailDo mdRulArticleBomDetailDo, boolean isUpdateVersionFlag,int state) {
        MdRulArticleBomDetail bomDetail = new MdRulArticleBomDetail();
        bomDetail.setMinMeasureUnit(mdRulArticleBomDetailDo.getMinMeasureUnit());
        bomDetail.setMaterialCount(mdRulArticleBomDetailDo.getMaterialCount());
        if(isUpdateVersionFlag){
            bomDetail.setVersion(mdRulArticleBomDetailDo.getVersion()+1);
        }else{
            bomDetail.setVersion(mdRulArticleBomDetailDo.getVersion());
        }
        bomDetail.setState(state);
        return bomDetail;
    }

    private MdRulArticleBomHead generateArticleBomDoHeaderForUpdate(MdRulArticleBomHeadDo mdRulArticleBomHeadDo, boolean isUpdateVersionFlag, int maxVersion) {
        MdRulArticleBomHead mdRulArticleBomHead =new MdRulArticleBomHead();
        mdRulArticleBomHead.setArticleId(mdRulArticleBomHeadDo.getArticleId());
        mdRulArticleBomHead.setCreaterId(mdRulArticleBomHeadDo.getCreaterId());
        mdRulArticleBomHead.setGmtModified(new Date());
        mdRulArticleBomHead.setArticleFamilyId(mdRulArticleBomHeadDo.getArticleFamilyId());
        mdRulArticleBomHead.setCreaterName(mdRulArticleBomHeadDo.getCreaterName());
        mdRulArticleBomHead.setMeasurementUnit(mdRulArticleBomHeadDo.getMeasurementUnit());
        mdRulArticleBomHead.setPriority(mdRulArticleBomHeadDo.getPriority());

        if(isUpdateVersionFlag){
            mdRulArticleBomHead.setVersion(maxVersion+1);
            mdRulArticleBomHead.setMaxVersion(maxVersion+1);
        }else{
            mdRulArticleBomHead.setVersion(mdRulArticleBomHeadDo.getVersion());
            mdRulArticleBomHead.setMaxVersion(mdRulArticleBomHeadDo.getMaxVersion());
        }
        mdRulArticleBomHead.setStartEffect(DateUtil.parseDate(mdRulArticleBomHeadDo.getStartEffect(),DateUtil.DATE_PATTERN));
        mdRulArticleBomHead.setEndEffect((DateUtil.parseDate(mdRulArticleBomHeadDo.getEndEffect(),DateUtil.DATE_PATTERN)));
        mdRulArticleBomHead.setState(mdRulArticleBomHeadDo.getState());
        mdRulArticleBomHead.setProducer(mdRulArticleBomHeadDo.getProducer());
        return  mdRulArticleBomHead;

    }

    private MdRulArticleBomDetail generateOmsDocDoDetailForInsert(MdRulArticleBomHead mdRulArticleBomHead, MdRulArticleBomDetailDo mdRulArticleBomDetailDo) {
        MdRulArticleBomDetail bomDetail = new MdRulArticleBomDetail();
        bomDetail.setLossFactor(mdRulArticleBomDetailDo.getLossFactor());
        bomDetail.setActLossFactor(mdRulArticleBomDetailDo.getActLossFactor());
        bomDetail.setArticleBomHeadId(mdRulArticleBomHead.getId());
        bomDetail.setCreaterId(mdRulArticleBomHead.getCreaterId());
        bomDetail.setCreaterName(mdRulArticleBomHead.getCreaterName());
        bomDetail.setMaterialCount(mdRulArticleBomDetailDo.getMaterialCount());
        bomDetail.setMaterialId(mdRulArticleBomDetailDo.getMaterialId());
        bomDetail.setMaterialCode(mdRulArticleBomDetailDo.getMaterialCode());
        bomDetail.setMaterialName(mdRulArticleBomDetailDo.getMaterialName());
        bomDetail.setMaterialType(mdRulArticleBomDetailDo.getMaterialType());
        bomDetail.setMinMeasureUnit(mdRulArticleBomDetailDo.getMinMeasureUnit());
        bomDetail.setSpecName(mdRulArticleBomDetailDo.getSpecName());
        bomDetail.setUnitName(mdRulArticleBomDetailDo.getUnitName());
        bomDetail.setVersion(mdRulArticleBomHead.getVersion());
        bomDetail.setStartEffect(mdRulArticleBomHead.getStartEffect());
        bomDetail.setEndEffect(mdRulArticleBomHead.getEndEffect());
        bomDetail.setState(mdRulArticleBomHead.getState());
        return bomDetail;
    }

    private MdRulArticleBomHead generateArticleBomDoHeaderForInsert(MdRulArticleBomHeadDo mdRulArticleBomHeadDo, Integer maxVersion) {
        MdRulArticleBomHead mdRulArticleBomHead =new MdRulArticleBomHead();
        mdRulArticleBomHead.setArticleId(mdRulArticleBomHeadDo.getArticleId());
        mdRulArticleBomHead.setCreaterId(mdRulArticleBomHeadDo.getCreaterId());
        mdRulArticleBomHead.setArticleFamilyId(mdRulArticleBomHeadDo.getArticleFamilyId());
        mdRulArticleBomHead.setCreaterName(mdRulArticleBomHeadDo.getCreaterName());
        mdRulArticleBomHead.setMeasurementUnit(mdRulArticleBomHeadDo.getMeasurementUnit());
        mdRulArticleBomHead.setPriority(mdRulArticleBomHeadDo.getPriority());
        mdRulArticleBomHead.setProductCategory(mdRulArticleBomHeadDo.getProductCategory());
        mdRulArticleBomHead.setProductCode(mdRulArticleBomHeadDo.getProductCode());
        mdRulArticleBomHead.setProductName(mdRulArticleBomHeadDo.getProductName());
        mdRulArticleBomHead.setShopDetailId(mdRulArticleBomHeadDo.getShopDetailId());
        mdRulArticleBomHead.setVersion(maxVersion+1);
        mdRulArticleBomHead.setMaxVersion(maxVersion+1);
        if(maxVersion.compareTo(0) >0 ||(maxVersion.compareTo(0) ==0 && mdRulArticleBomHeadDo.getState().equals(1)) ){
//        if(maxVersion.compareTo(0) >=0){
            mdRulArticleBomHead.setVersion(maxVersion+1);
            mdRulArticleBomHead.setMaxVersion(maxVersion+1);
        }else{
            mdRulArticleBomHead.setVersion(maxVersion);
            mdRulArticleBomHead.setMaxVersion(maxVersion);
        }
        mdRulArticleBomHead.setBomCode(UniqueCodeGenerator.generate(KeyPrefix.BOM.getPrefix(), "D"));

        mdRulArticleBomHead.setStartEffect(DateUtil.parseDate(mdRulArticleBomHeadDo.getStartEffect(),DateUtil.DATE_PATTERN));
        mdRulArticleBomHead.setEndEffect(DateUtil.parseDate(mdRulArticleBomHeadDo.getEndEffect(),DateUtil.DATE_PATTERN));

        mdRulArticleBomHead.setState(mdRulArticleBomHeadDo.getState());
        mdRulArticleBomHead.setProducer(mdRulArticleBomHeadDo.getProducer());
        return mdRulArticleBomHead;


    }



    @Override
    public Long deleteById(Long id) {
        //删除head
        int i = mdRulArticleBomHeadManager.deleteById(id);
        if(i>0){
            //删除detail
            MdRulArticleBomDetail md = new MdRulArticleBomDetail();
            md.setArticleBomHeadId(id);
            BaseQuery<MdRulArticleBomDetail> bomHead =BaseQuery.getInstance(md);
            try {
                mdRulArticleBomDetailManager.delete(bomHead);
            }catch (Exception e){
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[SCM-删除菜品bom清单异常]")
                        .addParm("SCM bom清单信息,异常headId:",id)
                        .setException(e)
                        .log();
            }

        }
        return id;

    }

    @Override
    public   List<MdRulArticleBomHead>  findBomHeadByState(String shopDetailId, String articleId,Integer state) {
        return mdRulArticleBomHeadManager.findBomHeadByState(shopDetailId, articleId, state);
    }


}
