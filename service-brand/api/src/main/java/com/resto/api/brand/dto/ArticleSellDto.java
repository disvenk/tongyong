package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 菜品销售报表
 * @author lmx
 *
 */
@Data
public class ArticleSellDto implements Serializable{

	private static final long serialVersionUID = 3042520688392255570L;

	@ApiModelProperty(value = "菜品ID")
	private String articleId;

	@ApiModelProperty(value = "菜品名称")
	private String articleName;

	@ApiModelProperty(value = "菜品分类ID")
	private String articleFamilyId;

	@ApiModelProperty(value = "菜品分类名称")
	private String articleFamilyName;

	@ApiModelProperty(value = "店铺销售数量")
	private Integer shopSellNum;

	@ApiModelProperty(value = "品牌销售数量")
	private Integer brandSellNum;

	@ApiModelProperty(value = "销售占比")
	private String salesRatio;

	@ApiModelProperty(value = "销量占比")
	private String numRatio;

	@ApiModelProperty(value = "销售额")
	private BigDecimal salles;

	@ApiModelProperty(value = "点赞数")
	private Integer likes;

	@ApiModelProperty(value = "餐品的类型  是为了计算套餐  3  是套餐")
	private Integer  type;

	@ApiModelProperty(value = "店铺ID")
	private String shopId;

	@ApiModelProperty(value = "折扣金额")
	private BigDecimal discountMoney;

	@ApiModelProperty(value = "新增菜品编号")
    private  String numberCode;

	@ApiModelProperty(value = "菜品类型名称 type=3是套餐 其他事单品  主要是下载报表数据好用")
    private  String typeName;

	@ApiModelProperty(value = "退菜数量")
    private  Integer refundCount;

	@ApiModelProperty(value = "退菜金额  unit_price * refund_count")
    private  BigDecimal refundTotal;

    private Map<String, Object> brandReport;

    private List<Map<String, Object>> brandArticleUnit;

    private List<Map<String, Object>> brandArticleFamily;

    private List<Map<String, Object>> shopArticleUnit;

    private List<Map<String, Object>> shopArticleFamily;

    private List<Map<String, Object>> brandArticleType;

    private List<Map<String, Object>> shopArticleType;

	private List<Map<String, Object>> shopMealSeals;

	private List<Map<String, Object>> brandMealSeals;

}
