package com.resto.shop.web.report;


import com.resto.shop.web.model.ChargePayment;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ChargePaymentMapperReport{

    List<ChargePayment> selectPayList(@Param("begin")Date begin, @Param("end")Date end);


}
