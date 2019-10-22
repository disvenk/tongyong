package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.dto.MeiTuanOrderDto;
import com.resto.shop.web.dao.PlatformOrderExtraMapper;
import com.resto.shop.web.model.PlatformOrderExtra;
import com.resto.shop.web.service.PlatformOrderExtraService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Component
@Service
public class PlatformOrderExtraServiceImpl extends GenericServiceImpl<PlatformOrderExtra, String> implements PlatformOrderExtraService {

    @Resource
    private PlatformOrderExtraMapper platformorderextraMapper;

    @Override
    public GenericDao<PlatformOrderExtra, String> getDao() {
        return platformorderextraMapper;
    }

    @Override
    public List<PlatformOrderExtra> selectByPlatformOrderId(String platformOrderId) {
        return platformorderextraMapper.selectByPlatformOrderId(platformOrderId);
    }

    @Override
    public void meituanOrderExtra(MeiTuanOrderDto orderDto) {
        String orderId = orderDto.getOrderId();
        JSONArray jsonArray = JSON.parseArray(orderDto.getExtras());
        for (int index=0;index < jsonArray.size();index++){
            JSONObject object = jsonArray.getJSONObject(index);
            if(StringUtils.isNotEmpty(object.getString("remark"))){
                platformorderextraMapper.insertSelective(meituanConvertToPlatformOrderExtra(orderId,object));
            }
        }
        JSONObject shippingFee = new JSONObject();
        shippingFee.put("remark","配送费用");
        shippingFee.put("reduce_fee",orderDto.getShippingFee());
        platformorderextraMapper.insertSelective(meituanConvertToPlatformOrderExtra(orderId,shippingFee));
    }

    public PlatformOrderExtra meituanConvertToPlatformOrderExtra(String orderId,JSONObject object){
        PlatformOrderExtra  platformOrderExtra = new PlatformOrderExtra();
        platformOrderExtra.setId(ApplicationUtils.randomUUID());
        platformOrderExtra.setPlatformOrderId(orderId);
        platformOrderExtra.setQuantity(1);
        if(object.containsKey("rider_fee")){//如果包含 rider_fee（骑士配送费），则额外添加此服务费（骑手应付款，只对美团配送线上支付线下结算的商家有效）
            platformOrderExtra.setName("美团配送费");
            platformOrderExtra.setPrice(new BigDecimal(Double.toString(object.getDoubleValue("rider_fee"))));
        }else{
            platformOrderExtra.setName(object.getString("remark"));
            platformOrderExtra.setPrice(new BigDecimal(Double.toString(object.getDoubleValue("reduce_fee"))));
        }
        return platformOrderExtra;
    }
}
