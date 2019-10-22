package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.ModuleList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleListMapper  extends BaseDao<ModuleList,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ModuleList record);

    int insertSelective(ModuleList record);

    ModuleList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ModuleList record);

    int updateByPrimaryKey(ModuleList record);

	void deleteBrandModuleList(String brandId);

	void insertBatch(@Param("brandId") String brandId, @Param("moduleIds") Integer[] moduleIds);

	List<Integer> selectBrandHasModule(String brandId);

	ModuleList selectBySignAndBrandId(String sign, String brandId);
}
