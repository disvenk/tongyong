package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.ChargeLog;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.ShopDetail;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.customer.mapper.AccountMapper;
import com.resto.service.customer.mapper.ChargeLogMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class ChargeLogService extends BaseServiceResto<ChargeLog, ChargeLogMapper> {

}
