package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.Participant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
public interface ParticipantMapper extends GenericDao<Participant, Long> {

    List<Participant> selectCustomerListByGroupIdOrderId(@Param("groupId") String groupId, @Param("orderId") String orderId);

    Participant selectByOrderIdCustomerId(@Param("orderId") String orderId, @Param("customerId") String customerId);

    int updateAppraiseByOrderIdCustomerId(@Param("orderId") String orderId, @Param("customerId") String customerId);

    List<Participant> selectNotAppraiseByGroupId(@Param("groupId") String groupId, @Param("orderId") String orderId);

    void updateIsPayByOrderIdCustomerId(@Param("groupId") String groupId, @Param("orderId") String orderId, @Param("customerId") String customerId);

    List<String> selectCustomerIdByGroupId(String groupId);

}
