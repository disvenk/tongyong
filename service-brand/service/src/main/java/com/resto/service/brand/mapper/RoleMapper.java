package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseDao<Role, Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

	List<Role> selectRolesByUserId(Long userId);

	void assignRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") Long[] pids);

	void deleteRolePermissions(Long roleId);

	void changeUserRoles(@Param("userId") Long userId, @Param("roleIds") Long[] roleIds);

	void deleteUserRoles(Long id);

	List<Role> selectList(@Param("userGroupId") Long userGroupId);

}