package com.resto.shop.web.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by disvenk.dai on 2018-10-20 10:40
 */

@Data
public class ProductCouponForm implements Serializable{

    public Long id;
    //1品牌2门店

    public Integer isBrand;

    public String couponName;

    public String ShopId;

    //按时间类型算
    public Integer timeConsType;

    //产品券价值
    public BigDecimal couponValue;

    //1按日期 2按具体时间
    public Integer couponValiday;

    public Integer deductionType;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    public Date beginDateTime;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    public Date endDateTime;

    public String couponPhoto;

    public Boolean isActivty;

    public Integer distributionModeId;

}
