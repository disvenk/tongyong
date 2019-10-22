package com.resto.brand.web.dto;

import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.Permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分配权限的 Jstree dto
 * @author Diamond
 *
 */
public class AssignJsTreeDto implements Serializable {
	List<PermissionJsTreeDto> permissions;  //所有的权限  
	List<Long> hasPermissions;				//角色已有的权限id
	public List<PermissionJsTreeDto> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<PermissionJsTreeDto> permissions) {
		this.permissions = permissions;
	}
	public List<Long> getHasPermissions() {
		return hasPermissions;
	}
	public void setHasPermisssions(List<Long> hasPermissions) {
		this.hasPermissions = hasPermissions;
	}
	public static AssignJsTreeDto createPermissionTree(List<Permission> hasPermission, List<Permission> parentPermission, List<Permission> allPermission) {
		List<PermissionJsTreeDto> pDtos = new ArrayList<PermissionJsTreeDto>();
		AssignJsTreeDto dto = new AssignJsTreeDto();
		for (Permission parent : parentPermission) {
			PermissionJsTreeDto pdto = new PermissionJsTreeDto(parent);
			pdto.addAllChildren(allPermission);
			pDtos.add(pdto);
		}
		dto.setPermissions(pDtos);
		List<Long> hasPermissionIds = new ArrayList<Long>();
		try {
			hasPermissionIds = ApplicationUtils.ObjectsToFields(hasPermission,"getId",Long.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		dto.setHasPermisssions(hasPermissionIds);
		return dto;
	}
}
