package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.dao.CollectPosMapper;
import com.resto.shop.web.model.CollectPos;
import com.resto.shop.web.service.CollectPosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Service
public class CollectPosServiceImpl implements CollectPosService {

    @Autowired
    CollectPosMapper collectPosMapper;

    @Override
    public void insertSelective(CollectPos collectPos) {
        collectPosMapper.insertSelective(collectPos);
    }

    @Override
    public int insert(CollectPos record) {
        return collectPosMapper.insert(record);
    }
}
