package com.resto.brand.web.dao;

import com.resto.brand.web.model.ModuleList;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.resto.brand.core.generic.GenericDao;


public interface ModuleListMapper  extends GenericDao<ModuleList,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ModuleList record);

    int insertSelective(ModuleList record);

    ModuleList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ModuleList record);

    int updateByPrimaryKey(ModuleList record);

	void deleteBrandModuleList(String brandId);

	void insertBatch(@Param("brandId")String brandId, @Param("moduleIds")Integer[] moduleIds);

	List<Integer> selectBrandHasModule(String brandId);

	ModuleList selectBySignAndBrandId(String sign, String brandId);
}
