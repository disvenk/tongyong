package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.TableCode;
import com.resto.service.shop.mapper.TableCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class TableCodeService extends BaseService<TableCode, String> {

    @Autowired
    private TableCodeMapper tablecodeMapper;

    public BaseDao<TableCode, String> getDao() {
        return tablecodeMapper;
    }

}
