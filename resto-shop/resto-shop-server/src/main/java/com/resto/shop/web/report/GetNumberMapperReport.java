package com.resto.shop.web.report;

import com.resto.brand.web.dto.RedPacketDto;

import java.util.List;
import java.util.Map;

/**
 * Created by carl on 2016/10/14.
 */
public interface GetNumberMapperReport{

    List<RedPacketDto> selectGetNumberRed(Map<String, Object> selectMap);

}
