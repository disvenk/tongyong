package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.shop.web.dao.UnitMapper;
import com.resto.shop.web.model.Unit;
import com.resto.shop.web.model.UnitArticle;
import com.resto.shop.web.model.UnitDetail;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.service.UnitService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KONATA on 2016/9/11.
 */
@Component
@Service
public class UnitServiceImpl extends GenericServiceImpl<Unit, String> implements UnitService {

    @Autowired
    private PosService posService;

    @Autowired
    private UnitMapper unitMapper;

    @Override
    public GenericDao<Unit, String> getDao() {
        return unitMapper;
    }

    @Override
    public List<Unit> getUnits(String shopId) {
        return unitMapper.getUnits(shopId);
    }

    @Override
    public List<Unit> getUnitsByArticleId(String shopId, String articleId) {
        return unitMapper.getUnitsByArticleId(shopId, articleId);
    }

    @Override
    public Unit insertDetail(Unit unit) {
        for (UnitDetail unitDetail : unit.getDetails()) {
            String detailId = ApplicationUtils.randomUUID();
            unitDetail.setId(detailId);
                unitMapper.insertDetail(unit.getId(), unitDetail);
        }


        return  unit;

    }

    @Override
    public Unit getUnitById(String id) {
        return unitMapper.getUnitById(id);
    }

    @Override
    public void initUnit(Unit unit) {
        unitMapper.deleteDetail(unit.getId());
    }

    @Override
    public List<Unit> getUnitByArticleid(String articleId) {
        List<Unit> listResult = new ArrayList();
        List<Unit> list = unitMapper.getUnitByArticleid(articleId);
        if(list.size() > 0){
            for (Unit unit : list) {
                if(unit != null){
                    listResult.add(unit);
                }
            }
        }
        return listResult;
    }

    @Override
    public List<Unit> getUnitByArticleidWechat(String articleId) {
        List<Unit> units = unitMapper.getUnitByArticleidWechat(articleId);
        return units;
    }

    @Override
    public void insertArticleRelation(String articleId, List<Unit> units,String brandId,String shopId) {
        for(Unit unit : units){
            String id = ApplicationUtils.randomUUID();
            unit.setChoiceType(unit.getChoiceType() == null ? 1 : unit.getChoiceType());
            unitMapper.insertArticleRelation(articleId,id,unit);
            //消息通知newpos后台发生变化
            ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
            shopMsgChangeDto.setBrandId(brandId);
            shopMsgChangeDto.setShopId(shopId);
            shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEUNITNEW);
            shopMsgChangeDto.setType("add");
            shopMsgChangeDto.setId(id);
            try{
                posService.shopMsgChange(shopMsgChangeDto);
            }catch(Exception e){
                e.printStackTrace();
            }
            for(UnitDetail unitDetail : unit.getDetails()){
                String detailID = ApplicationUtils.randomUUID();
                unitMapper.insertUnitDetailRelation(detailID,id,unitDetail);
                //消息通知newpos后台发生变化
                ShopMsgChangeDto yshopMsgChangeDto=new ShopMsgChangeDto();
                yshopMsgChangeDto.setBrandId(brandId);
                yshopMsgChangeDto.setShopId(shopId);
                yshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEUNITDETAIL);
                yshopMsgChangeDto.setType("add");
                yshopMsgChangeDto.setId(unitDetail.getId());
                try{
                    posService.shopMsgChange(shopMsgChangeDto);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateArticleRelation(String articleId, List<Unit> units,String brandId,String shopId) {
        unitMapper.deleteArticleUnit(articleId);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(brandId);
        shopMsgChangeDto.setShopId(shopId);
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEUNITNEW);
        shopMsgChangeDto.setType("delete");
        shopMsgChangeDto.setId(articleId);
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        insertArticleRelation(articleId, units,brandId,shopId);
    }

    @Override
    public void deleteUnit(String id) {
        List<String> ids = unitMapper.getUnitNew(id);
        if(ids!=null && !ids.isEmpty()){
            unitMapper.deleteUnit(ids);
        }
        unitMapper.deleteUnitNew(id);

    }

    @Override
    public void modifyUnit(Unit unit,String brandId,String shopId) {
        //首先得到使用该规格的菜品关系列表
        List<String> ids = unitMapper.getUnitNew(unit.getId());

        for(String id: ids){
            //删除多余的规格
            unitMapper.deleteUnitMore(id,unit.getDetails());
            //遍历每个菜品关系
            for(UnitDetail unitDetail : unit.getDetails()){
                //拿到每个明细
                int count = unitMapper.getUnitByRelation(unitDetail.getId(),id);
                if(count == 0){ //不存在
                    unitDetail.setPrice(new BigDecimal(0));
                    unitMapper.insertUnitDetailRelation(ApplicationUtils.randomUUID(),id,unitDetail);
                    //消息通知newpos后台发生变化
                    ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
                    shopMsgChangeDto.setBrandId(brandId);
                    shopMsgChangeDto.setShopId(shopId);
                    shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBUNITDETAIL);
                    shopMsgChangeDto.setType("add");
                    shopMsgChangeDto.setId(unitDetail.getId());
                    try{
                        posService.shopMsgChange(shopMsgChangeDto);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public List<UnitArticle> selectUnitDetail(String id) {
        return unitMapper.selectUnitDetail(id);
    }

    @Override
    public List<Unit> selectUnitByShopId(String shopId) {
        return unitMapper.selectUnitByShopId(shopId);
    }

    @Override
    public List<UnitDetail> selectUnitDetailByShopId(String shopId) {
        return unitMapper.selectUnitDetailByShopId(shopId);
    }

    @Override
    public List<Unit> getUnitsPackage() {
        return unitMapper.getUnitsPackage();
    }

    @Override
    public Unit getAnUnitsPackage(String unitId) {
        return unitMapper.getAnUnitsPackage(unitId);
    }

    @Override
    public List<Unit> getBrandUnits(String currentShopId) {
        return unitMapper.getBrandUnits(currentShopId);
    }
}
