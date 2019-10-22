package com.resto.service.appraise.service.impl.service;


import com.resto.api.appraise.entity.AppraiseStep;
import com.resto.conf.mybatis.base.BaseService;
import com.resto.service.appraise.mapper.AppraiseStepMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class AppraiseStepService extends BaseService<AppraiseStep, AppraiseStepMapper> {

    @Resource
    private AppraiseStepMapper appraisestepMapper;


    public List<AppraiseStep> selectByAppraiseId(Long appraiseId) {
        return appraisestepMapper.selectByAppraiseId(appraiseId);
    }
}
