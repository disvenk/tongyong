package com.resto.brand.web.dto;

import com.resto.brand.web.model.Role;

import java.io.Serializable;

public class RoleDto implements Serializable {
	private Long id;
	private String roleName;
	private String roleSign;
	private boolean hasRole;
	public RoleDto(Role role) {
		this.id=role.getId();
		this.roleName=role.getRoleName();
		this.roleSign=role.getRoleSign();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleSign() {
		return roleSign;
	}
	public void setRoleSign(String roleSign) {
		this.roleSign = roleSign;
	}
	public boolean isHasRole() {
		return hasRole;
	}
	public void setHasRole(boolean hasRole) {
		this.hasRole = hasRole;
	}
	
}