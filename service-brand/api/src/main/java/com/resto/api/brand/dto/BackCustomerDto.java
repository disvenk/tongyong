package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yz on 2017-03-07.
 */
@Data
public class BackCustomerDto implements Serializable {

    private static final long serialVersionUID = 4966064532375851116L;

    @ApiModelProperty(value = "回头用户的id")
    private String customerId;

    @ApiModelProperty(value = "回头用户出现的次数")
    private  int num ;

}
