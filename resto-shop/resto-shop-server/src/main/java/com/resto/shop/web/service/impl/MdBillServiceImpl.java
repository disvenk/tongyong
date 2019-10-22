package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.dto.DocStkInPlanDetailDo;
import com.resto.shop.web.dto.DocStkInPlanHeaderDetailDo;
import com.resto.shop.web.dto.MdBillDo;
import com.resto.shop.web.enums.MaterialTypeStatus;
import com.resto.shop.web.manager.MdBillManager;
import com.resto.shop.web.model.MdBill;
import com.resto.shop.web.service.MdBillService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Component
@Service
public class MdBillServiceImpl implements MdBillService {

    @Resource
    private MdBillManager mdBillManager;


    public List<MdBill> selectList() {
        return mdBillManager.findMdBillsByMdBill(new MdBill());
    }


    public MdBill selectById(Long id) {
        return mdBillManager.getById(id);
    }

    /**
     *
     *
     * @param mdBill
     * @return
     */
    @Override
    public Integer insert(MdBill mdBill) {
        return mdBillManager.insert(mdBill);
    }


    public Integer update(MdBill mdBill) {
        return mdBillManager.update(mdBill);

    }

    public Integer deleteById(Long id) {
        return mdBillManager.deleteById(id);

    }

    public List<MdBillDo> queryJoin4Page(String shopId, String beginDate, String endDate) {
        if(StringUtils.isNotEmpty(beginDate)){
            beginDate =beginDate+" 00:00:00";
        }

        if(StringUtils.isNotEmpty(endDate)){
            endDate =endDate+" 12:59:59";
        }


        return mdBillManager.queryJoin4Page(shopId, beginDate, endDate);
    }


    public List<DocStkInPlanHeaderDetailDo> queryJoin4PageAndBill(String shopId, String beginDate, String endDate) {
        if(StringUtils.isNotEmpty(beginDate)){
            beginDate =beginDate+" 00:00:00";
        }

        if(StringUtils.isNotEmpty(endDate)){
            endDate =endDate+" 12:59:59";
        }

        List<DocStkInPlanHeaderDetailDo> docStkInPlanHeaderDos = mdBillManager.queryJoin4PageAndBill(shopId, beginDate, endDate);
        Map<Long, List<DocStkInPlanHeaderDetailDo>> collects = docStkInPlanHeaderDos.stream().collect(Collectors.groupingBy(DocStkInPlanHeaderDetailDo::getBillId));
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
            docStkInPlanHeaderDetailDo.setMaterialTypes(countMaterialTypes(docStkInPlanDetailDos));
            docStkInPlanHeaderDetailDo.setDocStkInPlanDetailDoList(docStkInPlanDetailDos);
            returnDocStkInPlanHeaderDos.add(docStkInPlanHeaderDetailDo);
        }



        return returnDocStkInPlanHeaderDos;


    }



    private String countMaterialTypes(List<DocStkInPlanDetailDo> docStkInPlanDetailDoList) {
        Set<String> sets = new HashSet<>();
        for (DocStkInPlanDetailDo docStkInPlanDetailDo : docStkInPlanDetailDoList) {
            Arrays.stream(MaterialTypeStatus.values()).forEach((MaterialTypeStatus item) -> {
                if (docStkInPlanDetailDo.getMaterialType() != null && docStkInPlanDetailDo.getMaterialType().equals(item.getValue())) {
                    sets.add(item.getDescription());
                }
            });
        }
        return String.join(",", sets);
    }


    private DocStkInPlanDetailDo generateDocStkInPlanDetailDo(DocStkInPlanHeaderDetailDo docStkInPlanHeaderDetailDo) {
        DocStkInPlanDetailDo docStkInPlanDetailDo = new DocStkInPlanDetailDo();
        docStkInPlanDetailDo.setActQty(docStkInPlanHeaderDetailDo.getActQty());
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
        //docStkInPlanDetailDo.setMaterialId(docStkInPlanHeaderDeta);
        return docStkInPlanDetailDo;
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







}
