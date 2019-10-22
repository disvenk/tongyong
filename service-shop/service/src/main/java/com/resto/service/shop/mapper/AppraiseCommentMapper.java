package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.AppraiseComment;

import java.util.List;

/**
 * Created by carl on 2016/11/20.
 */
public interface AppraiseCommentMapper extends BaseDao<AppraiseComment,String> {
    int deleteByPrimaryKey(String id);

    int insert(AppraiseComment appraiseComment);

    int insertSelective(AppraiseComment appraiseComment);

    AppraiseComment selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AppraiseComment appraiseComment);

    int updateByPrimaryKey(AppraiseComment appraiseComment);

    List<AppraiseComment> appraiseCommentList(String appraiseId);

    int selectByCustomerCount(String customerId);
}
