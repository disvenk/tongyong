package com.resto.shop.web.report;


import com.resto.brand.web.dto.RedPacketDto;
import com.resto.shop.web.model.RedPacket;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RedPacketMapperReport{

    List<RedPacketDto> selectRedPacketLog(Map<String, Object> selectMap);

    Map<String, BigDecimal> selectUseRedOrder(Map<String, Object> selectMap);
}
