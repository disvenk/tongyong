package com.resto.service.appraise.service.impl.service;

import com.resto.api.appraise.entity.AppraiseFile;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.appraise.mapper.AppraiseFileMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by carl on 2016/11/20.
 */
@Service
public class AppraiseFileService extends BaseServiceResto<AppraiseFile, AppraiseFileMapper> {

    @Resource
    private AppraiseFileMapper appraiseFileMapper;

    public List<AppraiseFile> appraiseFileList(String appraiseId) {
        List<AppraiseFile> list=appraiseFileMapper.appraiseFileList(appraiseId);
        return list;
    }
}
