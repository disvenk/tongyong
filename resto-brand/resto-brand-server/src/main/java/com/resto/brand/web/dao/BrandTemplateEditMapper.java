package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.BrandTemplateEdit;
import com.resto.brand.web.model.BrandTemplateRoot;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BrandTemplateEditMapper extends GenericDao<BrandTemplateEdit,Integer>{
    int deleteByPrimaryKey(Integer id);

    int insert(BrandTemplateEdit record);

    int insertSelective(BrandTemplateEdit record);

    BrandTemplateEdit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BrandTemplateEdit record);

    int updateByPrimaryKey(BrandTemplateEdit record);

    List<BrandTemplateEdit> selectListByBrandId(String brandId);

    BrandTemplateEdit selectOneByManyTerm(@Param("appId") String appId,
                                          @Param("templateNum") String templateNum,
                                          @Param("templateSign") Integer templateSign);

    int resetById(@Param("id") Integer id,@Param("bigOpen")Boolean bigOpen);

    int startOrOpenById(Integer id,Integer bigOpen);

    int distribution(List<BrandTemplateEdit> list);

    int deleteByBrandId(String id);

    int addToDoUpdateRoot(List<BrandTemplateEdit> list);

    int deleteToDoUpdataRoot(Map<String,Object> map);

    int updateToDoUpdataRoot(Map<String,Object> map);
}