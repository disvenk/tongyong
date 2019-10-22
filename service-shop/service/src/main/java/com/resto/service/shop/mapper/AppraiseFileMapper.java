package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.AppraiseFile;

import java.util.List;

public interface AppraiseFileMapper extends BaseDao<AppraiseFile,String> {

    int insert(AppraiseFile appraiseFile);

    int updateByPrimaryKey(AppraiseFile appraiseFile);

    List<AppraiseFile> appraiseFileList(String appraiseId);
}
