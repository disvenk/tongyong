package com.resto.service.shop.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RedConfig implements Serializable {

    private static final long serialVersionUID = 9063503690944715371L;

    private Long id;
    
    @NotNull(message="{延迟发送时间   不能为空}")
    @Range(min=0,message="{延迟发送时间     不能小于0}")
    private Integer delay;
    
    @NotNull(message="{单个红包最小比例   不能为空}")
    @Range(min=0,message="{单个红包最小比例    不能小于0}")
    private Integer minRatio;

    @NotNull(message="{单个红包最大比例   不能为空}")
    @Range(min=0,message="{单个红包最大比例   不能小于0}")
    private Integer maxRatio;

    @NotNull(message="{单个最大红包   不能为空}")
    @Range(min=0,message="{单个最大红包   不能小于0}")
    private BigDecimal maxSingleRed;
    
    @NotNull(message="{单个最小红包   不能为空}")
    @Range(min=0,message="{单个最小红包   不能小于0}")
    private BigDecimal minSignleRed;

    @NotBlank(message="{红包标题   不能为空}")
    private String title;

    @NotBlank(message="{红包备注   不能为空}")
    private String remark;

    @NotNull(message="{是否可以叠加   不能为空}")
    private Byte isAddRatio;

    private BigDecimal minTranslateMoney;

    @NotNull(message="{是否启用   不能为空}")
    private Integer isActivity;

    private String shopDetailId;

}