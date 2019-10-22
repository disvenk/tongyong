package com.resto.shop.web.service.impl;



import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.dto.DocPmsPoDetailDo;
import com.resto.shop.web.dto.DocPmsPoHeaderDetailDo;
import com.resto.shop.web.enums.StockCountStatus;
import com.resto.shop.web.manager.DocPmsPoDetailManager;
import com.resto.shop.web.manager.DocPmsPoHeaderManager;
import com.resto.shop.web.model.DocPmsPoDetail;
import com.resto.shop.web.model.DocPmsPoHeader;
import com.resto.shop.web.service.DocPmsPoHeaderService;
import com.resto.shop.web.util.keygenerate.KeyPrefix;
import com.resto.shop.web.util.keygenerate.UniqueCodeGenerator;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Component
@Service
public class DocPmsPoHeaderServiceImpl implements DocPmsPoHeaderService {

    @Resource
    private DocPmsPoHeaderManager docPmsPoHeaderManager;
    @Resource
    private DocPmsPoDetailManager docPmsPoDetailManager;

	private DocPmsPoDetailDo generateDocPmsPoHeaderDetailDo(DocPmsPoHeaderDetailDo docPmsPoHeaderDetailDo) {
		DocPmsPoDetailDo docPmsPoDetail =  new DocPmsPoDetailDo();
		docPmsPoDetail.setActQty(docPmsPoHeaderDetailDo.getActQty());
		docPmsPoDetail.setPlanQty(docPmsPoHeaderDetailDo.getPlanQty());
		docPmsPoDetail.setCategoryOneName(docPmsPoHeaderDetailDo.getCategoryOneName());
		docPmsPoDetail.setCategoryTwoName(docPmsPoHeaderDetailDo.getCategoryTwoName());
		docPmsPoDetail.setCategoryThirdName(docPmsPoHeaderDetailDo.getCategoryThirdName());
		docPmsPoDetail.setCityName(docPmsPoHeaderDetailDo.getCityName());
		docPmsPoDetail.setProvinceName(docPmsPoHeaderDetailDo.getProvinceName());
		docPmsPoDetail.setDistrictName(docPmsPoHeaderDetailDo.getDistrictName());
		docPmsPoDetail.setMaterialCode(docPmsPoHeaderDetailDo.getMaterialCode());
		docPmsPoDetail.setMaterialType(docPmsPoHeaderDetailDo.getMaterialType());
		docPmsPoDetail.setMaterialName(docPmsPoHeaderDetailDo.getMaterialName());
		docPmsPoDetail.setMeasureUnit(docPmsPoHeaderDetailDo.getMeasureUnit());
		docPmsPoDetail.setNote(docPmsPoHeaderDetailDo.getNote());
		docPmsPoDetail.setSpecName(docPmsPoHeaderDetailDo.getSpecName());
		docPmsPoDetail.setTopContact(docPmsPoHeaderDetailDo.getTopContact());
		docPmsPoDetail.setTopEmail(docPmsPoHeaderDetailDo.getTopEmail());
		docPmsPoDetail.setTopMobile(docPmsPoHeaderDetailDo.getTopMobile());
		docPmsPoDetail.setPurchaseMoney(docPmsPoHeaderDetailDo.getPurchaseMoney());
		docPmsPoDetail.setPurchaseTaxMoney(docPmsPoHeaderDetailDo.getPurchaseTaxMoney());
		docPmsPoDetail.setPurchaseRealMoney(docPmsPoHeaderDetailDo.getPurchaseRealMoney());
		docPmsPoDetail.setPurchaseRealTaxMoney(docPmsPoHeaderDetailDo.getPurchaseRealTaxMoney());
		docPmsPoDetail.setPurchaseRealMoney(docPmsPoHeaderDetailDo.getReceivedMoney());
		docPmsPoDetail.setReceivedTaxMoney(docPmsPoHeaderDetailDo.getReceivedTaxMoney());
		return docPmsPoDetail;
	}



	@Override
	public List<DocPmsPoHeaderDetailDo> queryJoin4Page(String shopId,String shopName) {

		List<DocPmsPoHeaderDetailDo> pmsPoHeaderDetailDos = docPmsPoHeaderManager.queryJoin4Page(shopId);
		for(DocPmsPoHeaderDetailDo docPmsPoHeaderDetailDo:pmsPoHeaderDetailDos){
			docPmsPoHeaderDetailDo.setShopName(shopName);
		}
		/*Map<Long,List<DocPmsPoHeaderDetailDo>> collects = pmsPoHeaderDetailDos.stream().collect(Collectors.groupingBy(DocPmsPoHeaderDetailDo::getId));

		List<DocPmsPoHeaderDetailDo> returnDocPmsPoHeaderDetailDos = new ArrayList<>(collects.size());
		for (Map.Entry<Long,List<DocPmsPoHeaderDetailDo>> each:collects.entrySet()) {
			//组装头部
			List<DocPmsPoHeaderDetailDo> values = each.getValue();
			DocPmsPoHeaderDetailDo pmsPoHeaderDetailDo = values.get(0);
			//pmsPoHeaderDetailDo.setAuditTime();
			pmsPoHeaderDetailDo.setOrderStatusShow(convertOrderStatus(pmsPoHeaderDetailDo.getOrderStatus()));
			//组装detail
			List<DocPmsPoDetailDo> docPmsPoDetails = new ArrayList<>(values.size());
			BigDecimal totalAmount =BigDecimal.ZERO;
			for (DocPmsPoHeaderDetailDo docPmsPoHeaderDetailDo:values) {
				docPmsPoDetails.add(generateDocPmsPoHeaderDetailDo(docPmsPoHeaderDetailDo));
				//参考价格含税
				//BigDecimal purchaseMoney = docPmsPoHeaderDetailDo.getPurchaseMoney();
				if(pmsPoHeaderDetailDo.getTotalAmount() == null){
					totalAmount =totalAmount.add(Optional.ofNullable(docPmsPoHeaderDetailDo.getPurchaseMoney()).orElse(BigDecimal.valueOf(0)).multiply(Optional.ofNullable(docPmsPoHeaderDetailDo.getPlanQty()).orElse(BigDecimal.valueOf(0))));
				}
			}
			pmsPoHeaderDetailDo.setDocPmsPoDetailDos(docPmsPoDetails);
			pmsPoHeaderDetailDo.setTotalAmount(pmsPoHeaderDetailDo.getTotalAmount()==null?totalAmount:pmsPoHeaderDetailDo.getTotalAmount());
			pmsPoHeaderDetailDo.setShopName(shopName);
			pmsPoHeaderDetailDo.setSize(docPmsPoDetails.size());
			returnDocPmsPoHeaderDetailDos.add(pmsPoHeaderDetailDo);

		}
		return returnDocPmsPoHeaderDetailDos;*/
		return  pmsPoHeaderDetailDos;

	}

	@Override
	public List<DocPmsPoDetailDo> queryDocPmsPoDetailDos(String scmDocPmsPoHeaderId) {
		List<DocPmsPoDetailDo> docPmsPoDetails=docPmsPoHeaderManager.queryDocPmsPoDetailDos(scmDocPmsPoHeaderId);
		return docPmsPoDetails;
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

	public DocPmsPoHeader selectById(Long id){
    		return docPmsPoHeaderManager.getById(id);
	}

	public Integer insert(DocPmsPoHeader docPmsPoHeader){
    		return docPmsPoHeaderManager.insert(docPmsPoHeader);
	}

	public Integer update(DocPmsPoHeader docPmsPoHeader){
    		return  docPmsPoHeaderManager.update(docPmsPoHeader);
	}

	/**
	 *审核接口
	 * @param id
	 * @param state
	 * @return
	 */
	@Override
	public Integer updateStateById(Long id, Integer state,String auditName) {
		DocPmsPoHeader where = new DocPmsPoHeader();
		where.setId(id);
		DocPmsPoHeader update = new DocPmsPoHeader();
		update.setOrderStatus(state);
		update.setAuditTime(new Date());
		update.setAuditName(auditName);
		docPmsPoHeaderManager.updateDocPmsPoHeaderOfCommon(where,update);
		DocPmsPoDetail whereDetail =new DocPmsPoDetail();
		whereDetail.setPmsHeaderId(id);
		DocPmsPoDetail updateDetail= new DocPmsPoDetail();
		updateDetail.setOrderDetailStatus(state);
		return  docPmsPoDetailManager.updateDocPmsPoDetailOfCommon(whereDetail,updateDetail);
	}

	@Override
	public Integer createPmsPoHeaderDetailDo(DocPmsPoHeaderDetailDo detailDo) {
		DocPmsPoHeader docPmsPoHeader = generateDocPmsPoHeader(detailDo);
		int insert = docPmsPoHeaderManager.insert(docPmsPoHeader);
		List<DocPmsPoDetailDo> docPmsPoDetailDos = detailDo.getDocPmsPoDetailDos();
		BigDecimal totalAmount =BigDecimal.ZERO;

		for (DocPmsPoDetailDo each : docPmsPoDetailDos) {
			if(detailDo.getTotalAmount() == null){
				totalAmount =totalAmount.add(Optional.ofNullable(each.getPurchaseMoney()).orElse(BigDecimal.valueOf(0)).multiply(Optional.ofNullable(detailDo.getPlanQty()).orElse(BigDecimal.valueOf(0))));
			}
			 docPmsPoDetailManager.insert(generateDocPmsPoDetail(each,docPmsPoHeader.getId(),docPmsPoHeader.getOrderCode()));
		}
		return insert;
	}

	private DocPmsPoDetail generateDocPmsPoDetail(DocPmsPoDetailDo each,Long pmsHeaderId,String orderCode) {
		DocPmsPoDetail docPmsPoDetail = new DocPmsPoDetail();
		docPmsPoDetail.setOrderDetailStatus(Integer.valueOf(StockCountStatus.INIT.getValue()));
		docPmsPoDetail.setPmsHeaderId(pmsHeaderId);
		docPmsPoDetail.setMaterialId(each.getMaterialId());
		docPmsPoDetail.setActQty(each.getActQty());
		docPmsPoDetail.setPlanQty(each.getPlanQty());
		docPmsPoDetail.setCreaterId(each.getCreaterId());
		docPmsPoDetail.setCreaterName(each.getCreaterName());
		docPmsPoDetail.setGmtCreate(new Date());
		docPmsPoDetail.setPmsHeaderCode(orderCode);
		docPmsPoDetail.setPurchaseMoney(each.getPurchaseMoney());//采购参考价格，不含税
		docPmsPoDetail.setNote(each.getNote());
		docPmsPoDetail.setSpecId(each.getSpecId());
		docPmsPoDetail.setSpecName(each.getSpecName());
		docPmsPoDetail.setSupPriceDetailId(each.getSupPriceDetailId());
		docPmsPoDetail.setUnitId(each.getUnitId());
		docPmsPoDetail.setUnitName(each.getUnitName());
		return docPmsPoDetail;
	}

	private DocPmsPoHeader generateDocPmsPoHeader(DocPmsPoHeaderDetailDo detailDo) {
		DocPmsPoHeader docPmsPoHeader = new DocPmsPoHeader();
		docPmsPoHeader.setCreaterId(detailDo.getCreaterId());
		docPmsPoHeader.setCreaterName(detailDo.getCreaterName());
		docPmsPoHeader.setExpectTime(detailDo.getExpectTime());
		docPmsPoHeader.setGmtCreate(new Date());
		docPmsPoHeader.setIsDelete(detailDo.getIsDelete());
		docPmsPoHeader.setNote(detailDo.getNote());
		docPmsPoHeader.setOrderCode(UniqueCodeGenerator.generate(KeyPrefix.PUO.getPrefix(), "O"));
		docPmsPoHeader.setOrderStatus(Integer.valueOf(StockCountStatus.INIT.getValue()));
		docPmsPoHeader.setShopDetailId(detailDo.getShopDetailId());
		docPmsPoHeader.setOrderName(detailDo.getOrderName());
		docPmsPoHeader.setShopName(detailDo.getShopName());
		docPmsPoHeader.setSupplierId(detailDo.getSupplierId());
		docPmsPoHeader.setSupPriceHeadId(detailDo.getSupPriceHeadId());
		docPmsPoHeader.setTotalAmount(detailDo.getTotalAmount());
		docPmsPoHeader.setTax(detailDo.getTax());
		return docPmsPoHeader;
	}

	public Integer deleteById(Long id){
    		return docPmsPoHeaderManager.deleteById(id);
    }










}
