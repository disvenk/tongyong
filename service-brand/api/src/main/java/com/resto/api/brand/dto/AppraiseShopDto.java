package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 店铺评论  （用于报表）
 *
 */
@Data
public class AppraiseShopDto implements Serializable{

	private static final long serialVersionUID = -3332960258405501013L;

	@ApiModelProperty(value = "评论等级名称")
	private String levelName;

	@ApiModelProperty(value = "评论等级")
    private Integer level;

	@ApiModelProperty(value = "手机号")
	private String telephone;

	@ApiModelProperty(value = "订单金额")
	private BigDecimal orderMoney;

	@ApiModelProperty(value = "评论金额")
	private BigDecimal redMoney;

	@ApiModelProperty(value = "评论对象")
	private String feedBack;

	@ApiModelProperty(value = "评论内容")
	private String content;

	@ApiModelProperty(value = "评论时间")
	private String createTime;

	@ApiModelProperty(value = "桌号")
	private Integer tablenumber;//

	@ApiModelProperty(value = "区域")
	private String areaname;

    private List<Map<String, Object>> appraiseShopDtos;

}
