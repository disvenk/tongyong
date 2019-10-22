package com.resto.service.appraise.service.impl.service;

import com.resto.api.appraise.entity.AppraiseGrade;
import com.resto.conf.mybatis.base.BaseService;
import com.resto.service.appraise.mapper.AppraiseGradeMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class AppraiseGradeService extends BaseService<AppraiseGrade, AppraiseGradeMapper> {

    @Resource
    private AppraiseGradeMapper appraisegradeMapper;

    public List<AppraiseGrade> selectByAppraiseId(Long appraiseId) {
        return appraisegradeMapper.selectByAppraiseId(appraiseId);
    }
}
