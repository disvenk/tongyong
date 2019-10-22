package com.resto.shop.web.service;



import com.resto.shop.web.dto.DocPmsPoDetailDo;
import com.resto.shop.web.dto.DocPmsPoHeaderDetailDo;
import com.resto.shop.web.model.DocPmsPoHeader;

import java.util.List;

public interface DocPmsPoHeaderService {


	 List<DocPmsPoHeaderDetailDo> queryJoin4Page(String shopId, String shopName);

	 DocPmsPoHeader selectById(Long id);

	 Integer insert(DocPmsPoHeader docPmsPoHeader);

	 Integer update(DocPmsPoHeader docPmsPoHeader);

	 Integer deleteById(Long id);

	 Integer updateStateById(Long id, Integer state, String auditName);

	 Integer createPmsPoHeaderDetailDo(DocPmsPoHeaderDetailDo detailDo);

	 List<DocPmsPoDetailDo> queryDocPmsPoDetailDos(String scmDocPmsPoHeaderId);

}
