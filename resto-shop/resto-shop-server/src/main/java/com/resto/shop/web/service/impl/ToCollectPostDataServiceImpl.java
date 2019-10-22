package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.shop.web.dao.CollectPosMapper;
import com.resto.shop.web.model.CollectPos;
import com.resto.shop.web.service.ToCollectPostDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Service
public class ToCollectPostDataServiceImpl implements ToCollectPostDataService {

    @Autowired
    CollectPosMapper collectPosMapper;



    @Override
    public List<CollectPos> selectFailureOrders(Date staDate, Date endDate,String shopId) {
        List<CollectPos> collectPos = collectPosMapper.selectFailureOrders(staDate,endDate,shopId);

        return collectPos;
    }

    @Override
    public void updateFailure(CollectPos collectPo) {
        collectPosMapper.updateByPrimaryKey(collectPo);
    }
}
