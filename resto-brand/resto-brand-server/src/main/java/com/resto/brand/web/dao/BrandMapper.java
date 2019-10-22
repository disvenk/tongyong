package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BrandMapper extends GenericDao<Brand,String> {
    int deleteByPrimaryKey(String id);

    int insert(Brand record);

    int insertSelective(Brand record);

    Brand selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Brand record);

    int updateByPrimaryKey(Brand record);

    /**
     * 查询 品牌状态为1的品牌详细信息，包含该品牌的数据库配置和微信配置
     * @return
     */
    List<Brand> selectBrandDetailInfo();

    //查询 品牌 ID 和 Name
    List<Brand> selectIdAndName();

    Brand selectBySign(String brandSign);

    /**
     * 验证参数是否重复
     * @param paramName
     * @param paramValue
     * @return
     */
    int validataParam(@Param("paramName") String paramName, @Param("paramValue")String paramValue);


    /**
     * 给指定品牌充值短信数量
     * @param brandId
     * @param number
     * @return
     */
    int chargeSms(@Param("brandId")String brandId,@Param("number")int number);

    Brand selectBrandBySetting(String id);

    int definedById(Map<String,Object> map);

}