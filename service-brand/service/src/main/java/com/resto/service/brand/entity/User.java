package com.resto.service.brand.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties("password")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 2832141127296721724L;

    private Long id;

    private Date createTime;

    private String password;

    private String state;

    @NotBlank(message="用户名不能为空")
    private String username;
    
    public User(){}

    public User(String username2, String password2) {
    	this.username=username2;
    	this.password=password2;
	}
}