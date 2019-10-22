package com.resto.service.shop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.resto.api.brand.define.api.BrandApiBoOrderRemark;
import com.resto.api.brand.dto.BoOrderRemarkDto;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.OrderRemark;
import com.resto.service.shop.mapper.OrderRemarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderRemarkService extends BaseService<OrderRemark, String> {

	@Autowired
	private OrderRemarkMapper orderRemarkMapper;

    @Autowired
    private BrandApiBoOrderRemark boBrandApiOrderRemark;

	public BaseDao<OrderRemark, String> getDao() {
		return orderRemarkMapper;
	}

    public List<JSONObject> getShopOrderRemark(String shopId) {
        List<JSONObject> objectList = new ArrayList<>();
        //List<OrderRemark> shopOrderRemarks = orderRemarkMapper.selectOrderRemarkByShopId(shopId);
        List<BoOrderRemarkDto> brandOrderRemarks = boBrandApiOrderRemark.selectOrderRemarks();
        /*for (BoOrderRemarkDto boOrderRemark : brandOrderRemarks){
            if (!shopOrderRemarks.isEmpty()){
                for (OrderRemark orderRemark : shopOrderRemarks){
                    if (orderRemark.getBoOrderRemarkId().equalsIgnoreCase(boOrderRemark.getId())){
                        objectList.add(JSON.parseObject(JSON.toJSONString(boOrderRemark)));
                        break;
                    }
                }
            }else{
                break;
            }
        }*/
        return objectList;
    }

}
