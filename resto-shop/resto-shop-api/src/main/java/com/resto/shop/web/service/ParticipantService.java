package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.Participant;

import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
public interface ParticipantService extends GenericService<Participant, Long> {

    List<Participant> selectCustomerListByGroupIdOrderId(String groupId, String orderId);

    Participant selectByOrderIdCustomerId(String orderId, String customerId);

    void updateAppraiseByOrderIdCustomerId(String orderId, String customerId);

    List<Participant> selectNotAppraiseByGroupId(String groupId, String orderId);

    void updateIsPayByOrderIdCustomerId(String groupId, String orderId, String customerId);

    List<String> selectCustomerIdByGroupId(String groupId);

}
