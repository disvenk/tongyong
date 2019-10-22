package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.AritclekitchenPrinterDtoMapper;
import com.resto.shop.web.dao.KitchenGroupDetailMapper;
import com.resto.shop.web.dao.KitchenGroupMapper;
import com.resto.shop.web.dao.KitchenMapper;
import com.resto.shop.web.model.Kitchen;
import com.resto.shop.web.model.KitchenGroup;
import com.resto.shop.web.model.KitchenGroupDetail;
import com.resto.shop.web.service.KitchenGroupService;
import com.resto.shop.web.service.PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class KitchenGroupServiceImpl extends GenericServiceImpl<KitchenGroup, Integer> implements KitchenGroupService {

    @Resource
    private KitchenGroupMapper kitchengroupMapper;

    @Autowired
    private KitchenMapper kitchenMapper;

    @Autowired
    private PosService posService;

    @Autowired
    private AritclekitchenPrinterDtoMapper aritclekitchenPrinterDtoMapper;
    @Autowired
    private KitchenGroupDetailMapper kitchenGroupDetailMapper;
    @Override
    public GenericDao<KitchenGroup, Integer> getDao() {
        return kitchengroupMapper;
    }

    @Override
    public List<KitchenGroup> selectKitchenGroupByShopDetailId(String shopDetailId) {
        return kitchengroupMapper.selectByShopDetailId(shopDetailId);
    }

    @Override
    public List<KitchenGroup> selectKitchenGroupByShopDetailId(String brandId, String shopDetailId) {
        List<KitchenGroup> kitchenGroups = kitchengroupMapper.selectByShopDetailId(shopDetailId);
        if (kitchenGroups != null && kitchenGroups.size()>0){
            for(KitchenGroup kitchenGroup: kitchenGroups){
                if (kitchenGroup.getStatus()!=1){
                    kitchenGroup.setStatusName("开启");
                }else{
                    kitchenGroup.setStatusName("未开启");
                }
                List<Kitchen> kitchens = kitchenGroupDetailMapper.selectByGroupIdAndShopDeailId(kitchenGroup.getId(), shopDetailId);
                if (kitchens !=null){
                    StringBuffer sb = new StringBuffer();
                   for (int i = 0 ;  i < kitchens.size(); i++){
                       if(i==kitchens.size()-1){
                           sb.append(kitchens.get(i).getName());
                       }else{
                           sb.append(kitchens.get(i).getName()+",");
                       }
                   }
                   kitchenGroup.setKitchenName(sb.toString());
                }
                kitchenGroup.setKitchens(kitchens);
            };
        }
        return kitchenGroups;
    }

    @Override
    public List<KitchenGroup> selectKitchenGroupByShopDetailIdByStatus(String shopDetailId) {
        return kitchengroupMapper.selectByShopDetailIdByStatus(shopDetailId);
    }

    @Override
    public void insertSelective(KitchenGroup kitchenGroup) {
        kitchenGroup.setCreateTime(new Date());
        kitchengroupMapper.insertSelective(kitchenGroup);
        this.insertKitchenGroupDetail(kitchenGroup);
    }

    @Override
    public void insertKitchenGroupDetail(KitchenGroup kitchenGroup) {
        KitchenGroupDetail kitchenGroupDetail = new KitchenGroupDetail();
        kitchenGroupDetail.setBrandId(kitchenGroup.getBrandId());
        kitchenGroupDetail.setKitchenGroupId(kitchenGroup.getId());
        kitchenGroupDetail.setShopDetailId(kitchenGroup.getShopDetailId());
        List<Integer> kitchenIds = kitchenGroup.getKitchenIds();
        if (kitchenIds !=null){
            kitchenIds.forEach(kitchenId ->{
                kitchenGroupDetail.setKitchenId(kitchenId);
                kitchenGroupDetailMapper.insertSelective(kitchenGroupDetail);
            });
        }
    }

    @Override
    public int update(KitchenGroup kitchenGroup) {
        kitchenGroupDetailMapper.deleteByShopDetailIdAndKitchenGroupId(kitchenGroup.getShopDetailId(),kitchenGroup.getId());
        this.insertKitchenGroupDetail(kitchenGroup);
        return super.update(kitchenGroup);
    }

    @Override
    public int delete(String shopDetailId,Integer id) {
        kitchenGroupDetailMapper.deleteByShopDetailIdAndKitchenGroupId(shopDetailId,id);
        aritclekitchenPrinterDtoMapper.deleteByKitchenGroupId(id,shopDetailId);
        return super.delete(id);
    }
}
