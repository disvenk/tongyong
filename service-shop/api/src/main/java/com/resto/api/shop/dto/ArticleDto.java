package com.resto.api.shop.dto;

import com.resto.api.brand.dto.PlatformDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ArticleDto implements Serializable{

    private static final long serialVersionUID = 7596325066827568751L;
    /**
     * 单品
     */
    public static final int ARTICLE_TYPE_SIGNLE = 1;
    /**
     * 套餐
     */
    public static final int ARTICLE_TYPE_MEALS = 2;

    private String id;

    private String name;

    private String nameAlias;

    private String nameShort;

    private String photoBig;

    private String photoSmall;

    private String ingredients;

    private String description;

    private Boolean isEmpty;

    private Integer sort;

    private Boolean activated;

    private Boolean state;

    private Integer remainNumber;

    private Long saleNumber;

    private Long showSaleNumber;

    private Date updateTime;

    private Date createTime;

    private String shopDetailId;

    private String articleFamilyId;

    private String createUserId;

    private String updateUserId;

    private BigDecimal price;

    private BigDecimal fansPrice;

    /**
     * 供应时间关联字段，用于储存 菜品的折扣百分比
     */
    private int discount;

    private Boolean hasMultPrice;

    private String hasUnit;

    private String peference;

    private String unit;

    private ArticleFamilyDto articleFamily;

    private Boolean showBig;

    private Boolean showDesc;

    private Boolean isRemind;

    private String controlColor;

    private Integer articleType;

    private Long likes;

    private List<MealAttrDto> mealAttrs;

    private Integer isHidden;

    /**
     * 用于保存 类型名称
     */
    private String articleFamilyName;

    private List<ArticlePriceDto> articlePrices = new ArrayList<>();
    private Integer[] supportTimes;
    private Integer[] kitchenList;

    //工作日库存
    private Integer stockWorkingDay;

    //周末库存
    private Integer stockWeekend;

    private Integer currentWorkingStock;

    //推荐餐品包id
    private String recommendId;

    //规格属性
    private List<UnitDto> units;

    private Integer newUnit;

    private Integer recommendCount;

    private String pId;

    //饿了吗名称
    private String elemeName;

    private List<PlatformDto> platforms;

    private String photoSquare;

    private Integer mealFeeNumber;

    private Integer isMainFood;

    //虚拟餐品包id
    private Integer virtualId;

    //餐品首字母
    private String initials;

    private  Integer count;

    private String recommendCategoryId;

    private Integer monthlySales;          //菜品月销售量

    private Integer photoType;             //菜品图片显示   1大图  2小图  3无图

    private String photoLittle;            //菜品类型2  小图存放地址

    private Integer openCatty;             //是否开启称斤买卖菜品

    private BigDecimal cattyMoney;         //称斤买卖 价格 单位（kg）

    private Integer number;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        if (number == null){
            this.number = 0;
        }
        this.number = number;
    }

}