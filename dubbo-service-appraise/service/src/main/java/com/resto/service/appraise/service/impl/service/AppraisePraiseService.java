package com.resto.service.appraise.service.impl.service;

import com.resto.api.appraise.entity.AppraisePraise;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.appraise.mapper.AppraisePraiseMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by carl on 2016/11/20.
 */
@Service
public class AppraisePraiseService extends BaseServiceResto<AppraisePraise, AppraisePraiseMapper> {

    @Resource
    private AppraisePraiseMapper appraisePraiseMapper;
    
    public AppraisePraise updateCancelPraise( String appraiseId, String customerId, Integer isDel) {
        appraisePraiseMapper.updateCancelPraise(appraiseId, customerId, isDel);
        AppraisePraise appraisePraise = appraisePraiseMapper.selectByAppraiseIdCustomerId(appraiseId, customerId);
        return appraisePraise;
    }
    
    public AppraisePraise updateCancelPraise( @RequestBody AppraisePraise appraisePraise) {
        appraisePraiseMapper.insertSelective(appraisePraise);
        return appraisePraise;
    }
    
    public List<AppraisePraise> appraisePraiseList( String appraiseId) {
        return appraisePraiseMapper.appraisePraiseList(appraiseId);
    }
    
    public AppraisePraise selectByAppraiseIdCustomerId( String appraiseId, String customerId) {
        return appraisePraiseMapper.selectByAppraiseIdCustomerId(appraiseId, customerId);
    }
    
    public int selectByCustomerCount( String customerId) {
        return appraisePraiseMapper.selectByCustomerCount(customerId);
    }

}
