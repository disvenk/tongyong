package com.resto.service.appraise.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.api.appraise.entity.AppraiseComment;
import java.util.List;

/**
 * Created by carl on 2016/11/20.
 */
public interface AppraiseCommentMapper extends MyMapper<AppraiseComment> {

    int selectByCustomerCount(String customerId);

    List<AppraiseComment> appraiseCommentList(String appraiseId);
}
