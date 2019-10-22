package com.resto.brand.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Role;

/**
 * 角色 业务接口
 **/
public interface RoleService extends GenericService<Role, Long> {
    /**
     * 通过用户id 查询用户 拥有的角色
     * 
     * @param userId
     * @return
     */
    List<Role> selectRolesByUserId(Long userId);

	void assignRolePermissions(Long roleId, Long[] pids);

	void changeUserRoles(Long id, Long[] roleIds);

	List<Role> selectList(Long userGroupId);
}