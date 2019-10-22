package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import javax.annotation.Resource;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.RolePermissionMapper;
import com.resto.shop.web.dto.BrandPermissionDto;
import com.resto.shop.web.model.RolePermission;
import com.resto.shop.web.service.RolePermissionService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
@Service
public class RolePermissionServiceImpl extends GenericServiceImpl<RolePermission, Long> implements RolePermissionService {

    @Resource
    private RolePermissionMapper rolepermissionMapper;

    @Override
    public GenericDao<RolePermission, Long> getDao() {
        return rolepermissionMapper;
    }


    @Override
    public List<BrandPermissionDto> selectSuperAdmin() {
        return rolepermissionMapper.selectSuperAdmin();
    }

    @Override
    public List<BrandPermissionDto> selectPermissionsByRoleId(Long roleId) {
        return rolepermissionMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    public void assignRolePermissions(Long roleId, Long[] pids) {
        rolepermissionMapper.deleteRolePermissions(roleId);
        if(pids.length>0){
            rolepermissionMapper.assignRolePermissions(roleId,pids);
        }
    }
}
