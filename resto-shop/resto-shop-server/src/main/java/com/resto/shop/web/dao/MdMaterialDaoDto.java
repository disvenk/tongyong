package com.resto.shop.web.dao;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.dto.MaterialDo;

import java.util.List;

public interface MdMaterialDaoDto extends BaseDao<MaterialDo> {

    List<MaterialDo> queryJoin4Page();
}
