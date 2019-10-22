package com.resto.service.appraise.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.api.appraise.entity.AppraiseStep;

import java.util.List;

public interface AppraiseStepMapper  extends MyMapper<AppraiseStep> {

    List<AppraiseStep> selectByAppraiseId(Long appraiseId);
}
