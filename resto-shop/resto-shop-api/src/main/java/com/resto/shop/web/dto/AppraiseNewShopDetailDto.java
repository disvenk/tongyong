package com.resto.shop.web.dto;


import com.resto.brand.core.util.ExcelAnnotation;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/6/9.
 */
@Data
public class AppraiseNewShopDetailDto implements Serializable{

    private static final long serialVersionUID = 6366844779935529851L;
    @ExcelAnnotation(exportName = "评论时间")
    private String  createTime;

    @ExcelAnnotation(exportName = "用户手机号")
    private String telephone;

    @ExcelAnnotation(exportName = "订单金额")
    private BigDecimal orderMoney;

    @ExcelAnnotation(exportName = "评论红包金额")
    private BigDecimal redMoney;

    @ExcelAnnotation(exportName = "评分")
    private BigDecimal level;

    @ExcelAnnotation(exportName = "服务")
    private BigDecimal service;

    @ExcelAnnotation(exportName = "环境")
    private BigDecimal conditions;

    @ExcelAnnotation(exportName = "性价比")
    private BigDecimal price;

    @ExcelAnnotation(exportName = "氛围")
    private BigDecimal ambience;

    @ExcelAnnotation(exportName = "出品")
    private BigDecimal exhibit;

    @ExcelAnnotation(exportName = "评论内容")
    private String content;

    @ExcelAnnotation(exportName = "评论标签")
    private String feedback;

    @ExcelAnnotation(exportName = "推荐菜品 ")
    private String zanArticle;

    @ExcelAnnotation(exportName = "吐槽菜品 ")
    private String spitArticle;

}
