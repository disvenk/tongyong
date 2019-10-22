package com.resto.shop.web.service;



import com.resto.shop.web.model.DocReturnDetail;

import java.util.List;

public interface DocReturnDetailService {


	 List<DocReturnDetail> selectList();

	 List<DocReturnDetail> queryJoin4Page(String shopId);

	 DocReturnDetail selectById(Long id);

	 Integer insert(DocReturnDetail docReturnDetail);

	 Integer update(DocReturnDetail docReturnDetail);

	 Integer deleteById(Long id);
    
}
