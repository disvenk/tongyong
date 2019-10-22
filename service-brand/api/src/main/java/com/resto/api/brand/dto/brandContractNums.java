package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *为了方便 选择品牌的时候 自动显示公司名称
 * 和多个合同编号
 */
@Data
public class brandContractNums {

    @ApiModelProperty(value = "品牌名")
    private String brandName;

    @ApiModelProperty(value = "公司名")
    private String bCompanyName;

    @ApiModelProperty(value = "合同编号 从数据库查询完后如果有多个则会用，隔开")
    private String contractNums;

}


