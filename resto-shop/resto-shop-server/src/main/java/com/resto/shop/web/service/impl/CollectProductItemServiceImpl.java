package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.dao.CollectProductItemMapper;
import com.resto.shop.web.model.CollectProductItem;
import com.resto.shop.web.service.CollectProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/8/17/0017 11:43
 * @Description:
 */
@Component
@Service
public class CollectProductItemServiceImpl implements CollectProductItemService {

    @Autowired
    CollectProductItemMapper collectProductItemMapper;

    @Override
    public int insert(CollectProductItem record) {
        return collectProductItemMapper.insert(record);
    }

    @Override
    public int insertSelective(CollectProductItem record) {
        return collectProductItemMapper.insertSelective(record);
    }
}
