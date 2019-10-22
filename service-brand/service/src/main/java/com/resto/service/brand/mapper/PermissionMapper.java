package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Permission;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface PermissionMapper extends BaseDao<Permission, Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

	List<Permission> selectPermissionsByRoleId(Long roleId);

	List<Permission> selectList();

	List<Permission> selectAllParents(@Param("userGroupId") Long userGroupId);

	List<Permission> selectByParentId(Long parentId);

	List<Permission> selectMenuByType(@Param("menuTypes") int[] menuTypes, @Param("userGroupId") Long userGroupId);

	List<Permission> selectList(@Param("userGroupId") Long userGroupId);

    List<Permission> getChild(Long parentId);

}