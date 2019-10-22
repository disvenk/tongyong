package com.resto.shop.web.service;



import com.resto.shop.web.dto.DocReturnHeaderDetailDo;
import com.resto.shop.web.model.DocReturnHeader;

import java.util.List;

public interface DocReturnHeaderService {


	 List<DocReturnHeader> selectList();

	 List<DocReturnHeaderDetailDo> queryJoin4Page(String shopId, String shopName);

	 DocReturnHeader selectById(Long id);

	 Integer insert(DocReturnHeader docReturnHeader);

	 Integer update(DocReturnHeader docReturnHeader);

	 Integer deleteById(Long id);

	/**
	 *
	 *  根据退款单id, 修改单状态
	 * @param  id
	 * @param  status
	 * @return
	 */
	Long updateDocReturnHeaderStatus(Long id, String status);
    
}
