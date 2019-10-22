package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.dto.BrandPermissionDto;
import com.resto.shop.web.model.RolePermission;

import java.util.List;

public interface RolePermissionService extends GenericService<RolePermission, Long> {

    /**
     * 超级会员的默认创建角色跟用户权限
     * @return
     */
    List<BrandPermissionDto> selectSuperAdmin();

    /**
     * 通过角色id 查询角色 拥有的权限
     * @param roleId
     * @return
     */
    List<BrandPermissionDto> selectPermissionsByRoleId(Long roleId);

    /**
     * 重新给role赋权限
     * @param roleId
     * @param pids
     */
    void assignRolePermissions(Long roleId, Long[] pids);
}
