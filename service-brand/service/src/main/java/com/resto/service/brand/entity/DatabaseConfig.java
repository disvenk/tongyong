package com.resto.service.brand.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

@Data
public class DatabaseConfig implements Serializable{

    private static final long serialVersionUID = -4472067335941957903L;

    private String id;
    
    @NotBlank(message="数据库名称不能为空")
    private String name;
    
    @NotBlank(message="数据库路径不能为空")
    private String url;

    @NotBlank(message="数据库驱动名称不能为空")
    private String driverClassName;

    @NotBlank(message="数据库用户名不能为空")
    private String username;

    @NotBlank(message="数据库密码不能为空")
    private String password;
    
    private Date createTime;
    
    private Date updateTime;
    
    private Integer state;
    
    //关联查询的 品牌 名称
    private String brandName;

}