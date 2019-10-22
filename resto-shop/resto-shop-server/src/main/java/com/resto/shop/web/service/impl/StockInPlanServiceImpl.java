package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.dto.DocStkInPlanDetailDo;
import com.resto.shop.web.dto.DocStkInPlanHeaderDetailDo;
import com.resto.shop.web.dto.DocStkInPlanHeaderDo;
import com.resto.shop.web.enums.MaterialTypeStatus;
import com.resto.shop.web.enums.StkInPlanStatus;
import com.resto.shop.web.exception.ScmServiceException;
import com.resto.shop.web.manager.*;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.StockInPlanService;
import com.resto.shop.web.util.JSONUtil;
import com.resto.shop.web.util.ListUtil;
import com.resto.shop.web.util.keygenerate.KeyPrefix;
import com.resto.shop.web.util.keygenerate.UniqueCodeGenerator;
import com.resto.shop.web.util.log.LogBetter;
import com.resto.shop.web.util.log.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import static com.resto.shop.web.util.log.TraceLoggerFactory.logger;


/**
 * 入库管理
 */
@Component
@Service
public class StockInPlanServiceImpl implements StockInPlanService {

    @Autowired
    private DocStkInPlanDetailManager docStkInPlanDetailManager;

    @Autowired
    private DocStkInPlanHeaderManager docStkInPlanHeaderManager;

    @Autowired
    private DocMaterialStockManager docMaterialStockManager;

    @Autowired
    private DoOperateManager doOperateManager;

    @Resource
    private MdBillManager mdBillManager;

    /**
     * 入库
     * @param docStkInPlanHeaderDo
     * @return
     */
    @Override
    public Long addDocStkInPlanHeaderDo(DocStkInPlanHeaderDo docStkInPlanHeaderDo) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[SCM-新增入库单开始]")
                .addParm("入库单信息", docStkInPlanHeaderDo)
                .log();
        if (null == docStkInPlanHeaderDo || ListUtil.isNullOrEmpty(docStkInPlanHeaderDo.getDocStkInPlanDetailDoList())) {
            throw new ScmServiceException("SCM 新增入库单为空或明细不存在");
        }

        DocStkInPlanHeader stkInPlanHeader = generateDocStkInPlanHeaderDoForInsert(docStkInPlanHeaderDo);
        docStkInPlanHeaderManager.insert(stkInPlanHeader);
        for (DocStkInPlanDetailDo docStkInPlanDetailDo : docStkInPlanHeaderDo.getDocStkInPlanDetailDoList()) {
            Map<String, Object> fmParam = new HashMap<>();
            Map<String, Object> toParam = new HashMap<>();
            DoOperateLog doOperateLog;
            String operateReason;
            String operateType;
            //修改成根据原料id查找
            BaseQuery<DocMaterialStock> baseQuery = BaseQuery.getInstance(new DocMaterialStock());
            baseQuery.getData().setMaterialId(docStkInPlanDetailDo.getMaterialId());

            List<DocMaterialStock> docMaterialStocks = docMaterialStockManager.query(baseQuery);
            /**
             * 新增入库单的同时
             *     1.判断库存表是否存在该物料的库存列表，
             *           如果不存在
             *                      save 新增
             *            如果存在   update 可用库存
             */
            DocMaterialStock docMaterialStock = null;
            try {
                docStkInPlanDetailManager.insert(fillDocDoDetail(stkInPlanHeader, docStkInPlanDetailDo));

                if (ListUtil.isNullOrEmpty(docMaterialStocks) || docMaterialStocks.get(0) == null) {
                    //save 新增
                    toParam.put("actStockCount", docStkInPlanDetailDo.getActQty());
                    //docMaterialStockManager.insert(generateDocMaterialStock(stkInPlanHeader.getShopDetailId(),docStkInPlanDetailDo));
                    operateReason = "新增入库单,库存表不存在该原料id:" + docStkInPlanDetailDo.getMaterialId() + "时候,物料库存新增";
                    operateType = "MATERIAL_STOCK_INSERT";
                    doOperateLog = generateDoOperateLog(JSONUtil.toJson(fmParam), JSONUtil.toJson(toParam), stkInPlanHeader.getId(), stkInPlanHeader.getOrderCode(), operateType, operateReason, docStkInPlanDetailDo.getCreaterId(), docStkInPlanDetailDo.getCreaterName());
                } else {
                    docMaterialStock = docMaterialStocks.get(0);
                    //库存表实际剩余库存
                    BigDecimal actStockCount = docMaterialStock.getActStockCount();
                    //实际入库的库存
                    BigDecimal actQty = docStkInPlanDetailDo.getActQty();
                    BigDecimal actStock = actQty.add(actStockCount);
                    //docMaterialStock.setActStockCount(actStock);
                    docMaterialStock.setUpdaterId(docStkInPlanDetailDo.getUpdaterId());
                    docMaterialStock.setUpdaterName(docMaterialStock.getUpdaterName());
                    docMaterialStock.setGmtModified(new Date());

                    operateReason = "新增入库单,库存表已存在该原料id:" + docStkInPlanDetailDo.getMaterialId() + "时候,物料库存update";
                    operateType = "MATERIAL_STOCK_UPDATE";
                    fmParam.put("oldActStockCount", actStockCount);
                    toParam.put("newActStockCount", actStock);
                    doOperateLog = generateDoOperateLog(JSONUtil.toJson(fmParam), JSONUtil.toJson(toParam), stkInPlanHeader.getId(), stkInPlanHeader.getOrderCode(), operateType, operateReason, docStkInPlanDetailDo.getCreaterId(), docStkInPlanDetailDo.getCreaterName());
                    docMaterialStockManager.update(docMaterialStock);
                }
                doOperateManager.insert(doOperateLog);

            } catch (Exception e) {
                operateReason = "新增入库单,该原料id:" + docStkInPlanDetailDo.getMaterialId() + "时候,同步物料库存失败";
                operateType = "MATERIAL_STOCK_FAIL";

                if (docMaterialStock == null) {
                    //save 新增
                    toParam.put("actStockCount", docStkInPlanDetailDo.getActQty());
                } else {
                    //库存表实际剩余库存
                    BigDecimal actStockCount = docMaterialStock.getActStockCount();
                    //实际入库的库存
                    BigDecimal actQty = docStkInPlanDetailDo.getActQty();
                    BigDecimal actStock = actQty.add(actStockCount);
                    fmParam.put("oldActStockCount", actStockCount);
                    toParam.put("newActStockCount", actStock);
                }
//                doOperateLog = generateDoOperateLog(JSONUtil.toJson(fmParam), JSONUtil.toJson(toParam), stkInPlanHeader.getId(), stkInPlanHeader.getOrderCode(), operateType, operateReason, docStkInPlanDetailDo.getCreaterId(), docStkInPlanDetailDo.getCreaterName());
//                doOperateManager.insert(doOperateLog);
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[SCM-新增入库异常]")
                        .addParm("SCM 新增入库异信息", docStkInPlanHeaderDo)
                        .addParm("SCM 新增入库异信息fmParam", JSONUtil.toJson(fmParam))
                        .addParm("SCM 新增入库异信息toParam", JSONUtil.toJson(toParam))
                        .setException(e)
                        .log();
                throw new ScmServiceException("新增入库异信息" + e.getMessage());
            }

        }

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[SCM-新增入库单成功]")
                .log();
        return stkInPlanHeader.getId();

    }

    private DoOperateLog generateDoOperateLog(String fm, String tm, Long id, String stkInHeaderCode, String operateType, String operateReason, String operatorId, String operatorName) {
        DoOperateLog doOperateLog = new DoOperateLog();
        doOperateLog.setDoHeaderId(id);
        doOperateLog.setFmParam(fm);
        doOperateLog.setToParam(tm);
        doOperateLog.setOperateReason(operateReason);
        doOperateLog.setOperatorName(operatorName);
        doOperateLog.setOrderCode(stkInHeaderCode);
        doOperateLog.setOperateType(operateType);
        doOperateLog.setOperatorId(operatorId);
        return doOperateLog;
    }


    private DocMaterialStock generateDocMaterialStock(String shopDetailId, DocStkInPlanDetailDo docStkInPlanDetail) {
        DocMaterialStock docMaterialStock = new DocMaterialStock();
        docMaterialStock.setCreaterId(docStkInPlanDetail.getCreaterId());
        docMaterialStock.setCreaterName(docStkInPlanDetail.getCreaterName());
        docMaterialStock.setMaterialId(docStkInPlanDetail.getMaterialId());
        docMaterialStock.setActStockCount(docStkInPlanDetail.getActQty());
        docMaterialStock.setTheoryStockCount(docStkInPlanDetail.getActQty());
        docMaterialStock.setVersion(1);
        docMaterialStock.setMaterialStockCode(UniqueCodeGenerator.generate(KeyPrefix.STK.getPrefix(), "M"));
        docMaterialStock.setShopDetailId(shopDetailId);
        return docMaterialStock;
    }

    private DocMaterialStock generateDocMaterialStock_One(String shopDetailId, DocStkInPlanDetail docStkInPlanDetail) {
        DocMaterialStock docMaterialStock = new DocMaterialStock();
        docMaterialStock.setCreaterId(docStkInPlanDetail.getCreaterId());
        docMaterialStock.setCreaterName(docStkInPlanDetail.getCreaterName());
        docMaterialStock.setMaterialId(docStkInPlanDetail.getMaterialId());
        docMaterialStock.setActStockCount(docStkInPlanDetail.getActQty());
        docMaterialStock.setTheoryStockCount(docStkInPlanDetail.getActQty());
        docMaterialStock.setVersion(1);
        docMaterialStock.setMaterialStockCode(UniqueCodeGenerator.generate(KeyPrefix.STK.getPrefix(), "M"));
        docMaterialStock.setShopDetailId(shopDetailId);
        return docMaterialStock;
    }

    private DocStkInPlanDetail fillDocDoDetail(DocStkInPlanHeader stkInPlanHeader, DocStkInPlanDetailDo docStkInPlanDetailDo) {
        DocStkInPlanDetail  docStkInPlanDetail = new DocStkInPlanDetail();
        docStkInPlanDetail.setOrderDetailStatus(stkInPlanHeader.getOrderStatus());
        docStkInPlanDetail.setCreaterId(stkInPlanHeader.getCreaterId());
        docStkInPlanDetail.setCreaterName(stkInPlanHeader.getCreaterName());
        docStkInPlanDetail.setStkInHeaderCode(stkInPlanHeader.getOrderCode());
        docStkInPlanDetail.setStkInHeaderId(stkInPlanHeader.getId());
        docStkInPlanDetail.setActQty(docStkInPlanDetailDo.getActQty());
        docStkInPlanDetail.setMaterialId(docStkInPlanDetailDo.getMaterialId());
        docStkInPlanDetail.setSupPriceDetailId(docStkInPlanDetailDo.getSupPriceDetailId());
        docStkInPlanDetail.setUnitName(docStkInPlanDetailDo.getUnitName());
        docStkInPlanDetail.setNote(docStkInPlanDetailDo.getNote());
        docStkInPlanDetail.setPmsDetailId(docStkInPlanDetailDo.getPmsDetailId());
        docStkInPlanDetail.setPlanQty(docStkInPlanDetailDo.getPlanQty());
        docStkInPlanDetail.setPurchaseMoney(docStkInPlanDetailDo.getPurchaseMoney());
        return docStkInPlanDetail;
    }

    private DocStkInPlanHeader generateDocStkInPlanHeaderDoForInsert(DocStkInPlanHeaderDo docStkInPlanHeaderDo) {
        DocStkInPlanHeader docStkInPlanHeader = new DocStkInPlanHeader();
        docStkInPlanHeader.setGmtCreate(new Date());
        docStkInPlanHeader.setCreaterId(docStkInPlanHeaderDo.getCreaterId());
        docStkInPlanHeader.setCreaterName(docStkInPlanHeaderDo.getCreaterName());
        docStkInPlanHeader.setPublishedName(docStkInPlanHeaderDo.getCreaterName());
        docStkInPlanHeader.setPublishedTime(new Date());
        docStkInPlanHeader.setSupplierId(docStkInPlanHeaderDo.getSupplierId());
        docStkInPlanHeader.setOrderCode(UniqueCodeGenerator.generate(KeyPrefix.MPR.getPrefix(), "K"));
        docStkInPlanHeader.setSupPriceHeadId(docStkInPlanHeaderDo.getSupPriceHeadId());
        docStkInPlanHeader.setShopDetailId(docStkInPlanHeaderDo.getShopDetailId());
        docStkInPlanHeader.setPmsHeaderId(docStkInPlanHeaderDo.getPmsHeaderId());
        docStkInPlanHeader.setOrderStatus(StkInPlanStatus.INIT.getValue());
        docStkInPlanHeader.setOrderName(docStkInPlanHeaderDo.getOrderName());
        return docStkInPlanHeader;
    }

    @Override
    public List<DocStkInPlanHeaderDetailDo> queryJoin4Page(String shopDetailId) {

        List<DocStkInPlanHeaderDetailDo> docStkInPlanHeaderDos = docStkInPlanHeaderManager.queryJoin4Page(shopDetailId);
        Map<Long, List<DocStkInPlanHeaderDetailDo>> collects = docStkInPlanHeaderDos.stream().collect(Collectors.groupingBy(DocStkInPlanHeaderDetailDo::getId));
        List<DocStkInPlanHeaderDetailDo> returnDocStkInPlanHeaderDos = new ArrayList<>(docStkInPlanHeaderDos.size());

        for (Map.Entry<Long,List<DocStkInPlanHeaderDetailDo>> each:collects.entrySet()) {
            //组装头部
            List<DocStkInPlanHeaderDetailDo> values = each.getValue();
            DocStkInPlanHeaderDetailDo docStkInPlanHeaderDetailDo = values.get(0);
            docStkInPlanHeaderDetailDo.setOrderStatusShow(convertOrderStatus(docStkInPlanHeaderDetailDo.getOrderStatus()));
            //DocStkInPlanHeaderDo docStkInPlanHeaderDo = generateDocStkInPlanHeader(docStkInPlanHeaderDetailDo);
            //组装detail
            List<DocStkInPlanDetailDo> docStkInPlanDetailDos = new ArrayList<>(values.size());
            for (DocStkInPlanHeaderDetailDo docStkInPlanHeaderDetailEach:values) {
                docStkInPlanDetailDos.add(generateDocStkInPlanDetailDo(docStkInPlanHeaderDetailEach));
            }
            docStkInPlanHeaderDetailDo.setDocStkInPlanDetailDoList(docStkInPlanDetailDos);
            docStkInPlanHeaderDetailDo.setSize(docStkInPlanDetailDos.size());
            docStkInPlanHeaderDetailDo.setMaterialTypes(countMaterialTypes(docStkInPlanDetailDos));
            returnDocStkInPlanHeaderDos.add(docStkInPlanHeaderDetailDo);
        }

        return returnDocStkInPlanHeaderDos;

    }


    private String countMaterialTypes( List<DocStkInPlanDetailDo> docStkInPlanDetailDos) {
        Set<String> sets = new HashSet<>();
        for (DocStkInPlanDetailDo mdSupplierPriceDetail:docStkInPlanDetailDos) {
            Arrays.stream(MaterialTypeStatus.values()).forEach((MaterialTypeStatus item) ->{
                if(item.getValue().equals(mdSupplierPriceDetail.getMaterialType())) {
                    sets.add(item.getDescription());
                }
            });
        }
        return String.join(",", sets);
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


    private DocStkInPlanDetailDo generateDocStkInPlanDetailDo(DocStkInPlanHeaderDetailDo docStkInPlanHeaderDetailDo) {
        DocStkInPlanDetailDo docStkInPlanDetailDo = new DocStkInPlanDetailDo();
        docStkInPlanDetailDo.setActQty(docStkInPlanHeaderDetailDo.getActQty());
        docStkInPlanDetailDo.setPlanQty(docStkInPlanHeaderDetailDo.getPlanQty());
        docStkInPlanDetailDo.setCategoryOneName(docStkInPlanHeaderDetailDo.getCategoryOneName());
        docStkInPlanDetailDo.setCategoryTwoName(docStkInPlanHeaderDetailDo.getCategoryTwoName());
        docStkInPlanDetailDo.setCategoryThirdName(docStkInPlanHeaderDetailDo.getCategoryThirdName());
        docStkInPlanDetailDo.setCityName(docStkInPlanHeaderDetailDo.getCityName());
        docStkInPlanDetailDo.setProvinceName(docStkInPlanHeaderDetailDo.getProvinceName());
        docStkInPlanDetailDo.setDistrictName(docStkInPlanHeaderDetailDo.getDistrictName());
        docStkInPlanDetailDo.setMaterialCode(docStkInPlanHeaderDetailDo.getMaterialCode());
        docStkInPlanDetailDo.setMaterialType(docStkInPlanHeaderDetailDo.getMaterialType());
        docStkInPlanDetailDo.setMaterialName(docStkInPlanHeaderDetailDo.getMaterialName());
        docStkInPlanDetailDo.setMeasureUnit(docStkInPlanHeaderDetailDo.getMeasureUnit());
        docStkInPlanDetailDo.setNote(docStkInPlanHeaderDetailDo.getNote());
        docStkInPlanDetailDo.setSpecName(docStkInPlanHeaderDetailDo.getSpecName());
        docStkInPlanDetailDo.setTopContact(docStkInPlanHeaderDetailDo.getTopContact());
        docStkInPlanDetailDo.setTopEmail(docStkInPlanHeaderDetailDo.getTopEmail());
        docStkInPlanDetailDo.setTopMobile(docStkInPlanHeaderDetailDo.getTopMobile());
        docStkInPlanDetailDo.setPurchaseMoney(docStkInPlanHeaderDetailDo.getPurchaseMoney());
        return docStkInPlanDetailDo;
    }


//    private DocStkInPlanHeaderDo generateDocStkInPlanHeader(DocStkInPlanHeaderDetailDo docStkInPlanHeaderDetailDo) {
//        DocStkInPlanHeaderDo docStkInPlanHeaderDo = new DocStkInPlanHeaderDo();
//        docStkInPlanHeaderDo.setGmtCreate(docStkInPlanHeaderDetailDo.getGmtCreate());
//        docStkInPlanHeaderDo.setCreaterId(docStkInPlanHeaderDetailDo.getCreaterId());
//        docStkInPlanHeaderDo.setCreaterName(docStkInPlanHeaderDetailDo.getCreaterName());
//        docStkInPlanHeaderDo.setOrderCode(docStkInPlanHeaderDetailDo.getOrderCode());
//        docStkInPlanHeaderDo.setOrderStatus(docStkInPlanHeaderDetailDo.getOrderStatus());
//        return docStkInPlanHeaderDo;
//
//    }



    @Override
    public List<DocStkInPlanDetailDo> queryByStkInPlanHeaderId(Long id) {

        return null;
    }

    @Override
    public Long updateDocStkInPlanHeaderDo(DocStkInPlanHeaderDo docStkInPlanHeaderDo) {
        DocStkInPlanHeader docStkInPlanHeader = docStkInPlanHeaderManager.getById(docStkInPlanHeaderDo.getId());
        if(docStkInPlanHeader != null){
            docStkInPlanHeaderManager.update(convertDocStkInPlanHeader(docStkInPlanHeaderDo));
             //TODO 暂时未没有实现，如果使用请自己实现
            List<DocStkInPlanDetailDo> docStkInPlanDetailDoList = docStkInPlanHeaderDo.getDocStkInPlanDetailDoList();
        }
        return docStkInPlanHeaderDo.getId();
    }

    @Override
    public Long updateDocStkStatusById(Long id, String orderStatus,String auditName,String shopName) {

        DocStkInPlanHeader docStkInPlanHeader = docStkInPlanHeaderManager.queryJoinSupplier(id);
        if(StkInPlanStatus.PASSED.getValue().equals(orderStatus)){
            //入库的时候生成应付账单
            MdBill mdBill = new MdBill();
            mdBill.setBillNumber(UniqueCodeGenerator.generate(KeyPrefix.BILL.getPrefix(), "B"));
            mdBill.setShopDetailId(docStkInPlanHeader.getShopDetailId());
            mdBill.setStockPlanId(docStkInPlanHeader.getId());
            mdBill.setStockPlanName(docStkInPlanHeader.getOrderName());
            mdBill.setStockPlanNumber(docStkInPlanHeader.getOrderCode());
            mdBill.setSupplierId(docStkInPlanHeader.getSupplierId());
            mdBill.setGmtCreate(new Date());
            mdBill.setCreateName(auditName);
            mdBill.setState(1);
            mdBill.setSupplierTax(docStkInPlanHeader.getTaxNumber());
            mdBill.setShopDetailName(shopName);
            BaseQuery<DocStkInPlanDetail> queryDocStkInPlan= BaseQuery.getInstance(new DocStkInPlanDetail());
            queryDocStkInPlan.getData().setStkInHeaderId(docStkInPlanHeader.getId());
            List<DocStkInPlanDetail> docStkInPlanDetails = docStkInPlanDetailManager.query(queryDocStkInPlan);
            BigDecimal billAmount = BigDecimal.ZERO;
            for (DocStkInPlanDetail it:docStkInPlanDetails) {
                billAmount = billAmount.add(Optional.ofNullable(it.getActQty()).orElse(BigDecimal.ZERO).multiply(Optional.ofNullable(it.getPurchaseMoney()).orElse(BigDecimal.ZERO)));
            }
            mdBill.setBillAmount(billAmount);
            //先生成应付账单
            mdBillManager.insert(mdBill);

            //TODO 批准之后改变库存数量 xielc
            List<DocStkInPlanDetail> list = docStkInPlanDetailManager.selectMaterialId(id);
            for(DocStkInPlanDetail docStkInPlanDetailDo:list){
                BaseQuery<DocMaterialStock> baseQuery = BaseQuery.getInstance(new DocMaterialStock());
                baseQuery.getData().setMaterialId(docStkInPlanDetailDo.getMaterialId());
                List<DocMaterialStock> docMaterialStocks = docMaterialStockManager.query(baseQuery);
                if (ListUtil.isNullOrEmpty(docMaterialStocks)) {
                    //save 新增
                    docMaterialStockManager.insert(generateDocMaterialStock_One(docStkInPlanHeader.getShopDetailId(),docStkInPlanDetailDo));
                }else{
                    for(DocMaterialStock docMaterialStock:docMaterialStocks){
                        //库存表实际剩余库存
                        BigDecimal actStockCount = docMaterialStock.getActStockCount();
                        //实际入库的库存
                        BigDecimal actQty = docStkInPlanDetailDo.getActQty();
                        BigDecimal actStock = actQty.add(actStockCount);
                        docMaterialStock.setActStockCount(actStock);
                        docMaterialStockManager.update(docMaterialStock);
                    }
                }
            }
        }
        docStkInPlanHeaderManager.updateDocStkStatusById(id,orderStatus,auditName);
        return id;
    }



    private DocStkInPlanHeader convertDocStkInPlanHeader(DocStkInPlanHeaderDo docStkInPlanHeaderDo) {
        DocStkInPlanHeader docStkInPlanHeader = new DocStkInPlanHeader();
        docStkInPlanHeader.setGmtModified(new Date());
        docStkInPlanHeader.setSupplierId(docStkInPlanHeaderDo.getSupplierId());
        docStkInPlanHeader.setId(docStkInPlanHeaderDo.getId());
        docStkInPlanHeader.setSupPriceHeadId(docStkInPlanHeaderDo.getSupPriceHeadId());
        docStkInPlanHeader.setOrderStatus(docStkInPlanHeaderDo.getOrderStatus());
        return docStkInPlanHeader;
    }

    @Override
    public Long deleteById(Long id) {
        return null;
    }
}
