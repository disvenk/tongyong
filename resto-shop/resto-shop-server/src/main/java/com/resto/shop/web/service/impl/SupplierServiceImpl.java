package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.dto.*;
import com.resto.shop.web.enums.MaterialTypeStatus;
import com.resto.shop.web.enums.SupplierTypeStatus;
import com.resto.shop.web.exception.ScmServiceException;
import com.resto.shop.web.manager.MdCategoryManager;
import com.resto.shop.web.manager.MdSupplierContactManager;
import com.resto.shop.web.manager.MdSupplierManager;
import com.resto.shop.web.manager.SupplierMaterialPriceHeadManager;
import com.resto.shop.web.model.MdCategory;
import com.resto.shop.web.model.MdSupplier;
import com.resto.shop.web.model.MdSupplierContact;
import com.resto.shop.web.service.SupplierService;
import com.resto.shop.web.util.ListUtil;
import com.resto.shop.web.util.keygenerate.KeyPrefix;
import com.resto.shop.web.util.keygenerate.UniqueCodeGenerator;
import com.resto.shop.web.util.log.LogBetter;
import com.resto.shop.web.util.log.LogLevel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;
import static com.resto.shop.web.util.log.TraceLoggerFactory.logger;


@Component
@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private MdSupplierManager mdSupplierManager;
    @Autowired
    private MdSupplierContactManager mdSupplierContactManager;
    @Autowired
    private SupplierMaterialPriceHeadManager supplierMaterialPriceHeadManager;
    @Autowired
    private MdCategoryManager mdCategoryManager;

    @Override
    public Long addMdSupplier(MdSupplierDo mdSupplierDo) {

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[SCM-新增供应商开始]")
                .addParm("供应商信息", mdSupplierDo)
                .log();
        if (null == mdSupplierDo || ListUtil.isNullOrEmpty(mdSupplierDo.getSupplierContacts())) {
            throw new ScmServiceException("SCM 新增供应商联系人为空或明细不存在");
        }
        try {

            MdSupplierContact topSupplierContact = findTopSupplierContact(mdSupplierDo);
            if(topSupplierContact  == null){
                throw  new ScmServiceException("请选这最常用联系人");
            }
            MdSupplier mdSupplier = generateMdSupplierForInsert(mdSupplierDo,topSupplierContact);
            mdSupplierManager.insert(mdSupplier);

            for (MdSupplierContact mdSupplierContact : mdSupplierDo.getSupplierContacts()) {

                mdSupplierContactManager.insert(fillMdSupplierContact(mdSupplier,mdSupplierContact));
            }
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[SCM-新增供应商成功]")
                    .log();
            return mdSupplier.getId();
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[SCM-新增供应商异常]")
                    .addParm("SCM 供应商信息", mdSupplierDo)
                    .setException(e)
                    .log();
            throw new ScmServiceException("新增供应商异常" + e.getMessage());
        }


    }

    private MdSupplierContact fillMdSupplierContact(MdSupplier mdSupplier, MdSupplierContact mdSupplierContact) {
        mdSupplierContact.setSupCode(mdSupplier.getSupCode());
        mdSupplierContact.setSupplierId(mdSupplier.getId());
        mdSupplierContact.setCreaterId(mdSupplier.getCreaterId());
        mdSupplier.setCreaterName(mdSupplier.getCreaterName());
        mdSupplier.setGmtCreate(new Date());
        return mdSupplierContact;
    }

    private MdSupplierContact findTopSupplierContact(MdSupplierDo mdSupplierDo) {
        for (MdSupplierContact mdSupplierContact : mdSupplierDo.getSupplierContacts()) {
            Integer isTop = mdSupplierContact.getIsTop();
            if(Integer.valueOf(0).equals(isTop)){
                return mdSupplierContact;
            }
        }
        return null;
    }

    private MdSupplier generateMdSupplierForInsert(MdSupplierDo mdSupplierDo, MdSupplierContact topSupplierContact) {
        MdSupplier mdSupplier = new MdSupplier();
        mdSupplier.setSupCode(UniqueCodeGenerator.generate(KeyPrefix.SUP.getPrefix(), "S"));
        mdSupplier.setBankAccount(mdSupplierDo.getBankAccount());
        mdSupplier.setBankName(mdSupplierDo.getBankName());
        mdSupplier.setSupName(mdSupplierDo.getSupName());
        mdSupplier.setSupAliasName(mdSupplierDo.getSupAliasName());
        mdSupplier.setSupplierType(mdSupplierDo.getSupplierType());
        mdSupplier.setCreaterId(mdSupplierDo.getCreaterId());
        mdSupplier.setCreaterName(mdSupplierDo.getCreaterName());
        mdSupplier.setNote(mdSupplierDo.getNote());
        mdSupplier.setShopDetailId(mdSupplierDo.getShopDetailId());
        mdSupplier.setTopContact(topSupplierContact.getContact());
        mdSupplier.setTopEmail(topSupplierContact.getEmail());
        mdSupplier.setTopMobile(topSupplierContact.getMobile());
        mdSupplier.setTopPosition(topSupplierContact.getPosition());
        mdSupplier.setVersion(mdSupplierDo.getVersion()== null ? "1":mdSupplierDo.getVersion());
        mdSupplier.setProductTypes(mdSupplierDo.getMaterialTypes());
        mdSupplier.setTaxNumber(mdSupplierDo.getTaxNumber());
        mdSupplier.setState(mdSupplierDo.getState());
        return mdSupplier;
    }

    @Override
    public List<MdSupplierAndContactDo> queryJoin4Page(String shopDetailId, Integer state) {

        List<MdSupplierAndContactDo> mdSupplierDos = mdSupplierManager.queryJoin4Page(shopDetailId,state);
        Map<Long, List<MdSupplierAndContactDo>> collect = mdSupplierDos.stream().collect(Collectors.groupingBy(MdSupplierAndContactDo::getId));
        List<MdSupplierAndContactDo> supplierDos =new ArrayList<>(collect.size());
           for (Map.Entry entity:collect.entrySet() ) {
               List<MdSupplierAndContactDo> values =(List<MdSupplierAndContactDo>)entity.getValue();
               if(ListUtil.isNotEmpty(values)){
                   MdSupplierAndContactDo mdSupplierAndContactDo = convertMdSupplierAndContactDo(values);
                   if(mdSupplierAndContactDo !=null){
                       supplierDos.add(mdSupplierAndContactDo);
                   }
               }else{
                   LogBetter.instance(logger)
                           .setLevel(LogLevel.INFO)
                           .setMsg("[SCM-查询供应商] key:"+entity.getKey()+"value:"+values)
                           .log();
               }
           }

        return supplierDos;

    }

    private MdSupplierAndContactDo convertMdSupplierAndContactDo(List<MdSupplierAndContactDo> values) {
        MdSupplierAndContactDo mdSupplierAndContactDo = new MdSupplierAndContactDo();
        if(ListUtil.isNotEmpty(values)){
            //组装供应商 表头
             mdSupplierAndContactDo =values.get(0);
            //供应商类型
            String supplierTypeShow = convertMdSupplierTypeToDescription(mdSupplierAndContactDo.getSupplierType());
            if(supplierTypeShow == null){
                LogBetter.instance(logger)
                   .setLevel(LogLevel.INFO)
                   .setMsg("[SCM-SupplierServiceImpl.convertMdSupplierAndContactDo,SupplierType："+mdSupplierAndContactDo.getSupplierType()+"转化供应商名称不存在]"+supplierTypeShow)
                   .log();
            }
            mdSupplierAndContactDo.setSupplierTypeShow(supplierTypeShow);
            mdSupplierAndContactDo.setSupplierType(mdSupplierAndContactDo.getSupplierType());
            mdSupplierAndContactDo.setMaterialIds(mdSupplierAndContactDo.getMaterialTypes());
            //获取供应商的产品组成
            if (StringUtils.isNotBlank(mdSupplierAndContactDo.getMaterialTypes())){
                StringBuilder sb = new StringBuilder("");
                String types[] = mdSupplierAndContactDo.getMaterialTypes().split(",");
                for (String type : types) {
                    if (StringUtils.isNoneBlank(type)){
                        MdCategory mdCategory = mdCategoryManager.getById(Long.parseLong(type));
                        if (mdCategory != null) {
                            sb.append(mdCategory.getCategoryName()).append(",");
                        }
                    }
                }
                if (sb.length()>0){
                    mdSupplierAndContactDo.setMaterialTypes(sb.substring(0, sb.length()-1));
                }
            }

            //组装联系人信息
            List<MdSupplierContact> mdSupplierContacts = new ArrayList<>(values.size());
            for (MdSupplierAndContactDo each:values) {
                mdSupplierContacts.add(convertMdSupplierContact(each));
            }
            mdSupplierAndContactDo.setSupplierContacts(mdSupplierContacts);
        }

        return mdSupplierAndContactDo;
    }

    private String convertMdSupplierTypeToDescription(String supplierType) {
        SupplierTypeStatus[] values = SupplierTypeStatus.values();
        for (SupplierTypeStatus v:values) {
            if(v.getValue().equals(supplierType)){
                return  v.getDescription();
            }
        }
        return null;
    }

    private String convertMaterialTypeName(String materialTypes) {
        String[] splits = materialTypes.split(",");

        MaterialTypeStatus[] values = MaterialTypeStatus.values();
        Map<String,String> map =new HashMap<>(values.length);
        for (MaterialTypeStatus e:values) {
            map.put(e.getValue(),e.getDescription());
        }
        StringBuffer sb =new StringBuffer();
        for (String  materialType:splits) {
            if(map.containsKey(materialType)){
                sb.append(map.get(materialType));
            }
        }

        return sb.toString();
    }

    private MdSupplierContact convertMdSupplierContact(MdSupplierAndContactDo each) {
        MdSupplierContact contact = new MdSupplierContact();
        contact.setId(each.getSupContactId());
        contact.setSupplierId(each.getId());
        contact.setContact(each.getContact());
        contact.setEmail(each.getEmail());
        contact.setMobile(each.getMobile());
        contact.setIsTop(each.getIsTop());
        contact.setPosition(each.getPosition());
        return contact;
    }

    private String getCateGoryNames(String  categoryIds, List<MdCategory> mdCategoryOnes) {
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


    @Override
    public MdSupplier queryById(Long id) {
        return mdSupplierManager.getById(id);
    }

    @Override
    public Integer updateMdSupplier(MdSupplierDo mdSupplierDo) {

        MdSupplierContact topSupplierContact = findTopSupplierContact(mdSupplierDo);
        if(topSupplierContact  == null){
            throw  new ScmServiceException("请选这最常用联系人");
        }

        //更新主表
        BaseQuery<MdSupplier> baseQuery = BaseQuery.getInstance(new MdSupplier());
        baseQuery.getData().setId(mdSupplierDo.getId());
        UpdateByQuery<MdSupplier> mdSupplierByQuery = new UpdateByQuery<>(baseQuery,generateMdSupplier(mdSupplierDo,topSupplierContact));
        int i = mdSupplierManager.updateByQuery(mdSupplierByQuery);

        //更新联系人表  有移除的先删除
        List<Long> supContactIds = mdSupplierDo.getSupContactIds();

        if(ListUtil.isNotEmpty(supContactIds)){
            BaseQuery<MdSupplierContact> baseQueryDelete =BaseQuery.getInstance(new MdSupplierContact());
            baseQueryDelete.addIn("id",supContactIds);
            mdSupplierContactManager.delete(baseQueryDelete);
        }
        //在修改
        List<MdSupplierContact> supplierContacts = mdSupplierDo.getSupplierContacts();
        for (MdSupplierContact each: supplierContacts) {
            if(each.getId() != null){
                BaseQuery<MdSupplierContact> baseQueryContact =BaseQuery.getInstance(new MdSupplierContact());
                baseQueryContact.getData().setSupplierId(mdSupplierDo.getId());
                baseQueryContact.getData().setId(each.getId());
                each.setGmtModified(new Date());
                UpdateByQuery<MdSupplierContact> updateByQuery = new UpdateByQuery<>(baseQueryContact,each);
                mdSupplierContactManager.updateByQuery(updateByQuery);
            }else{
                mdSupplierContactManager.insert(fillMdSupplierContact(generateMdSupplier(mdSupplierDo,topSupplierContact),each) );
            }

        }
        return  i;
    }

    private MdSupplier generateMdSupplier(MdSupplierDo mdSupplierDo, MdSupplierContact topSupplierContact) {
        MdSupplier mdSupplier =  new MdSupplier();
        mdSupplier.setGmtModified(new Date());
        mdSupplier.setNote(mdSupplierDo.getNote());
        mdSupplier.setSupplierType(mdSupplierDo.getSupplierType());
        mdSupplier.setSupCode(mdSupplierDo.getSupCode());
        mdSupplier.setSupName(mdSupplierDo.getSupName());
        mdSupplier.setSupAliasName(mdSupplierDo.getSupAliasName());
        mdSupplier.setTaxNumber(mdSupplierDo.getTaxNumber());
        mdSupplier.setProductTypes(mdSupplierDo.getMaterialTypes());
        mdSupplier.setBankName(mdSupplierDo.getBankName());
        mdSupplier.setBankAccount(mdSupplierDo.getBankAccount());
        mdSupplier.setVersion(mdSupplierDo.getVersion());
        mdSupplier.setUpdaterName(mdSupplierDo.getUpdaterName());
        mdSupplier.setUpdaterId(mdSupplierDo.getUpdaterId());
        mdSupplier.setTopContact(topSupplierContact.getContact());
        mdSupplier.setTopMobile(topSupplierContact.getMobile());
        mdSupplier.setTopEmail(topSupplierContact.getEmail());
        mdSupplier.setId(mdSupplierDo.getId());
        mdSupplier.setTopPosition(topSupplierContact.getPosition());
        mdSupplier.setState(mdSupplierDo.getState());
        return mdSupplier;
    }

    @Override
    public Integer delete(Long id,String shopId) {

        int i = mdSupplierManager.deleteById(id);
        if(i>0){
            //删除detail
            MdSupplierContact md = new MdSupplierContact();
            md.setSupplierId(id);
            BaseQuery<MdSupplierContact> supplierPriceDetailQuery =BaseQuery.getInstance(md);
            try {
                mdSupplierContactManager.delete(supplierPriceDetailQuery);
            }catch (Exception e){
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setMsg("[SCM-删除供应商联系人异常]")
                        .addParm("SCM 供应商联系人异常,异常supplierId:",id)
                        .setException(e)
                        .log();
            }
        }
        return i;


    }

    @Override
    public List<SupplierAndSupPriceListDo> querySupplierAndSupPrice(String shopId) {
        List<SupplierAndSupPriceDo> supplierAndSupPriceDos = mdSupplierManager.querySupplierAndSupPrice(shopId);
        Map<Long, List<SupplierAndSupPriceDo>> collect = supplierAndSupPriceDos.stream().collect(Collectors.groupingBy(SupplierAndSupPriceDo::getSupplierId));
        List<SupplierAndSupPriceListDo> supplierAndSupPriceListDos =new ArrayList<>();
        for (Map.Entry<Long,List<SupplierAndSupPriceDo>> each:collect.entrySet()) {
           supplierAndSupPriceListDos.add(convertSupplierAndSupPriceList(each));
        }
        return supplierAndSupPriceListDos;
    }

    private SupplierAndSupPriceListDo convertSupplierAndSupPriceList(Map.Entry<Long,List<SupplierAndSupPriceDo>> each) {
        SupplierAndSupPriceListDo supplierAndSupPriceListDo =  new SupplierAndSupPriceListDo();
        List<SupplierAndSupPriceDo> value = each.getValue();
        supplierAndSupPriceListDo.setSupName(value.get(0).getSupName());
        supplierAndSupPriceListDo.setSupplierId(each.getKey());
        supplierAndSupPriceListDo.setSupplierAndSupPriceList(value);
        return supplierAndSupPriceListDo;

    }


    @Override
    public List<SupplierAndPmsHeadListDo> querySupplierAndPmsHeadDo(String shopId) {
        List<SupplierAndPmsHeadDo> supplierAndPmsHeadDos = mdSupplierManager.querySupplierAndPmsHeadDo(shopId);
        Map<Long, List<SupplierAndPmsHeadDo>> collect = supplierAndPmsHeadDos.stream().collect(Collectors.groupingBy(SupplierAndPmsHeadDo::getSupplierId));
        List<SupplierAndPmsHeadListDo> supplierAndPmsHeadListDos = new ArrayList<>();
        for (Map.Entry<Long,List<SupplierAndPmsHeadDo>> each:collect.entrySet()) {
            supplierAndPmsHeadListDos.add(convertSupplierAndSPmsHeadDo(each));
        }
        return supplierAndPmsHeadListDos;
    }

    private SupplierAndPmsHeadListDo convertSupplierAndSPmsHeadDo(Map.Entry<Long, List<SupplierAndPmsHeadDo>> each) {
        SupplierAndPmsHeadListDo supplierAndPmsHeadListDo= new SupplierAndPmsHeadListDo();
        supplierAndPmsHeadListDo.setSupName(each.getValue().get(0).getSupName());
        supplierAndPmsHeadListDo.setSupplierId(each.getKey());
        supplierAndPmsHeadListDo.setSupplierAndPmsHeadDos(each.getValue());
        return supplierAndPmsHeadListDo;
    }
}
