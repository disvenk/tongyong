package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 品牌菜品销售  （用于报表）
 * @author lmx
 *
 */
@Data
public class brandArticleReportDto implements Serializable {

    private static final long serialVersionUID = 4214300668062037584L;

    @ApiModelProperty(value = "品牌名称")
	private String brandName;

    @ApiModelProperty(value = "菜品总销量")
	private int totalNum;

    @ApiModelProperty(value = "菜品总销售额")
    private BigDecimal sellIncome;

    @ApiModelProperty(value = "退菜总数")
    private  Integer refundCount;

    @ApiModelProperty(value = "退菜金额")
    private  BigDecimal refundTotal;//

    @ApiModelProperty(value = "折扣总额")
    private BigDecimal discountTotal;
	
}
