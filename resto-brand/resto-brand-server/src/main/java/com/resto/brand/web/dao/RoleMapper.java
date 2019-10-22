package com.resto.brand.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.Role;

public interface RoleMapper extends GenericDao<Role, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

	List<Role> selectRolesByUserId(Long userId);

	void assignRolePermissions(@Param("roleId")Long roleId, @Param("permissionIds")Long[] pids);

	void deleteRolePermissions(Long roleId);

	void changeUserRoles(@Param("userId")Long userId, @Param("roleIds")Long[] roleIds);

	void deleteUserRoles(Long id);

	List<Role> selectList(@Param("userGroupId")Long userGroupId);

}