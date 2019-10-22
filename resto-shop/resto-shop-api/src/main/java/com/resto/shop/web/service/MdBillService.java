package com.resto.shop.web.service;



import com.resto.shop.web.dto.DocStkInPlanHeaderDetailDo;
import com.resto.shop.web.dto.MdBillDo;
import com.resto.shop.web.model.MdBill;

import java.util.List;

public interface MdBillService {


	 List<MdBill> selectList();

	 List<MdBillDo> queryJoin4Page(String shopId, String beginDate, String endDate);

	List<DocStkInPlanHeaderDetailDo> queryJoin4PageAndBill(String shopId, String beginDate, String endDate);

	 MdBill selectById(Long id);

	 Integer insert(MdBill mdBill);

	 Integer update(MdBill mdBill);

	 Integer deleteById(Long id);

}
