package com.resto.service.appraise.mapper;

import com.resto.api.appraise.entity.AppraiseGrade;
import com.resto.conf.mybatis.util.MyMapper;
import java.util.List;

public interface AppraiseGradeMapper  extends MyMapper<AppraiseGrade> {

    List<AppraiseGrade> selectByAppraiseId(Long appraiseId);
}
