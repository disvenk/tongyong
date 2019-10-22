package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.RedConfigMapper;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.RedConfig;
import com.resto.shop.web.service.RedConfigService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 *
 */
@Component
@Service
public class RedConfigServiceImpl extends GenericServiceImpl<RedConfig, Long> implements RedConfigService {

    @Resource
    private RedConfigMapper redconfigMapper;

    @Override
    public GenericDao<RedConfig, Long> getDao() {
        return redconfigMapper;
    }

	@Override
	public List<RedConfig> selectListByShopId(String shopId) {
		return redconfigMapper.selectListByShopId(shopId);
	}

	@Override
	public BigDecimal nextRedAmount(Order order) {
		List<RedConfig> configList = selectListByShopId(order.getShopDetailId());
		if(configList.size()==1){
			RedConfig config = configList.get(0); 
			BigDecimal money = order.getOrderMoney();
			if(order.getAmountWithChildren()!=null&&order.getAmountWithChildren().compareTo(money)==1){
				money = order.getAmountWithChildren();
			}
			
			boolean addRatio = config.getIsAddRatio()==1;
			
			int minRatio = config.getMinRatio();
			int maxRatio = config.getMaxRatio();
			
			Random r = new Random();
			//新增需求要返全单，如果最小范围=最大范围说明想要返全单所以直接赋个100  2017/09/23 wtl修改
			double finalRatio = 100;
			if (maxRatio > minRatio ){
				finalRatio = r.nextInt(maxRatio-minRatio)+minRatio;
			}
			money =money.multiply(new BigDecimal(finalRatio/100));
			BigDecimal maxSingle = config.getMaxSingleRed();
			BigDecimal minSingle = config.getMinSignleRed();
			BigDecimal minMoney = new BigDecimal(0.01);
			if(addRatio){
				money = money.add(minSingle);
			}
			if(money.compareTo(maxSingle)>0){
				money = maxSingle;
			}else if(money.compareTo(minMoney)<0){
				money = minMoney;
			}
			
			return money.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return BigDecimal.ZERO;
	}
}
