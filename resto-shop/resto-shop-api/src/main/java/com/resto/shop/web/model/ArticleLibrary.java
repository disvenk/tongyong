package com.resto.shop.web.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ArticleLibrary implements Serializable{
    private static final long serialVersionUID = -3646980729362828583L;

    //主键
    private String id;
    //自增  菜品编号
    private Integer serialNumber;
    //品牌id
    private String brandId;
    //店铺id
    private String shopId;
    //销售类型  (1堂食 2外卖 3堂食+外卖)
    private Integer distributionModeId;
    //销售类型名称
    private String distributionModeName;
    //餐品首字母缩写
    private String initials;
    //菜品分类
    private String articleFamilyId;
    //菜品分类名称
    private String articleFamilyName;
    //餐品类型 1:普通餐品  2:套餐餐品
    private Integer articleType;
    //菜品名称
    private String name;
    //菜品描述
    private String description;
    //菜品图片显示   1大图  2小图  3无图  4超级大图  5正方形图
    private Byte photoType;
    //小图
    private String photoLittle;
    //大图
    private String photoSmall;
    //超大图
    private String photoSuper;
    //正方形图
    private String photoSquareOriginal;
    //gif图片路径
    private String gifUrl;
    //菜品单品(原价)
    private BigDecimal price;
    //菜品粉丝价
    private BigDecimal fansPrice;
    //是否开启称斤买卖菜品 默认0 不开启  1开启
    private Integer openCatty;
    //称斤买卖 价格 单位（500g   斤）
    private BigDecimal cattyMoney;
    //菜品预警库存  默认0
    private Integer inventoryWarning;
    //工作日库存 默认100
    private Integer stockWorkingDay;
    //周末库存 默认100
    private Integer stockWeekend;
    //排序  默认0
    private Integer sort;
    //数据状态 1正常 0删除
    private Long state;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //创建用户(及登录shop品牌权限后台用户)
    private String createUser;
    //最后一次更新菜品用户
    private String updateUser;
    //菜品单位(份、个)
    private String unit;
    //是否显示大图   默认1 显示    0不显示
    private Boolean showBig;
    //是否显示描述   默认1 显示    0不显示
    private Boolean showDesc;
    //控制按钮颜色   默认  '#000'
    private String controlColor;
    //好评数
    private Long likes;
    //餐盒数量
    private Integer mealFeeNumber;
    //推荐餐品id
    private String recommendId;
    //规格id
    private String unitId;
    //重量包
    private Long weightPackageId;
    //推荐程度
    private String recommendationDegree;
    //预计出餐时长
    private String mealOutTime;
    //菜品类型
    private String articleKind;
    //菜品标签
    private String articleLabel;
    //辣度
    private String articleHot;
    //份量
    private String articleComponent;
    //供应时间
    private Integer[] supportTimes;
    //规格属性
    private List<ArticleUnitNew> units;
    //套餐属性
    private List<MealAttr> mealAttrs;
}