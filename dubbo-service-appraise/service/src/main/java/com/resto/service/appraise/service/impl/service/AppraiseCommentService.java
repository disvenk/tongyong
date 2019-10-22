package com.resto.service.appraise.service.impl.service;

import com.resto.api.appraise.entity.AppraiseComment;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.appraise.mapper.AppraiseCommentMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by carl on 2016/11/20.
 */
@Service
public class AppraiseCommentService extends BaseServiceResto<AppraiseComment, AppraiseCommentMapper> {

    @Resource
    private AppraiseCommentMapper appraiseCommentMapper;

    public List<AppraiseComment> appraiseCommentList(String appraiseId) {
        return appraiseCommentMapper.appraiseCommentList(appraiseId);
    }

    public AppraiseComment insertComment(AppraiseComment appraiseComment) {
        appraiseCommentMapper.insertSelective(appraiseComment);
        return appraiseComment;
    }

    public int selectByCustomerCount(String customerId) {
        return appraiseCommentMapper.selectByCustomerCount(customerId);
    }

}
