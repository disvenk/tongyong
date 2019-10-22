package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ParticipantMapper;
import com.resto.shop.web.model.Participant;
import com.resto.shop.web.service.ParticipantService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
@Component
@Service
public class ParticipantServiceImpl extends GenericServiceImpl<Participant, Long> implements ParticipantService {

    @Resource
    private ParticipantMapper participantMapper;

    @Override
    public GenericDao<Participant, Long> getDao() {
        return participantMapper;
    }

    @Override
    public List<Participant> selectCustomerListByGroupIdOrderId(String groupId, String orderId) {
        return participantMapper.selectCustomerListByGroupIdOrderId(groupId, orderId);
    }

    @Override
    public Participant selectByOrderIdCustomerId(String orderId, String customerId) {
        return participantMapper.selectByOrderIdCustomerId(orderId, customerId);
    }

    @Override
    public void updateAppraiseByOrderIdCustomerId(String orderId, String customerId) {
        participantMapper.updateAppraiseByOrderIdCustomerId(orderId, customerId);
    }

    @Override
    public List<Participant> selectNotAppraiseByGroupId(String groupId, String orderId) {
        return participantMapper.selectNotAppraiseByGroupId(groupId, orderId);
    }

    @Override
    public void updateIsPayByOrderIdCustomerId(String groupId, String orderId, String customerId) {
        participantMapper.updateIsPayByOrderIdCustomerId(groupId, orderId, customerId);
    }

    @Override
    public List<String> selectCustomerIdByGroupId(String groupId) {
        return participantMapper.selectCustomerIdByGroupId(groupId);
    }
}
