package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

public class DatabaseConfig implements Serializable{
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
    
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName == null ? null : driverClassName.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

	public Integer getState() {
		return (state == null || "".equals(state)) ? 1 : state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName == null ? null : brandName.trim();
	}

}