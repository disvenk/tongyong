package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.datasource.DataSourceContextHolder;
import com.resto.shop.web.dto.*;
import com.resto.shop.web.enums.MaterialConsumeOptType;
import com.resto.shop.web.enums.MaterialTypeStatus;
import com.resto.shop.web.enums.StockCountStatus;
import com.resto.shop.web.exception.ScmServiceException;
import com.resto.shop.web.manager.*;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.ArticleSaleService;
import com.resto.shop.web.service.StockCountCheckService;
import com.resto.shop.web.util.DateUtil;
import com.resto.shop.web.util.ListUtil;
import com.resto.shop.web.util.keygenerate.KeyPrefix;
import com.resto.shop.web.util.keygenerate.UniqueCodeGenerator;
import com.resto.shop.web.util.log.LogBetter;
import com.resto.shop.web.util.log.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import static com.resto.shop.web.util.log.TraceLoggerFactory.logger;


/**
 * 库存盘点管理
 */

@Component
@Service
public class StockCountCheckServiceImpl implements StockCountCheckService {

    @Autowired
    private DocStockCountDetailManager docStockCountDetailManager;
    @Autowired
    private DocStockCountHeaderManager docStockCountHeaderManager;
    @Autowired
    private DocMaterialStockManager docMaterialStockManager;
    @Autowired
    private DocStkInPlanDetailManager docStkInPlanDetailManager;
    @Autowired
    private ArticleSaleService articleSaleService;

    @Autowired
    private MdMaterialConsumeDetailManager materialConsumeDetailManager;
    @Autowired
    private MdMaterialConsumeOptManager materialConsumeOptManager;
    @Autowired
    private MdRulArticleBomHeadManager mdRulArticleBomHeadManager;


    private String countMaterialTypes(List<DocStockCountDetailDo> list) {
        Set<String> sets = new HashSet<>();
        for (DocStockCountDetailDo docStockCountDetailDo : list) {
            Arrays.stream(MaterialTypeStatus.values()).forEach((MaterialTypeStatus item) -> {
                if (docStockCountDetailDo.getMaterialType() != null && docStockCountDetailDo.getMaterialType().equals(item.getValue())) {
                    sets.add(item.getDescription());
                }
            });
        }
        return String.join(",", sets);
    }

    @Override
    public List<DocStockCountHeaderDo> findStockList(String shopId) {
        List<DocStockCountHeaderDo> list = docStockCountHeaderManager.findStockList(shopId);
        for (DocStockCountHeaderDo item : list) {
            item.setSize(item.getStockCountDetailList().size());
            item.setMaterialTypes(countMaterialTypes(item.getStockCountDetailList()));
            item.setOrderStatusShow(convertOrderStatus(item.getOrderStatus()));
        }
        return list;
    }




    @Override
    public boolean saveStock(DocStockInput docStockInput) {

        if (null == docStockInput) {
            throw new ScmServiceException("SCM 新增盘点单为空");
        }
        if (docStockInput.getDocStockCountDetailDo().isEmpty()) {
            throw new ScmServiceException("SCM 新增盘点单明细为空");
        }
        //统计本次所有要参与盘点的原料id
        List<Long> materialStockCodeAlls = new ArrayList<>(docStockInput.getDocStockCountDetailDo().size());
        //统计本次参与盘点失败的原料id
        List<Long> materialStockCodeFails = new ArrayList<>(docStockInput.getDocStockCountDetailDo().size());


        //先保存盘点单
            DocStockCountHeader docStockCountHeader = new DocStockCountHeader();
            docStockCountHeader.setGmtCreate(new Date());
            docStockCountHeader.setCreaterId(docStockInput.getCreateId());
            docStockCountHeader.setCreaterName(docStockInput.getCreateName());
            String headerCode = UniqueCodeGenerator.generate(KeyPrefix.SCH.getPrefix(), "D");
            docStockCountHeader.setOrderCode(headerCode);
            docStockCountHeader.setOrderName(docStockInput.getOrderName());
           //docStockCountHeader.setStockCountStartTime(DateUtil.parseDate(docStockInput.getStartTime(), DateUtil.DEF_PATTERN));
           //docStockCountHeader.setStockCountEndTime(DateUtil.parseDate(docStockInput.getEndTime(), DateUtil.DEF_PATTERN));
            docStockCountHeader.setStockCountStartTime(new Date());
            docStockCountHeader.setStockCountEndTime(new Date());
            docStockCountHeader.setShopDetailId(docStockInput.getShopDetailId());
            docStockCountHeader.setPublishedTime(new Date());
            docStockCountHeader.setIsBlind(1);
            docStockCountHeader.setOrderStatus(StockCountStatus.INIT.getValue());
            docStockCountHeader.setUpdaterName(docStockInput.getCreateName());
            docStockCountHeaderManager.insert(docStockCountHeader);


        MdMaterialConsumeOpt mdMaterialConsumeOpt1 = materialConsumeOptManager.findMdMaterialConsumeLimitOne(docStockInput.getShopDetailId());
        //查询菜品统计
        String lastCountTime = DateUtil.formatDateStr(mdMaterialConsumeOpt1.getEndOrderTime(), DateUtil.DEF_PATTERN);
        List<ArticleSellCountDto> articleSellCountDtos = mdRulArticleBomHeadManager.findArticleByLastCountTime(docStockInput.getShopDetailId(), lastCountTime);
        Map<Long, List<ArticleBomDo>> longListMap = articleSaleService.countMaterialStockSellGroupByMaterialId(docStockInput.getShopDetailId(), lastCountTime, articleSellCountDtos);

        List<MaterialStockDo> materialStockDos = docMaterialStockManager.queryJoin4Page(docStockInput.getShopDetailId(), null, null);
        Map<Long, List<MaterialStockDo>> materialStockDoMap= new HashMap<>();
        if(ListUtil.isNotEmpty(materialStockDos)){
            materialStockDoMap = materialStockDos.stream().collect(Collectors.groupingBy(MaterialStockDo::getMaterialId));
        }
           Map<Long, List<DocStockCountDetailDo>> collect = docStockInput.getDocStockCountDetailDo().stream().collect(Collectors.groupingBy(DocStockCountDetailDo::getMaterialId));
            for (Map.Entry<Long,List<DocStockCountDetailDo>> each : collect.entrySet()) {

                DocStockCountDetailDo item = each.getValue().get(0);
                List<ArticleBomDo> articleBomDos = longListMap.get(each.getKey());
                materialStockCodeAlls.add(item.getMaterialId());
                DocStockCountDetail docStockCountDetail = new DocStockCountDetail();
                docStockCountDetail.setStockCountHeaderId(docStockCountHeader.getId());
                docStockCountDetail.setStockCountHeaderCode(headerCode);
                docStockCountDetail.setMaterialId(item.getMaterialId());
                docStockCountDetail.setDefferentReason(item.getDefferentReason());
                docStockCountDetail.setGmtCreate(new Date());
                docStockCountDetail.setCreaterId(docStockInput.getCreateId());
                docStockCountDetail.setCreaterName(docStockInput.getCreateName());
                List<MaterialStockDo> materialStockDos1 = materialStockDoMap.get(item.getMaterialId());
                if(ListUtil.isNotEmpty(materialStockDos1)){
                    docStockCountDetail.setActStockCount(item.getActStockCount());//盘点数量
                    docStockCountDetail.setAvailableStockCount(item.getActStockCount());//当前可用数量
                    BigDecimal actStockCount = materialStockDos1.get(0).getActStockCount()==null?BigDecimal.ZERO:materialStockDos1.get(0).getActStockCount();
                    docStockCountDetail.setTheoryStockCount(actStockCount.subtract(countLossStock(articleBomDos,docStockInput.getShopDetailId())));//理论剩余库存===原始库存-消耗
                    docStockCountDetail.setDefferentCount(item.getActStockCount().subtract(docStockCountDetail.getTheoryStockCount()));

                }else {
                    docStockCountDetail.setActStockCount(item.getActStockCount());//盘点数量
                    docStockCountDetail.setAvailableStockCount(item.getActStockCount());//当前可用数量
                    //docStockCountDetail.setTheoryStockCount(BigDecimal.ZERO.subtract(countLossStock(articleBomDos,docStockInput.getShopDetailId())));//理论剩余库存===原始库存-消耗
                    docStockCountDetail.setTheoryStockCount(item.getActStockCount());//理论剩余库存===原始库存-消耗==实际库存
                    docStockCountDetail.setDefferentCount(item.getActStockCount().subtract(docStockCountDetail.getTheoryStockCount()));
                }
                docStockCountDetailManager.insert(docStockCountDetail);
            }
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[SCM-新增盘点单成功]")
                .addParm("统计本次所有要参与盘点的原料materialStockCodeAlls.ids", materialStockCodeAlls)
                .addParm("统计本次参与盘点失败的原料materialStockCodeFails.ids", materialStockCodeFails)
                .log();
        return true;
    }

    //TODO 计算原料消耗,每天一个原料存在多条记录，每一条可以关联对应菜品产生的消耗
    private BigDecimal countLossStock(List<ArticleBomDo> articleBomDos,String shopId) {
        //计算原材料消耗并保存
        List<ArticleBomDo> newArticleBomDos = saveMaterialConsumeDetail(articleBomDos,shopId);
        BigDecimal materialCount = BigDecimal.ZERO;
        for (ArticleBomDo each : newArticleBomDos) {
            materialCount = materialCount.add(each.getMaterialConsume());
        }
        return materialCount;
    }

    private String convertOrderStatus(String orderStatus) {
        String orderStatusShow;
        switch(orderStatus){
            case "11":orderStatusShow="待审核";break;
            case "12":orderStatusShow="审核通过";break;
            case "13":orderStatusShow="已驳回";break;
            case "14":orderStatusShow="审核失败";break;
            case "15":orderStatusShow="已失效";break;
            default:orderStatusShow="";
        }
        return orderStatusShow;
    }


    private List<ArticleBomDo> saveMaterialConsumeDetail(List<ArticleBomDo> articleBomDos, String shopId) {
        //计算原材料消耗并保存
        if(ListUtil.isNullOrEmpty(articleBomDos)){
            return new ArrayList<>();
        }
        List<ArticleBomDo> newArticleBomDos = new ArrayList<>(articleBomDos.size());
        for (ArticleBomDo each : articleBomDos) {
            each.setMaterialConsume(countEachArticleLossStockByMaterialId(each));
            newArticleBomDos.add(each);
            materialConsumeDetailManager.insert(generateMaterialConsume(each,shopId));
        }
        return newArticleBomDos;

    }

    private MdMaterialConsumeDetail generateMaterialConsume(ArticleBomDo each, String shopId) {
        MdMaterialConsumeDetail materialConsumeDetail = new MdMaterialConsumeDetail();
        materialConsumeDetail.setActLossFactor(each.getActLossFactor());
        materialConsumeDetail.setArticleBomDetailId(each.getArticleBomHeadId());
        materialConsumeDetail.setArticleFamilyId(each.getArticleFamilyId());
        materialConsumeDetail.setArticleId(each.getArticleId());
        materialConsumeDetail.setArticleMealfeeNumber(each.getArticleMealFeeNumber());
        materialConsumeDetail.setArticleTotalCount(each.getArticleTotalCount());
        materialConsumeDetail.setCoefficient(each.getCoefficient());
        materialConsumeDetail.setGmtCreate(new Date());
        materialConsumeDetail.setIsDelete(0);
        materialConsumeDetail.setGmtModified(new Date());
        materialConsumeDetail.setMaterialConsume(each.getMaterialConsume());
        materialConsumeDetail.setRate(each.getRate());
        materialConsumeDetail.setMaterialId(each.getMaterialId());
        materialConsumeDetail.setMaterialCode(each.getMaterialCode());
        materialConsumeDetail.setMaterialName(each.getMaterialName());
        materialConsumeDetail.setMinMeasureUnit(each.getMinMeasureUnit());
        materialConsumeDetail.setMaterialCount(each.getMaterialCount());
        materialConsumeDetail.setSpecName(each.getSpecName());
        materialConsumeDetail.setState(1);
        materialConsumeDetail.setUnitName(each.getUnitName());
        materialConsumeDetail.setVersion(1);
        materialConsumeDetail.setShopId(shopId);
        return materialConsumeDetail;
    }

    //TODO 重点计算 转换最大规则
    private BigDecimal countEachArticleLossStockByMaterialId(ArticleBomDo each) {
        BigDecimal subtract = each.getMinMeasureUnit().multiply(each.getMaterialCount());
        BigDecimal divide = subtract.multiply(each.getArticleTotalCount()).divide(each.getRate(),4).divide(each.getCoefficient(),4);
        return divide;
    }


    @Override
    public List<MaterialStockDo> findDefaultStock(String shopId) {
        return docMaterialStockManager.queryJoin4Page(shopId, null, null);
    }

    @Override
    public Long approveStockStatusById(Long id, String status, String shopId,String auditName) {
        //记录操作时间
        MdMaterialConsumeOpt mdMaterialConsumeOpt = new MdMaterialConsumeOpt();
        mdMaterialConsumeOpt.setStartOptTime(new Date());
        mdMaterialConsumeOpt.setOptType(MaterialConsumeOptType.STOCK_COUNT.getValue());
        mdMaterialConsumeOpt.setShopId(shopId);
        if(StockCountStatus.PASSED.getValue().equals(status)){
            consumerMaterialCount(id, status, shopId, mdMaterialConsumeOpt,auditName);
            materialConsumeOptManager.insert(mdMaterialConsumeOpt);

            //TODO 批准之后改变库存数量 xielc
            List<DocStockCountDetail> list = docStockCountDetailManager.selectMaterialId(id);
            for(DocStockCountDetail docStkInPlanDetailDo:list){
                BaseQuery<DocMaterialStock> baseQuery = BaseQuery.getInstance(new DocMaterialStock());
                baseQuery.getData().setMaterialId(docStkInPlanDetailDo.getMaterialId());
                List<DocMaterialStock> docMaterialStocks = docMaterialStockManager.query(baseQuery);
                /*if(docMaterialStocks!=null){
                    for(DocMaterialStock docMaterialStock:docMaterialStocks){
                        //盘点之后实际入库的库存
                        docMaterialStock.setActStockCount(docStkInPlanDetailDo.getActStockCount());
                        docMaterialStockManager.update(docMaterialStock);
                    }
                }*/
                if (ListUtil.isNullOrEmpty(docMaterialStocks)) {
                    //save 新增
                    docMaterialStockManager.insert(generateDocMaterialStock_One(shopId,docStkInPlanDetailDo));
                }else{
                    for(DocMaterialStock docMaterialStock:docMaterialStocks){
                        //盘点之后实际入库的库存
                        docMaterialStock.setActStockCount(docStkInPlanDetailDo.getActStockCount());
                        docMaterialStockManager.update(docMaterialStock);
                    }
                }
            }
        }else{
            //盘点的时候根据盘点明细号，更新盘点单明细
            BaseQuery<DocStockCountHeader> baseQuery = BaseQuery.getInstance(new DocStockCountHeader());
            baseQuery.getData().setId(id);
            DocStockCountHeader docStockCountHeader = new DocStockCountHeader();
            docStockCountHeader.setOrderStatus(status);
            //审核人
            docStockCountHeader.setUpdaterName(auditName);
            UpdateByQuery<DocStockCountHeader> bomHeadQuery = new UpdateByQuery<>(baseQuery, docStockCountHeader);
            //修改主表
            docStockCountHeaderManager.updateByQuery(bomHeadQuery);

            //修改明细表  ---根据订单头id 批量修改明细状态
            BaseQuery<DocStockCountDetail> detailBaseQuery = BaseQuery.getInstance(new DocStockCountDetail());
            detailBaseQuery.getData().setStockCountHeaderId(id);
            DocStockCountDetail stockCountDetail = new DocStockCountDetail();
            stockCountDetail.setLineStatus(status);
            UpdateByQuery<DocStockCountDetail> stockCountDetailBaseQuery = new UpdateByQuery<>(detailBaseQuery, stockCountDetail);
            docStockCountDetailManager.updateByQuery(stockCountDetailBaseQuery);
        }


        return id;

    }

    private DocMaterialStock generateDocMaterialStock_One(String shopDetailId, DocStockCountDetail docStockCountDetail) {
        DocMaterialStock docMaterialStock = new DocMaterialStock();
        docMaterialStock.setCreaterId(docStockCountDetail.getCreaterId());
        docMaterialStock.setCreaterName(docStockCountDetail.getCreaterName());
        docMaterialStock.setMaterialId(docStockCountDetail.getMaterialId());
        docMaterialStock.setActStockCount(docStockCountDetail.getActStockCount());
        docMaterialStock.setTheoryStockCount(docStockCountDetail.getActStockCount());
        docMaterialStock.setVersion(1);
        docMaterialStock.setMaterialStockCode(UniqueCodeGenerator.generate(KeyPrefix.STK.getPrefix(), "M"));
        docMaterialStock.setShopDetailId(shopDetailId);
        return docMaterialStock;
    }

    private MdMaterialConsumeOpt consumerMaterialCount(Long id, String status, String shopId, MdMaterialConsumeOpt mdMaterialConsumeOpt,String auditName) {
            //盘点的时候根据盘点明细号，更新盘点单明细
            BaseQuery<DocStockCountHeader> baseQuery = BaseQuery.getInstance(new DocStockCountHeader());
            baseQuery.getData().setId(id);
            DocStockCountHeader docStockCountHeader = new DocStockCountHeader();
            docStockCountHeader.setOrderStatus(status);
            //审核人
            docStockCountHeader.setUpdaterName(auditName);
            UpdateByQuery<DocStockCountHeader> bomHeadQuery = new UpdateByQuery<>(baseQuery, docStockCountHeader);
            //修改主表
            docStockCountHeaderManager.updateByQuery(bomHeadQuery);

            //修改明细表  ---根据订单头id 批量修改明细状态
            BaseQuery<DocStockCountDetail> detailBaseQuery = BaseQuery.getInstance(new DocStockCountDetail());
            detailBaseQuery.getData().setStockCountHeaderId(id);


            DocStockCountDetail stockCountDetail = new DocStockCountDetail();
            stockCountDetail.setLineStatus(status);

            MdMaterialConsumeOpt mdMaterialConsumeOpt1 = materialConsumeOptManager.findMdMaterialConsumeLimitOne(shopId);
            Date endOrderTime = mdMaterialConsumeOpt1.getEndOrderTime();
            String lastCountTime = DateUtil.formatDateStr(endOrderTime, DateUtil.DEF_PATTERN);
            //计算理论库存和实际库存
            DocStockCountHeaderDo docStockCountHeaderDo = docStockCountHeaderManager.findStockListById(shopId, id);

            //查询菜品统计
            List<ArticleSellCountDto> articleSellCountDtos = mdRulArticleBomHeadManager.findArticleByLastCountTime(shopId, lastCountTime);
            if (docStockCountHeaderDo == null || StockCountStatus.PASSED.getValue().equals(status)) {
                List<DocStockCountDetailDo> stockCountDetailList = docStockCountHeaderDo.getStockCountDetailList();
                if (ListUtil.isNullOrEmpty(stockCountDetailList)) {
                    //根据 stockCountHeaderId 更新
                    mdMaterialConsumeOpt.setStartOrderTime(endOrderTime);
                    mdMaterialConsumeOpt.setEndOrderTime(endOrderTime);
                    mdMaterialConsumeOpt.setNote("手动盘点-无盘点明细");
                    UpdateByQuery<DocStockCountDetail> stockCountDetailBaseQuery = new UpdateByQuery<>(detailBaseQuery, stockCountDetail);
                    docStockCountDetailManager.updateByQuery(stockCountDetailBaseQuery);
                } else {
                    Map<Long, List<ArticleBomDo>> longListMap = articleSaleService.countMaterialStockSellGroupByMaterialId(shopId, lastCountTime, articleSellCountDtos);
                    pandianComsumerMaterialCount(detailBaseQuery,status ,stockCountDetailList, longListMap,shopId);
                    if(ListUtil.isNotEmpty(articleSellCountDtos)){
                        String createTime = articleSellCountDtos.get(0).getCreateTime();
                        mdMaterialConsumeOpt.setEndOrderTime(DateUtil.parseDate(createTime, DateUtil.DEF_PATTERN));
                        String createTime1 = articleSellCountDtos.get(articleSellCountDtos.size() - 1).getCreateTime();
                        mdMaterialConsumeOpt.setStartOrderTime(DateUtil.parseDate(createTime1, DateUtil.DEF_PATTERN));
                        mdMaterialConsumeOpt.setNote("手动盘点-有菜品消耗");
                    }else{
                        mdMaterialConsumeOpt.setStartOrderTime(mdMaterialConsumeOpt1.getStartOptTime());
                        mdMaterialConsumeOpt.setEndOrderTime(mdMaterialConsumeOpt1.getEndOrderTime());
                        logger.warn("无菜品消耗====");
                        mdMaterialConsumeOpt.setNote("手动盘点-无菜品消耗");
                    }
                }
            } else {
                mdMaterialConsumeOpt.setEndOrderTime(endOrderTime);
                mdMaterialConsumeOpt.setStartOrderTime(endOrderTime);
                mdMaterialConsumeOpt.setNote("手动盘点-驳回,菜品消耗不进行操作");
                //根据 stockCountHeaderId 更新
                UpdateByQuery<DocStockCountDetail> stockCountDetailBaseQuery = new UpdateByQuery<>(detailBaseQuery, stockCountDetail);
                docStockCountDetailManager.updateByQuery(stockCountDetailBaseQuery);
            }
        mdMaterialConsumeOpt.setEndOptTime(new Date());
        return mdMaterialConsumeOpt;

    }






    private void pandianComsumerMaterialCount(BaseQuery<DocStockCountDetail> detailBaseQuery, String status, List<DocStockCountDetailDo> stockCountDetailList, Map<Long, List<ArticleBomDo>> longListMap, String shopId) {
        DocStockCountDetail stockCountDetail = new DocStockCountDetail();
        stockCountDetail.setLineStatus(status);

        List<MaterialStockDo> materialStockDos = docMaterialStockManager.queryJoin4Page(shopId, null, null);
        Map<Long, List<MaterialStockDo>> materialStockDoMap= new HashMap<>();
        if(ListUtil.isNotEmpty(materialStockDos)){
            materialStockDoMap = materialStockDos.stream().collect(Collectors.groupingBy(MaterialStockDo::getMaterialId));

        }
        Map<Long, List<DocStockCountDetailDo>> collect = stockCountDetailList.stream().collect(Collectors.groupingBy(DocStockCountDetailDo::getMaterialId));
        for (Map.Entry<Long,List<ArticleBomDo>> each : longListMap.entrySet()) {
            List<DocStockCountDetailDo> docStockCountDetailDos = collect.get(each.getKey());
            if(docStockCountDetailDos!=null){
                DocStockCountDetailDo item = docStockCountDetailDos.get(0);
                List<MaterialStockDo> materialStockDos1 = materialStockDoMap.get(each.getKey());
                docMaterialStockManager.updateMaterStock(item, shopId);
                if(ListUtil.isNotEmpty(materialStockDos1)){
                    MaterialStockDo materialStockDo = materialStockDos1.get(0);
                    if(materialStockDo !=null){
                        stockCountDetail.setActStockCount(item.getActStockCount());//盘点数量
                        stockCountDetail.setAvailableStockCount(item.getActStockCount());//当前可用数量
                        stockCountDetail.setTheoryStockCount(materialStockDo.getActStockCount().subtract(countLossStock(each.getValue(),shopId)));//理论剩余库存===原始库存-消耗
                        stockCountDetail.setDefferentCount(item.getActStockCount().subtract(stockCountDetail.getTheoryStockCount()));
                    }

                }else {
                    stockCountDetail.setActStockCount(item.getActStockCount());//盘点数量
                    stockCountDetail.setAvailableStockCount(item.getActStockCount());//当前可用数量
                    stockCountDetail.setTheoryStockCount(BigDecimal.ZERO.subtract(countLossStock(each.getValue(),shopId)));//理论剩余库存===原始库存-消耗
                    stockCountDetail.setDefferentCount(item.getActStockCount().subtract(stockCountDetail.getTheoryStockCount()));
                }

                detailBaseQuery.getData().setId(item.getId());
                UpdateByQuery<DocStockCountDetail> stockCountDetailBaseQuery = new UpdateByQuery<>(detailBaseQuery, stockCountDetail);

                docStockCountDetailManager.updateByQuery(stockCountDetailBaseQuery);
            }
        }

    }

    @Override
    public void scheduledTaskComsumerMaterialCount(String  brandId,String shopId) {
        DataSourceContextHolder.setDataSourceName(brandId);
        MdMaterialConsumeOpt mdMaterialConsumeOpt = new MdMaterialConsumeOpt();
        mdMaterialConsumeOpt.setStartOptTime(new Date());
        mdMaterialConsumeOpt.setOptType(MaterialConsumeOptType.SHEDULER.getValue());
        mdMaterialConsumeOpt.setShopId(shopId);

        MdMaterialConsumeOpt mdMaterialConsumeOpt1 = materialConsumeOptManager.findMdMaterialConsumeLimitOne(shopId);
        if(mdMaterialConsumeOpt1!=null){
            if(mdMaterialConsumeOpt1.getEndOrderTime()!=null){
                Date endOrderTime = mdMaterialConsumeOpt1.getEndOrderTime();

                String lastCountTime = DateUtil.formatDateStr(endOrderTime, DateUtil.DEF_PATTERN);
                //查询菜品统计
                List<ArticleSellCountDto> articleSellCountDtos = mdRulArticleBomHeadManager.findArticleByLastCountTime(shopId, lastCountTime);

                Map<Long, List<ArticleBomDo>> longListMap = articleSaleService.countMaterialStockSellGroupByMaterialId(shopId, lastCountTime, articleSellCountDtos);

                List<MaterialStockDo> materialStockDos = docMaterialStockManager.queryJoin4Page(shopId, null, null);
                Map<Long, List<MaterialStockDo>> materialStockDoMap= new HashMap<>();
                if(ListUtil.isNotEmpty(materialStockDos)){
                    materialStockDoMap = materialStockDos.stream().collect(Collectors.groupingBy(MaterialStockDo::getMaterialId));
                }
                //原料消耗明细 TODO
                for (Map.Entry<Long,List<ArticleBomDo>> each : longListMap.entrySet()) {
                    DocStockCountDetailDo docStockCountDetailDo = new DocStockCountDetailDo();
                    List<MaterialStockDo> materialStockDos1 = materialStockDoMap.get(each.getKey());
                    try {
                        if(materialStockDos1!=null){
                            MaterialStockDo materialStockDo = materialStockDos1.get(0);
                            BigDecimal countLossStock = countLossStock(each.getValue(), shopId);
                            if(materialStockDo !=null){
                                BigDecimal actStockCount = materialStockDo.getActStockCount()==null?BigDecimal.ZERO:materialStockDos1.get(0).getActStockCount();
                                //TODO 计算价格 实际库存定时任务更新不了,只有手动盘点时候才可以更新
                                //docStockCountDetailDo.setActStockCount(actStockCount.subtract(countLossStock));
                                docStockCountDetailDo.setTheoryStockCount(actStockCount.subtract(countLossStock));
                                docStockCountDetailDo.setMaterialId(each.getKey());
                                docMaterialStockManager.updateMaterStock(docStockCountDetailDo, shopId);
                            }
                        }else{
                            logger.info("原料库存没有初始化,会引起原料统计误差,请先入库原材料,才可以保证原料统计正常shopId:"+shopId+",materialId:"+each.getKey());
                        }
                    }catch (Exception e){
                        throw new IllegalArgumentException(e);
                    }
                }

                if(ListUtil.isNotEmpty(articleSellCountDtos)){
                    String createTime = articleSellCountDtos.get(0).getCreateTime();
                    mdMaterialConsumeOpt.setEndOrderTime(DateUtil.parseDate(createTime, DateUtil.DEF_PATTERN));
                    String createTime1 = articleSellCountDtos.get(articleSellCountDtos.size() - 1).getCreateTime();
                    mdMaterialConsumeOpt.setStartOrderTime(DateUtil.parseDate(createTime1, DateUtil.DEF_PATTERN));
                    mdMaterialConsumeOpt.setEndOptTime(new Date());
                    mdMaterialConsumeOpt.setNote("定时任务-有菜品消耗");
                    materialConsumeOptManager.insert(mdMaterialConsumeOpt);
                }else{
                    logger.info("无菜品消耗");
                    mdMaterialConsumeOpt1.setId(null);
                    mdMaterialConsumeOpt1.setStartOptTime(mdMaterialConsumeOpt1.getStartOrderTime());
                    mdMaterialConsumeOpt1.setEndOptTime(mdMaterialConsumeOpt1.getEndOrderTime());
                    mdMaterialConsumeOpt1.setNote("定时任务-无菜品消耗");
                    materialConsumeOptManager.insert(mdMaterialConsumeOpt1);

                }
            }
        }
    }

}


