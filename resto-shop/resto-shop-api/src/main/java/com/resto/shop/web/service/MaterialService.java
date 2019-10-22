package com.resto.shop.web.service;



import com.resto.shop.web.baseScm.domain.PageResult;
import com.resto.shop.web.dto.MaterialDo;
import com.resto.shop.web.model.MdMaterial;

import java.util.List;

/***
 * 原料清单管理
 */
public interface MaterialService {


    /**
     *
     *  分页查询原料清单
     * @param mdMaterial
     * @return
     */

    PageResult<MdMaterial> query4Page(MdMaterial mdMaterial);




    /**
     *
     *  根据id查询原料清单
     * @param id
     * @return
     */

    MdMaterial queryById(Long id);


    /**
     *
     *  新增原料
     * @param mdMaterial
     * @return
     */
    Integer addMaterial(MdMaterial mdMaterial);


    /**
     *
     *  修改原料信息  系统记录原料版本
     * @param mdMaterial
     * @return
     */
    int updateMaterial(MdMaterial mdMaterial);



    Integer deleteById(Long id);



    /**
     *
     *  多表分页查询原料清单
     * @param
     * @return
     */

    List<MaterialDo> queryJoin4Page(String shopId);




}
