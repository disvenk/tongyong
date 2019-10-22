package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.Unit;
import com.resto.shop.web.model.UnitArticle;
import com.resto.shop.web.model.UnitDetail;

import java.util.List;

/**
 * Created by KONATA on 2016/9/11.
 */
public interface UnitService extends GenericService<Unit, String> {

   List<Unit> getUnits(String shopId);

   List<Unit> getUnitsByArticleId(String shopId,String articleId);

   Unit insertDetail(Unit unit);

   Unit getUnitById(String id);

   void initUnit(Unit unit);

   List<Unit> getUnitByArticleid(String articleId);

   List<Unit> getUnitByArticleidWechat(String articleId);

   void insertArticleRelation(String articleId,List<Unit> units,String brandId,String shopId);

   void updateArticleRelation(String articleId,List<Unit> units,String brandId,String shopId);

   void deleteUnit(String id);

   void modifyUnit(Unit unit,String brandId,String shopId);

   List<UnitArticle> selectUnitDetail(String id);

   /**
    * 根据 店铺ID 查询店铺下的所有  Unit  数据
    * Pos2.0 数据拉取接口			By___lmx
    * @param shopId
    * @return
    */
   List<Unit> selectUnitByShopId(String shopId);

   /**
    * 根据 店铺ID 查询店铺下的所有  UnitDetail  数据
    * Pos2.0 数据拉取接口			By___lmx
    * @param shopId
    * @return
    */
   List<UnitDetail> selectUnitDetailByShopId(String shopId);

   List<Unit> getUnitsPackage();

   Unit getAnUnitsPackage(String unitId);

    List<Unit> getBrandUnits(String currentShopId);
}
