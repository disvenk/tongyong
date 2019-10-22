package com.resto.shop.web.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class MenuArticle implements Serializable{

    private static final long serialVersionUID = 7067000980650566940L;

    private Long id;

    private String articleId;

    private Long menuId;

    private Integer serialNumber;

    private String brandId;

    private Integer distributionModeId;

    private String initials;

    private String articleFamilyId;

    private Integer articleType;

    private String name;

    private String description;

    private Byte photoType;

    private String photoSmall;

    private String photoLittle;

    private String photoSuper;

    private String photoSquareOriginal;

    private String gifUrl;

    private BigDecimal price;

    private BigDecimal fansPrice;

    private BigDecimal shopPrice;

    private Integer openCatty;

    private BigDecimal cattyMoney;

    private Integer inventoryWarning;

    private Integer stockWorkingDay;

    private Integer stockWeekend;

    private Integer sort;

    private Date createTime;

    private String unit;

    private Boolean showBig;

    private Boolean showDesc;

    private String controlColor;

    private Integer mealFeeNumber;

    private String recommendId;

    private String unitId;

    private Long weightPackageId;

    private String recommendationDegree;

    private String mealOutTime;

    private String articleKind;

    private String articleLabel;

    private String articleHot;

    private String articleComponent;

    //规格属性
    private List<ArticleUnitNew> units;
    //套餐属性
    private List<MealAttr> mealAttrs;
    //供应时间
    private Integer[] supportTimes;
}