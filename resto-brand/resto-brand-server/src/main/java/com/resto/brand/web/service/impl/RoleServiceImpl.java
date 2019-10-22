package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.RoleMapper;
import com.resto.brand.web.model.Role;
import com.resto.brand.web.service.RoleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色Service实现类
 *
 */
@Component
@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public GenericDao<Role, Long> getDao() {
        return roleMapper;
    }

    @Override
    public List<Role> selectRolesByUserId(Long userId) {
    	List<Role> roles = roleMapper.selectRolesByUserId(userId);
    	List<Role> roleList = roles;
    	return roles;
        //return roleMapper.selectRolesByUserId(userId);
    }

	@Override
	public void assignRolePermissions(Long roleId, Long[] pids) {
		roleMapper.deleteRolePermissions(roleId);
		if(pids.length>0){
			roleMapper.assignRolePermissions(roleId,pids);
		}
	}

	@Override
	public void changeUserRoles(Long id, Long[] roleIds) {
		roleMapper.deleteUserRoles(id);
		if(roleIds.length>0){
			roleMapper.changeUserRoles(id,roleIds);
		}
	}
	
	@Override
	public int delete(Long id) {
		roleMapper.deleteRolePermissions(id);
		return super.delete(id);
	}

	@Override
	public List<Role> selectList(Long userGroupId) {
		List<Role> roles = roleMapper.selectList(userGroupId);
		return roles;
	}

}
