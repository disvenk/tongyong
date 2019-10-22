package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.AppraisePraise;
import com.resto.service.shop.mapper.AppraisePraiseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraisePraiseService extends BaseService<AppraisePraise, String> {

    @Autowired
    private AppraisePraiseMapper appraisePraiseMapper;

    @Override
    public BaseDao<AppraisePraise, String> getDao() {
        return appraisePraiseMapper;
    }

    public AppraisePraise updateCancelPraise(String appraiseId, String customerId, Integer isDel) {
        appraisePraiseMapper.updateCancelPraise(appraiseId, customerId, isDel);
        AppraisePraise appraisePraise = appraisePraiseMapper.selectByAppraiseIdCustomerId(appraiseId, customerId);
        return appraisePraise;
    }

    public AppraisePraise updateCancelPraise(AppraisePraise appraisePraise) {
        appraisePraiseMapper.insertSelective(appraisePraise);
        return appraisePraise;
    }

    public List<AppraisePraise> appraisePraiseList(String appraiseId) {
        return appraisePraiseMapper.appraisePraiseList(appraiseId);
    }

    public AppraisePraise selectByAppraiseIdCustomerId(String appraiseId, String customerId) {
        return appraisePraiseMapper.selectByAppraiseIdCustomerId(appraiseId, customerId);
    }

    public int selectByCustomerCount(String customerId) {
        return appraisePraiseMapper.selectByCustomerCount(customerId);
    }
}
