package com.resto.shop.web.service;



import com.resto.shop.web.baseScm.domain.PageResult;
import com.resto.shop.web.model.MdUnit;

import java.util.List;

public interface ScmUnitService {


     int addScmUnit(MdUnit unit);


     PageResult<MdUnit> query4Page(MdUnit unit);

     MdUnit selectById(Long id);

     Integer update(MdUnit unit);

     Integer deleteById(Long id);

     List<MdUnit>queryByType(Integer type);
}
