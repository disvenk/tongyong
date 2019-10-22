package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.WeItemMapper;
import com.resto.shop.web.model.WeItem;
import com.resto.shop.web.service.WeItemService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class WeItemServiceImpl extends GenericServiceImpl<WeItem, Long> implements WeItemService {

    @Resource
    private WeItemMapper weitemMapper;

    @Override
    public GenericDao<WeItem, Long> getDao() {
        return weitemMapper;
    }

    @Override
    public List<WeItem> selectByShopIdAndTime(String beginTime, String shopId) {
//        Date beginDate = DateUtil.getformatBeginDate(beginTime);
//        Date endDate = DateUtil.getformatEndDate(beginTime);
//        Date begin = DateUtil.fomatDate(beginTime);
      //  return weitemMapper.selectByShopIdAndTime(beginDate,endDate,shopId);
        return  weitemMapper.selectByShopIdAndTime(beginTime,shopId);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
            weitemMapper.deleteByIds(ids);
    }
}
