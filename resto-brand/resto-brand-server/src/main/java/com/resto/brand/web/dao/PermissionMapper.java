package com.resto.brand.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.Permission;

public interface PermissionMapper extends GenericDao<Permission, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

	List<Permission> selectPermissionsByRoleId(Long roleId);

	List<Permission> selectList();

	List<Permission> selectAllParents(@Param("userGroupId")Long userGroupId);

	List<Permission> selectByParentId(Long parentId);

	List<Permission> selectMenuByType(@Param("menuTypes")int[] menuTypes, @Param("userGroupId")Long userGroupId);

	List<Permission> selectList(@Param("userGroupId")Long userGroupId);

    List<Permission> getChild(Long parentId);

}