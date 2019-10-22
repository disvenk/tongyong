package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.dto.DocReturnDetailDo;
import com.resto.shop.web.dto.DocReturnHeaderDetailDo;
import com.resto.shop.web.enums.MaterialTypeStatus;
import com.resto.shop.web.manager.DocReturnDetailManager;
import com.resto.shop.web.manager.DocReturnHeaderManager;
import com.resto.shop.web.model.DocReturnDetail;
import com.resto.shop.web.model.DocReturnHeader;
import com.resto.shop.web.service.DocReturnHeaderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Component
@Service
public class DocReturnHeaderServiceImpl implements DocReturnHeaderService {

    @Resource
    private DocReturnHeaderManager docReturnHeaderManager;
    @Resource
    private DocReturnDetailManager docReturnDetailManager;


    	public List<DocReturnHeader> selectList(){
    		return docReturnHeaderManager.findDocReturnHeadersByDocReturnHeader(new DocReturnHeader());
    	}

    	public DocReturnHeader selectById(Long id){
    		return docReturnHeaderManager.getById(id);
    	}

    	public Integer insert(DocReturnHeader docReturnHeader){
    		return docReturnHeaderManager.insert(docReturnHeader);

    	}

    	public Integer update(DocReturnHeader docReturnHeader){
    		return  docReturnHeaderManager.update(docReturnHeader);

    	}

    	public Integer deleteById(Long id){
    		return docReturnHeaderManager.deleteById(id);

    	}

	@Override
	public Long updateDocReturnHeaderStatus(Long id, String status) {

		BaseQuery<DocReturnHeader> baseQuery= BaseQuery.getInstance(new DocReturnHeader());
		baseQuery.getData().setId(id);

		DocReturnHeader docReturnHeader = new DocReturnHeader();
		docReturnHeader.setOrderStatus(status);
		UpdateByQuery<DocReturnHeader> headerUpdateByQuery =new UpdateByQuery<>(baseQuery,docReturnHeader);
		//修改主表
		docReturnHeaderManager.updateByQuery(headerUpdateByQuery);

		//修改明细表  ---根据订单头id 批量修改明细状态
		BaseQuery<DocReturnDetail> detailBaseQuery =BaseQuery.getInstance(new DocReturnDetail());
		detailBaseQuery.getData().setReturnHeaderId(id);

		DocReturnDetail returnDetail = new DocReturnDetail();
		returnDetail.setLineStatus(status);
		UpdateByQuery<DocReturnDetail> priceDetailBaseQuery = new UpdateByQuery<>(detailBaseQuery,returnDetail);
		docReturnDetailManager.updateByQuery(priceDetailBaseQuery);

		return id;

	}

	public  List<DocReturnHeaderDetailDo> queryJoin4Page(String shopId, String shopName){
			List<DocReturnHeaderDetailDo> docReturnHeaders = docReturnHeaderManager.queryJoin4Page(shopId);
			Map<Long, List<DocReturnHeaderDetailDo>> collects = docReturnHeaders.stream().collect(Collectors.groupingBy(DocReturnHeaderDetailDo::getId));
			List<DocReturnHeaderDetailDo> headerDetailDos = new ArrayList<>(docReturnHeaders.size());

			for (Map.Entry<Long,List<DocReturnHeaderDetailDo>> each:collects.entrySet()) {
				//组装头部
				List<DocReturnHeaderDetailDo> values = each.getValue();
				DocReturnHeaderDetailDo docReturnHeaderDetailDo = values.get(0);
				//组装detail
				List<DocReturnDetailDo> docReturnHeaderDetailDos = new ArrayList<>(values.size());
				for (DocReturnHeaderDetailDo docReturnHeaderDetailDoEach:values) {
					docReturnHeaderDetailDos.add(generateDocReturnDetailDo(docReturnHeaderDetailDoEach));
				}
				docReturnHeaderDetailDo.setDocReturnDetailDos(docReturnHeaderDetailDos);
				docReturnHeaderDetailDo.setShopName(shopName);
				docReturnHeaderDetailDo.setSize(values.size());
				headerDetailDos.add(docReturnHeaderDetailDo);

			}

			return headerDetailDos;

         }


	private DocReturnDetailDo generateDocReturnDetailDo(DocReturnHeaderDetailDo docReturnHeaderDetailDo) {
		DocReturnDetailDo docReturnDetailDo = new DocReturnDetailDo();
		docReturnDetailDo.setCategoryOneName(docReturnHeaderDetailDo.getCategoryOneName());
		docReturnDetailDo.setCategoryTwoName(docReturnHeaderDetailDo.getCategoryTwoName());
		docReturnDetailDo.setCategoryThirdName(docReturnHeaderDetailDo.getCategoryThirdName());
		docReturnDetailDo.setCityName(docReturnHeaderDetailDo.getCityName());
		docReturnDetailDo.setProvinceName(docReturnHeaderDetailDo.getProvinceName());
		docReturnDetailDo.setDistrictName(docReturnHeaderDetailDo.getDistrictName());
		docReturnDetailDo.setMaterialCode(docReturnHeaderDetailDo.getMaterialCode());
		docReturnDetailDo.setMaterialType(docReturnHeaderDetailDo.getMaterialType());
		docReturnDetailDo.setMaterialTypeShow(countStatus(MaterialTypeStatus.values(),docReturnHeaderDetailDo.getMaterialType()));

		docReturnDetailDo.setMaterialName(docReturnHeaderDetailDo.getMaterialName());
		docReturnDetailDo.setMeasureUnit(docReturnHeaderDetailDo.getMeasureUnit());
		docReturnDetailDo.setNote(docReturnHeaderDetailDo.getNote());
		docReturnDetailDo.setSpecName(docReturnHeaderDetailDo.getSpecName());
		docReturnDetailDo.setTopContact(docReturnHeaderDetailDo.getTopContact());
		docReturnDetailDo.setTopEmail(docReturnHeaderDetailDo.getTopEmail());
		docReturnDetailDo.setTopMobile(docReturnHeaderDetailDo.getTopMobile());
		docReturnDetailDo.setReturnCount(docReturnHeaderDetailDo.getReturnCount());
		return docReturnDetailDo;
	}


	private String countStatus(MaterialTypeStatus[] values, String materialType) {
		for (MaterialTypeStatus value:values) {
			if(value.getValue().equals(materialType)){
				return value.getDescription();
			}
		}
		return null;
	}

}
