package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ArticleUnit;
import com.resto.shop.web.model.ArticleUnitDetail;
import com.resto.shop.web.model.ArticleUnitNew;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleUnitMapper extends GenericDao<ArticleUnit,Integer>{
    int deleteByPrimaryKey(Integer id);

    int insert(ArticleUnit record);

    int insertSelective(ArticleUnit record);

    ArticleUnit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticleUnit record);

    int updateByPrimaryKey(ArticleUnit record);
    
    List<ArticleUnit> selectListByAttrId(@Param(value = "attrId") Integer attrId);
    
    /**
     * 根据 属性 ID 删除 规格信息(假删，修改状态)
     * @param id
     */
    void deleteByAttrId(Integer id);

    int insertByAuto(ArticleUnit articleUnit);

    ArticleUnit selectSame(@Param("name") String name,@Param("attrId") String attrId);

    /**
     * 根据 店铺ID 查询店铺下的所有     ArticleUnit     数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<ArticleUnit> selectByShopId(@Param("shopId") String shopId);

    /**
     * 根据 店铺ID 查询店铺下的所有     ArticleUnitDetail     数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<ArticleUnitDetail> selectArticleUnitDetailByShopId(@Param("shopId") String shopId);

    /**
     * 根据 店铺ID 查询店铺下的所有     ArticleUnitNew     数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<ArticleUnitNew> selectArticleUnitNewByShopId(@Param("shopId") String shopId);
}