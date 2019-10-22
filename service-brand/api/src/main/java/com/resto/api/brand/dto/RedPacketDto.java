package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class RedPacketDto implements Serializable {

    private static final long serialVersionUID = 4821882284784624514L;

    private String shopDetailId;

    private String shopName;

    private BigDecimal redCount;

    private BigDecimal redMoney;

    private BigDecimal useRedCount;

    private BigDecimal useRedMoney;

    private String useRedCountRatio;

    private String useRedMoneyRatio;

    private BigDecimal useRedOrderCount;

    private BigDecimal useRedOrderMoney;

    private String redPacketId;

    private List<Map<String, Object>> shopRedInfoList;

    private Map<String, Object> brandRedInfo;

    public RedPacketDto(String shopDetailId, String shopName, BigDecimal redCount, BigDecimal redMoney, BigDecimal useRedCount, BigDecimal useRedMoney, String useRedCountRatio, String useRedMoneyRatio, BigDecimal useRedOrderCount, BigDecimal useRedOrderMoney) {
        this.shopDetailId = shopDetailId;
        this.shopName = shopName;
        this.redCount = redCount;
        this.redMoney = redMoney;
        this.useRedCount = useRedCount;
        this.useRedMoney = useRedMoney;
        this.useRedCountRatio = useRedCountRatio;
        this.useRedMoneyRatio = useRedMoneyRatio;
        this.useRedOrderCount = useRedOrderCount;
        this.useRedOrderMoney = useRedOrderMoney;
    }

}
