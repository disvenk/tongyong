package com.resto.api.brand.dto;


import com.resto.core.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/11.
 */
@Data
public class OrderArticleDto implements Serializable {

    private static final long serialVersionUID = -3945767387490494775L;

    @ApiModelProperty(value = "店铺Id")
    private String shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "下单时间")
    private Date orderTime;

    @ApiModelProperty(value = "手机号码")
    private String telephone;

    @ApiModelProperty(value = "餐品名称")
    private String articleName;

    @ApiModelProperty(value = "餐品数量")
    private String articleNum;
}
