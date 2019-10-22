package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

@Data
public class DatabaseConfigDto implements Serializable{

    private static final long serialVersionUID = -4553406295878235143L;

    private String id;

    @ApiModelProperty(value = "数据库名称")
    private String name;
    
    @ApiModelProperty(value = "数据库路径")
    private String url;

    @ApiModelProperty(value = "数据库驱动名称")
    private String driverClassName;

    @ApiModelProperty(value = "数据库用户名")
    private String username;

    @ApiModelProperty(value = "数据库密码")
    private String password;
    
    private Date createTime;
    
    private Date updateTime;
    
    private Integer state;
    
    //关联查询的 品牌 名称
    private String brandName;

}