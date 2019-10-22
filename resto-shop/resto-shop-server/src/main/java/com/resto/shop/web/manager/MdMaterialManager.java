package com.resto.shop.web.manager;


import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.helper.DaoHelper;
import com.resto.shop.web.baseScm.manager.BaseManager;
import com.resto.shop.web.dao.MdMaterialDao;
import com.resto.shop.web.dto.MaterialDo;
import com.resto.shop.web.model.MdMaterial;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component("mdMaterialManager")
public class MdMaterialManager extends BaseManager<MdMaterial> {
    @Resource
    private MdMaterialDao mdMaterialDao;


    @Override
    public BaseDao<MdMaterial> getDao() {
        return this.mdMaterialDao;
    }

    public static void main(String[] args) {
       DaoHelper.genXMLWithFeature("E:\\temp\\mapper\\MdMaterialDao.xml",
               MdMaterialDao.class, MdMaterial.class, "md_material",false);
    }



    public List<MaterialDo> queryJoin4Page(String shopId) {
        return mdMaterialDao.queryJoin4Page(shopId);
    }

    public List<MaterialDo> findMaterialByThirdId(Long thirdId, String shopId){
        return mdMaterialDao.findMaterialByThirdId(thirdId,shopId);
    }

}
