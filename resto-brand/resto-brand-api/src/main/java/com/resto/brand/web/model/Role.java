package com.resto.brand.web.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class Role implements Serializable {
    private Long id;

    private String description;

    @NotBlank(message="角色名不得为空")
    private String roleName;

    @NotBlank(message="角色标示不得为空")
    private String roleSign;
    
    private Long userGroupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleSign() {
        return roleSign;
    }

    public void setRoleSign(String roleSign) {
        this.roleSign = roleSign == null ? null : roleSign.trim();
    }

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

}