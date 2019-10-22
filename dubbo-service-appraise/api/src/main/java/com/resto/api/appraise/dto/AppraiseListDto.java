package com.resto.api.appraise.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/4/3.
 */
@Data
public class AppraiseListDto implements Serializable {

    private static final long serialVersionUID = 8311873366616334236L;

    private String brandId;

    private String shopId;

    private String appraiseId;

    private String brandName;

    private String shopName;

    private String serialNumber;

    private String nickName;

    private Integer level;

    private String content;

    private Integer status;

    private BigDecimal redMoney;

    private String createTime;

    private Integer delayAppraiseMoneyTime;

    private String finishTime;
}
