package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.AppraiseFile;
import com.resto.service.shop.mapper.AppraiseFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraiseFileService extends BaseService<AppraiseFile, String> {

    @Autowired
    private AppraiseFileMapper appraiseFileMapper;

    @Override
    public BaseDao<AppraiseFile, String> getDao() {
        return appraiseFileMapper;
    }

    public List<AppraiseFile> appraiseFileList(String appraiseId) {
        return appraiseFileMapper.appraiseFileList(appraiseId);
    }

}
