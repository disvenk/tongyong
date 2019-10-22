package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.dto.MdSupplierPriceDetailDo;
import com.resto.shop.web.dto.MdSupplierPriceHeadDo;
import com.resto.shop.web.enums.MaterialTypeStatus;
import com.resto.shop.web.enums.SupplierPriceStatus;
import com.resto.shop.web.exception.ScmServiceException;
import com.resto.shop.web.manager.SupplierMaterialPriceDetailManager;
import com.resto.shop.web.manager.SupplierMaterialPriceHeadManager;
import com.resto.shop.web.model.MdCategory;
import com.resto.shop.web.model.MdSupplierPriceDetail;
import com.resto.shop.web.model.MdSupplierPriceHead;
import com.resto.shop.web.service.SupplierMaterialPriceService;
import com.resto.shop.web.util.DateUtil;
import com.resto.shop.web.util.ListUtil;
import com.resto.shop.web.util.keygenerate.KeyPrefix;
import com.resto.shop.web.util.keygenerate.UniqueCodeGenerator;
import com.resto.shop.web.util.log.LogBetter;
import com.resto.shop.web.util.log.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import static com.resto.shop.web.util.log.TraceLoggerFactory.logger;


@Component
@Service
public class SupplierMaterialPriceServiceImpl implements SupplierMaterialPriceService {


    @Autowired
    private SupplierMaterialPriceHeadManager supplierMaterialPriceHeadManager;

    @Autowired
    private SupplierMaterialPriceDetailManager supplierMaterialPriceDetailManager;



    @Override
    public Long addMdSupplierPrice(MdSupplierPriceHeadDo mdSupplierPriceHeadDo) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[SCM-新增原料报价单开始]")
                .addParm("原料报价单信息", mdSupplierPriceHeadDo)
                .log();
        if (null == mdSupplierPriceHeadDo || ListUtil.isNullOrEmpty(mdSupplierPriceHeadDo.getMdSupplierPriceDetailDoList())) {
            throw new ScmServiceException("SCM 新增原料报价为空或明细不存在");
        }
        try {
            MdSupplierPriceHead mdSupplierPriceHead = generateMdSupplierPriceHeadForInsert(mdSupplierPriceHeadDo);
            supplierMaterialPriceHeadManager.insert(mdSupplierPriceHead);
            for (MdSupplierPriceDetail mdSupplierPriceDetail : mdSupplierPriceHeadDo.getMdSupplierPriceDetailDoList()) {
                supplierMaterialPriceDetailManager.insert(fillMdSupplierPriceDetail(mdSupplierPriceHead, mdSupplierPriceDetail));
            }
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[SCM-新增原料报价成功]")
                    .log();
            return mdSupplierPriceHead.getId();
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[SCM-新增原料报价单异常]")
                    .addParm("SCM 新增原料报价信息", mdSupplierPriceHeadDo)
                    .setException(e)
                    .log();
            throw new ScmServiceException("新增原料报价异常" + e.getMessage());
        }
    }

    private MdSupplierPriceDetail  fillMdSupplierPriceDetail(MdSupplierPriceHead mdSupplierPriceHead, MdSupplierPriceDetail mdSupplierPriceDetail) {
        mdSupplierPriceDetail.setSupPriceId(mdSupplierPriceHead.getId());
        mdSupplierPriceDetail.setSupPriceNo(mdSupplierPriceHead.getPriceNo());
        mdSupplierPriceDetail.setCreaterId(mdSupplierPriceHead.getCreaterId());
        mdSupplierPriceDetail.setCreaterName(mdSupplierPriceHead.getCreaterName());
        mdSupplierPriceDetail.setSupDetailStatus(mdSupplierPriceHead.getSupStatus());
        mdSupplierPriceDetail.setShopDetailId(mdSupplierPriceHead.getShopDetailId());
        mdSupplierPriceDetail.setSupplierId(mdSupplierPriceHead.getSupplierId());
        mdSupplierPriceDetail.setVersion(1);
        return mdSupplierPriceDetail;
    }



    private MdSupplierPriceHead generateMdSupplierPriceHeadForInsert(MdSupplierPriceHeadDo mdSupplierPriceHeadDo) {
        MdSupplierPriceHead mdSupplierPriceHead =new MdSupplierPriceHead();
        mdSupplierPriceHead.setCreaterId(mdSupplierPriceHeadDo.getCreaterId());
        mdSupplierPriceHead.setCreaterName(mdSupplierPriceHeadDo.getCreaterName());
        mdSupplierPriceHead.setEndEffect(DateUtil.parseDate(mdSupplierPriceHeadDo.getEndEffect(),"yyyy-MM-dd"));
        mdSupplierPriceHead.setMaterialTypes(mdSupplierPriceHeadDo.getMaterialTypes());
        mdSupplierPriceHead.setPriceNo(UniqueCodeGenerator.generate(KeyPrefix.SPL.getPrefix(), "D"));
        mdSupplierPriceHead.setShopDetailId(mdSupplierPriceHeadDo.getShopDetailId());
        mdSupplierPriceHead.setSupStatus(SupplierPriceStatus.INIT.getValue());
        mdSupplierPriceHead.setStartEffect(DateUtil.parseDate(mdSupplierPriceHeadDo.getStartEffect(),"yyyy-MM-dd"));
        mdSupplierPriceHead.setVersion(1);
        mdSupplierPriceHead.setIsEffect(1);//默认生效
        mdSupplierPriceHead.setSupplierId(mdSupplierPriceHeadDo.getSupplierId());
        mdSupplierPriceHead.setContactId(mdSupplierPriceHeadDo.getContactId());
        mdSupplierPriceHead.setPriceName(mdSupplierPriceHeadDo.getPriceName());
        mdSupplierPriceHead.setTax(mdSupplierPriceHeadDo.getTax());
        return mdSupplierPriceHead;
    }

    @Override
    public List<MdSupplierPriceHeadDo> queryJoin4Page(String shopDetailId) {
        List<MdSupplierPriceHeadDo> mdSupplierPriceHeads =supplierMaterialPriceHeadManager.queryJoin4Page(shopDetailId);

        List<MdSupplierPriceHeadDo> returnList = new ArrayList<>(mdSupplierPriceHeads.size());

        for (MdSupplierPriceHeadDo mdSupplierPriceHeadDo:mdSupplierPriceHeads) {
            //根据报价单号 当前版本
            List<MdSupplierPriceDetail> query = supplierMaterialPriceDetailManager.queryJoinMaterialView(mdSupplierPriceHeadDo.getId(),mdSupplierPriceHeadDo.getVersion());
            mdSupplierPriceHeadDo.setMaterialSizes(query.size());
            mdSupplierPriceHeadDo.setMaterialTypes(countMaterialTypes(query));
            mdSupplierPriceHeadDo.setMdSupplierPriceDetailDoList(query);
            mdSupplierPriceHeadDo.setSupStatus(countSupStatus(SupplierPriceStatus.values(),mdSupplierPriceHeadDo.getSupStatus()),mdSupplierPriceHeadDo.getSupStatus());
            returnList.add(mdSupplierPriceHeadDo);
        }
        return returnList;
    }

    private String countSupStatus(SupplierPriceStatus[] values,String supStatus) {
        for (SupplierPriceStatus value:values) {
            if(value.getValue().equals(supStatus)){
                return value.getDescription();
            }
        }
        return null;
    }

    private String countMaterialTypes(List<MdSupplierPriceDetail> mdSupplierPriceDetails) {
        Set<String> sets = new HashSet<>();
        for (MdSupplierPriceDetail mdSupplierPriceDetail:mdSupplierPriceDetails) {
            Arrays.stream(MaterialTypeStatus.values()).forEach((MaterialTypeStatus item) ->{
                    if(item.getValue().equals(mdSupplierPriceDetail.getMaterialType())) {
                        sets.add(item.getDescription());
                    }
                });
        }
        return String.join(",", sets);
    }



    @Override
    public Long updateMdSupplierPrice(MdSupplierPriceHeadDo mdSupplierPriceHeadDo) {
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[SCM-修改供应商报价单开始]")
                .addParm("供应商报价单信息", mdSupplierPriceHeadDo)
                .log();
        if (null == mdSupplierPriceHeadDo || mdSupplierPriceHeadDo.getId() == null || ListUtil.isNullOrEmpty(mdSupplierPriceHeadDo.getMdSupplierPriceDetailDoList())) {
            throw new ScmServiceException("SCM 供应商报价单为空或者headerId 为空或明细不存在");
        }
        //更新头部--需要调整的逻辑  修改即是
        BaseQuery<MdSupplierPriceHead> baseQuery= BaseQuery.getInstance(new MdSupplierPriceHead());
        baseQuery.getData().setId(mdSupplierPriceHeadDo.getId());
        UpdateByQuery<MdSupplierPriceHead> bomHeadQuery =new UpdateByQuery<>(baseQuery,generateMdSupplierPriceHeadForUpdate(mdSupplierPriceHeadDo));
        supplierMaterialPriceHeadManager.updateByQuery(bomHeadQuery);

        //更新明细  有移除的先删除
        List<Long> priceDetailDeleteIds = mdSupplierPriceHeadDo.getSupplierPriceDetailDeleteIds();

        if(ListUtil.isNotEmpty(priceDetailDeleteIds)){
            BaseQuery<MdSupplierPriceDetail> baseQueryDelete =BaseQuery.getInstance(new MdSupplierPriceDetail());
            baseQueryDelete.addIn("id",priceDetailDeleteIds);
            supplierMaterialPriceDetailManager.delete(baseQueryDelete);
        }

        //在修改
        List<MdSupplierPriceDetail> bomDetailDoList = mdSupplierPriceHeadDo.getMdSupplierPriceDetailDoList();
        for (MdSupplierPriceDetail each: bomDetailDoList) {
            BaseQuery<MdSupplierPriceDetail> baseQueryDetail =BaseQuery.getInstance(new MdSupplierPriceDetail());
            baseQueryDetail.getData().setSupPriceId(mdSupplierPriceHeadDo.getId());
            baseQueryDetail.getData().setId(each.getId());

            each.setVersion(each.getVersion()+1);
            each.setGmtModified(new Date());
            UpdateByQuery<MdSupplierPriceDetail> updateBaseQueryDetail = new UpdateByQuery<>(baseQueryDetail,each);
            supplierMaterialPriceDetailManager.updateByQuery(updateBaseQueryDetail);
        }
        return mdSupplierPriceHeadDo.getId();
    }




    @Override
    public Long updateMdSupplierPriceStatus(Long id, String status) {
        BaseQuery<MdSupplierPriceHead> baseQuery= BaseQuery.getInstance(new MdSupplierPriceHead());
        baseQuery.getData().setId(id);

        MdSupplierPriceHead mdSupplierPriceHead = new MdSupplierPriceHead();
        mdSupplierPriceHead.setSupStatus(status);
        UpdateByQuery<MdSupplierPriceHead> bomHeadQuery =new UpdateByQuery<>(baseQuery,mdSupplierPriceHead);
        //修改主表
        supplierMaterialPriceHeadManager.updateByQuery(bomHeadQuery);

        //修改明细表  ---根据订单头id 批量修改明细状态
        BaseQuery<MdSupplierPriceDetail> detailBaseQuery =BaseQuery.getInstance(new MdSupplierPriceDetail());
        detailBaseQuery.getData().setSupPriceId(id);

        MdSupplierPriceDetail mdSupplierPriceDetail = new MdSupplierPriceDetail();
        mdSupplierPriceDetail.setSupDetailStatus(status);
        UpdateByQuery<MdSupplierPriceDetail> priceDetailBaseQuery = new UpdateByQuery<>(detailBaseQuery,mdSupplierPriceDetail);
        supplierMaterialPriceDetailManager.updateByQuery(priceDetailBaseQuery);

        return id;
    }


    @Override
    public List<MdSupplierPriceHead> exportDocSupplierPrice(Long id) {
        return null;
    }


    private MdSupplierPriceHead generateMdSupplierPriceHeadForUpdateInsert(MdSupplierPriceHeadDo mdSupplierPriceHeadDo) {
        MdSupplierPriceHead mdSupplierPriceHead = generateMdSupplierPriceHeadForInsert(mdSupplierPriceHeadDo);
        mdSupplierPriceHead.setPriceNo(UniqueCodeGenerator.generate(KeyPrefix.SPL.getPrefix(), "D"));
        mdSupplierPriceHead.setVersion(mdSupplierPriceHeadDo.getVersion()+1);
        mdSupplierPriceHead.setSupStatus(mdSupplierPriceHeadDo.getSupStatus());
        return mdSupplierPriceHead;
    }


    private MdSupplierPriceHead generateMdSupplierPriceHeadForUpdate(MdSupplierPriceHeadDo mdSupplierPriceHeadDo) {
        MdSupplierPriceHead mdSupplierPriceHead =new MdSupplierPriceHead();
        mdSupplierPriceHead.setEndEffect(DateUtil.parseDate(mdSupplierPriceHeadDo.getEndEffect(),"yyyy-MM-dd"));
        mdSupplierPriceHead.setPriceNo(mdSupplierPriceHeadDo.getPriceNo());
        mdSupplierPriceHead.setSupStatus(mdSupplierPriceHeadDo.getSupStatus());
        mdSupplierPriceHead.setStartEffect(DateUtil.parseDate(mdSupplierPriceHeadDo.getStartEffect(),"yyyy-MM-dd"));
        mdSupplierPriceHead.setVersion(mdSupplierPriceHeadDo.getVersion()+1);
        mdSupplierPriceHead.setSupplierId(mdSupplierPriceHeadDo.getSupplierId());
        mdSupplierPriceHead.setMaterialTypes(mdSupplierPriceHeadDo.getMaterialTypes());
        mdSupplierPriceHead.setUpdaterId(mdSupplierPriceHead.getUpdaterId());
        mdSupplierPriceHead.setUpdaterName(mdSupplierPriceHead.getUpdaterName());
        mdSupplierPriceHead.setContactId(mdSupplierPriceHeadDo.getContactId());
        return mdSupplierPriceHead;
    }


    @Override
    public Long deleteById(Long id) {
        //删除head
        int i = supplierMaterialPriceHeadManager.deleteById(id);
        if(i>0){
            //删除detail
            MdSupplierPriceDetail md = new MdSupplierPriceDetail();
            md.setSupPriceId(id);
            BaseQuery<MdSupplierPriceDetail> supplierPriceDetailQuery =BaseQuery.getInstance(md);
            try {
                supplierMaterialPriceDetailManager.delete(supplierPriceDetailQuery);
            }catch (Exception e){
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[SCM-删除供应商报价单异常]")
                        .addParm("SCM 供应商报价单异常,异常headId:",id)
                        .setException(e)
                        .log();
            }
        }
        return id;
    }



    @Override
    public List<MdSupplierPriceHeadDo> findEffectiveList(String shopDetailId,Long supplierId) {
        List<MdSupplierPriceHeadDo> mdSupplierPriceHeads =supplierMaterialPriceHeadManager.findEffectiveList(shopDetailId,supplierId);

        List<MdSupplierPriceHeadDo> returnList = new ArrayList<>(mdSupplierPriceHeads.size());

        for (MdSupplierPriceHeadDo mdSupplierPriceHeadDo:mdSupplierPriceHeads) {
            List<MdSupplierPriceDetail> query = supplierMaterialPriceDetailManager.
                    queryJoinMaterialView(mdSupplierPriceHeadDo.getId(),mdSupplierPriceHeadDo.getVersion());
            mdSupplierPriceHeadDo.setMaterialSizes(query.size());
            mdSupplierPriceHeadDo.setMaterialTypes(countMaterialTypes(query));
            mdSupplierPriceHeadDo.setMdSupplierPriceDetailDoList(query);
            returnList.add(mdSupplierPriceHeadDo);
        }
        return returnList;
    }

    @Override
    public List<String> findEffectiveSupPriceIds(String shopDetailId, Long supplierId) {
        List<MdSupplierPriceHeadDo> effectiveList = supplierMaterialPriceHeadManager.findEffectiveList(shopDetailId, supplierId);
        List<String> supPriceIds = new ArrayList<>();
        if(ListUtil.isNotEmpty(effectiveList)){
            effectiveList.forEach(each->{
                supPriceIds.add(each.getSupCode());
            });
        }
        return   supPriceIds;

    }

    private MdSupplierPriceDetail generateSupplierMaterialPriceDetailForUpdate(MdSupplierPriceDetailDo each) {
        MdSupplierPriceDetail mdSupplierPriceDetail =  new MdSupplierPriceDetail();
        mdSupplierPriceDetail.setUpdaterId(each.getUpdaterId());
        mdSupplierPriceDetail.setUpdaterName(each.getUpdaterName());
        mdSupplierPriceDetail.setPurchasePrice(each.getPurchasePrice());
        mdSupplierPriceDetail.setVersion(each.getVersion()+1);
        return mdSupplierPriceDetail;
    }

    private MdSupplierPriceDetail generateMdSupplierPriceDetailForInsert(MdSupplierPriceHead mdSupplierPriceHead, MdSupplierPriceDetailDo mdSupplierPriceDetailDo) {
        MdSupplierPriceDetail mdSupplierPriceDetail = new MdSupplierPriceDetail();
        mdSupplierPriceDetail.setSupPriceId(mdSupplierPriceHead.getId());
        mdSupplierPriceDetail.setSupPriceNo(mdSupplierPriceHead.getPriceNo());
        mdSupplierPriceDetail.setCreaterId(mdSupplierPriceHead.getCreaterId());
        mdSupplierPriceDetail.setCreaterName(mdSupplierPriceHead.getCreaterName());
        mdSupplierPriceDetail.setMaterialCode(mdSupplierPriceDetailDo.getMaterialCode());
        mdSupplierPriceDetail.setMaterialId(mdSupplierPriceDetailDo.getMaterialId());
        mdSupplierPriceDetail.setPurchasePrice(mdSupplierPriceDetailDo.getPurchasePrice());
        mdSupplierPriceDetail.setSupDetailStatus(mdSupplierPriceHead.getSupStatus());
        mdSupplierPriceDetail.setVersion(1);
        return mdSupplierPriceDetail;
    }


    private String getCateGoryNames(String categoryIds, List<MdCategory> mdCategoryOnes) {
        Map<Long,MdCategory> mdCategoryMap = new HashMap<>(mdCategoryOnes.size());
        for (MdCategory category:mdCategoryOnes ) {
            mdCategoryMap.put(category.getId(),category);
        }
        String[] split = categoryIds.split(",");
        StringBuffer sb = new StringBuffer();
        for (String each: split) {
            MdCategory mdCategory = mdCategoryMap.get(Long.valueOf(each));
            if(mdCategory != null){
                sb.append(mdCategory.getCategoryName());
            }
        }

        return sb.toString();
    }



}
