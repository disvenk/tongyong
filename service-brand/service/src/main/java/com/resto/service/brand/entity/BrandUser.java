package com.resto.service.brand.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties({"password"})
@Data
public class BrandUser implements Serializable {

    private static final long serialVersionUID = 7691621136967913577L;

    private String id;

    private String username;

    private String password;

    private String email;

    private String phone;
    
    private Date createTime;
    
    private Date lastLoginTime;

    private Long roleId;
    
    private String brandId;
    
    private String shopDetailId;
    
    //关联查询的 品牌 名称
    private String brandName;
    //关联查询的 店铺名称 名称
    private String shopName;
    //关联查询的 用户角色 名称
    private String roleName;

    private  Integer state;

    private String superPwd;

    private String name;

    private Integer sex;
    
    public BrandUser(String username, String password) {
    	this.username = username;
    	this.password = password;
	}
}