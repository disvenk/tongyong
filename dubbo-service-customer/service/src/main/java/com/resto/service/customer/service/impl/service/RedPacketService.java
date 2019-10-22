package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.constants.RedType;
import com.resto.api.customer.entity.AccountLog;
import com.resto.api.customer.entity.Customer;
import com.resto.api.customer.entity.RedPacket;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dto.RedPacketDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WechatConfig;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.customer.mapper.RedPacketMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
public class RedPacketService extends BaseServiceResto<RedPacket, RedPacketMapper> {

    @Resource
    private RedPacketMapper redPacketMapper;

    public void refundRedPacket(BigDecimal payValue, String Id) {
        redPacketMapper.refundRedPacket(payValue,Id);
    }
}