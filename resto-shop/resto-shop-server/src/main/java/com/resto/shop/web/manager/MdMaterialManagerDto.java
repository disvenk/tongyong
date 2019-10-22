package com.resto.shop.web.manager;



import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdMaterialDaoDto;
import com.resto.shop.web.dto.MaterialDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("mdMaterialManagerDto")
public class MdMaterialManagerDto extends BaseManager<MaterialDo> {
    @Autowired
    private MdMaterialDaoDto mdMaterialDaoDto;


    @Override
    public BaseDao<MaterialDo> getDao() {
        return this.mdMaterialDaoDto;
    }


    public List<MaterialDo> queryJoin4Page() {
        return mdMaterialDaoDto.queryJoin4Page();
    }


}
