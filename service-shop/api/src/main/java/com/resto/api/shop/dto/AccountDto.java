package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountDto implements Serializable {

    private static final long serialVersionUID = -8220463511815263645L;

    private String id;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal remain;

    @ApiModelProperty(value = "账户状态")
    private Byte status;

    @ApiModelProperty(value = "冻结余额")
    private BigDecimal frozenRemain;

    @ApiModelProperty(value = "用于保存 用户的交易明细")
    private List<AccountLogDto> accountLogs;

}