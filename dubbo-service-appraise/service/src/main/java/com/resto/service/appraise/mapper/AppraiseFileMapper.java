package com.resto.service.appraise.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.api.appraise.entity.AppraiseFile;

import java.util.List;

public interface AppraiseFileMapper extends MyMapper<AppraiseFile> {

    List<AppraiseFile> appraiseFileList(String appraiseId);

}
