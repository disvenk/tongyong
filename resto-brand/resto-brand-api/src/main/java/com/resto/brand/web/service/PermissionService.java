package com.resto.brand.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Permission;

/**
 * 权限 业务接口
 * 
 * @author StarZou
 * @since 2014年6月10日 下午12:02:39
 **/
public interface PermissionService extends GenericService<Permission, Long> {

    /**
     * 通过角色id 查询角色 拥有的权限
     * 
     * @param roleId
     * @return
     */
    List<Permission> selectPermissionsByRoleId(Long roleId);

	List<Permission> selectAllParents(Long userGroupId);

	List<Permission> selectListByParentId(Long parentId);

	List<String> selectAllUrls(); 

	List<Permission> selectAllMenu(Long userGroupId);

	void addSubPermission(Permission permission);

	void updateSubPermission(Permission model);

	List<Permission> selectList(Long userGroupId);

	List<Permission> selectFullStructMenu(long systemGroup);

	List<Permission> selectPermissionsByRoleIdWithOutParent(Long roleId);

}
