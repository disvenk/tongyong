package com.resto.shop.web.service.impl;



import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.PageResult;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.manager.MdUnitManager;
import com.resto.shop.web.model.MdUnit;
import com.resto.shop.web.service.ScmUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Service
public class ScmUnitServiceImpl implements ScmUnitService {

    @Autowired
    private MdUnitManager mdUnitManager;


    @Override
    public int addScmUnit(MdUnit unit) {
        return mdUnitManager.insert(unit);
    }

    @Override
    public PageResult<MdUnit> query4Page(MdUnit unit) {
        BaseQuery<MdUnit> unitQuery = BaseQuery.getInstance(unit) ;
        return mdUnitManager.query4Page(unitQuery);
    }

    @Override
    public MdUnit selectById(Long id) {
        return mdUnitManager.getById(id);
    }

    @Override
    public Integer update(MdUnit unit) {
        MdUnit m =new MdUnit();
        m.setId(unit.getId());
        BaseQuery<MdUnit> baseQuery = BaseQuery.getInstance(m);
        UpdateByQuery<MdUnit> unitQuery = new UpdateByQuery<>(baseQuery,unit);
        return mdUnitManager.updateByQuery(unitQuery);
    }

    @Override
    public Integer deleteById(Long id) {
        return mdUnitManager.deleteById(id);
    }

    @Override
    public List<MdUnit> queryByType(Integer type) {
        MdUnit unit = new MdUnit();
        unit.setType(type);
        BaseQuery<MdUnit> unitQuery = BaseQuery.getInstance(unit) ;
        return mdUnitManager.query(unitQuery);
    }
}
