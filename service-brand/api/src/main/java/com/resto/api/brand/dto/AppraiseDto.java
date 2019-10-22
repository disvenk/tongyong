package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 收入统计  （用于报表）
 * @author lmx
 *
 */
@Data
public class AppraiseDto implements Serializable {

	private static final long serialVersionUID = -8389251015582562410L;

	@ApiModelProperty(value = "店铺id")
	private String shopId;

	@ApiModelProperty(value = "品牌名称")
	private String brandName;

	@ApiModelProperty(value = "店铺名称")
	private String shopName;

	@ApiModelProperty(value = "评价单数")
	private int appraiseNum;

	@ApiModelProperty(value = "评价率")
	private String appraiseRatio;

	@ApiModelProperty(value = "红包总额")
	private BigDecimal redMoney;

	@ApiModelProperty(value = "订单总额")
	private BigDecimal totalMoney;

	@ApiModelProperty(value = "红包撬动率")
	private String redRatio;

	@ApiModelProperty(value = "一星")
	private int onestar;

	@ApiModelProperty(value = "二星")
	private int twostar;

	@ApiModelProperty(value = "三星")
	private int threestar;

	@ApiModelProperty(value = "四星")
	private int fourstar;

	@ApiModelProperty(value = "五星")
	private int fivestar;

	@ApiModelProperty(value = "报表下载用,让品牌和店铺名字显示在同一列上")
	private String name;

    private Map<String, Object> brandAppraise;

    private List<Map<String, Object>> shopAppraises;

}
