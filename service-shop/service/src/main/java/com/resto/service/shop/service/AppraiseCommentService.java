package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.AppraiseComment;
import com.resto.service.shop.mapper.AppraiseCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraiseCommentService extends BaseService<AppraiseComment, String>{

    @Autowired
    private AppraiseCommentMapper appraiseCommentMapper;

    @Override
    public BaseDao<AppraiseComment, String> getDao() {
        return appraiseCommentMapper;
    }

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
