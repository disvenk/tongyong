package com.resto.shop.web.service;

import com.resto.shop.web.model.CollectPos;

public interface CollectPosService {

    void insertSelective(CollectPos collectPos);

    int insert(CollectPos record);

}
