package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.Unit;
import com.resto.shop.web.model.UnitArticle;
import com.resto.shop.web.model.UnitDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KONATA on 2016/9/11.
 */
public interface UnitMapper extends GenericDao<Unit, String> {

    List<Unit> getUnits(String shopId);


    List<Unit> getUnitsByArticleId(@Param("shopId") String shopId,@Param("articleId") String articleId);

    int insertDetail(@Param("unitId") String unitId, @Param("detail") UnitDetail unitDetail);

//    int insertDetail(@Param("familyId") String familyId, @Param("detail")UnitDetail unitDetail);

    Unit getUnitById(String id);

    int deleteFamily(String id);

    int deleteDetail(String unitId);

    List<Unit> getUnitByArticleid(String articleId);

    List<Unit> getUnitByArticleidWechat(String articleId);

    int insertArticleRelation(@Param("articleId") String articleId,@Param("id") String id,
                              @Param("unit") Unit unit);

    int insertUnitDetailRelation(@Param("id") String id ,@Param("relationId") String relationId,
                                 @Param("detail") UnitDetail unitDetail);

    int deleteArticleUnit(String articleId);

    List<String> getUnitNew(String id);

    void deleteUnitMore(@Param("id") String id,@Param("details") List<UnitDetail> details);

    void deleteUnit(@Param("ids") List<String> ids);

    void deleteUnitNew(@Param("id") String id);

    Integer getUnitByRelation(@Param("detailId") String detailId,@Param("relationId") String relationId);

    List<UnitArticle> selectUnitDetail(String id);

    /**
     * 根据 店铺ID 查询店铺下的所有  Unit  数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<Unit> selectUnitByShopId(@Param("shopId") String shopId);

    /**
     * 根据 店铺ID 查询店铺下的所有  UnitDetail  数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<UnitDetail> selectUnitDetailByShopId(String shopId);

    List<Unit> getUnitsPackage();

    Unit getAnUnitsPackage(@Param("unitId") String unitId);

    UnitDetail selectUnitDetailByKey(@Param("unitDetailId")String unitDetailId);

    List<Unit> getBrandUnits(@Param("shopId")String shopId);
}
