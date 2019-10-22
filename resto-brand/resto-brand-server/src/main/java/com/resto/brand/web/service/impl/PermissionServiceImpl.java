package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.PermissionMapper;
import com.resto.brand.web.enums.MenuType;
import com.resto.brand.web.model.Permission;
import com.resto.brand.web.service.PermissionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 权限Service实现类
 *
 */
@Component
@Service
public class PermissionServiceImpl extends GenericServiceImpl<Permission, Long> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;


    @Override
    public GenericDao<Permission, Long> getDao() {
        return permissionMapper;
    }

    @Override
    public List<Permission> selectPermissionsByRoleId(Long roleId) {
    	List<Permission> permission  = permissionMapper.selectPermissionsByRoleId(roleId);
    	Set<Long> hasPermission = new HashSet<>();
    	for (Permission p : permission) {
			hasPermission.add(p.getId());
		}
    	
    	List<Permission> allHasPermission = addParentPermission(permission, hasPermission);
        return allHasPermission;
    }

	private List<Permission> addParentPermission(List<Permission> allPermissions,Set<Long> hasPermission) {
		List<Permission> allHasPermission = new ArrayList<>();
		allHasPermission.addAll(allPermissions);
		boolean hasParent = false;
		for (Iterator<Permission> iterator = allPermissions.iterator(); iterator.hasNext();) {
    		Permission p = iterator.next();
    		Long pid = p.getParentId();
    		if(pid!=null&&!hasPermission.contains(pid)){
    			Permission parent = selectById(pid);
    			allHasPermission.add(parent);
    			hasPermission.add(pid);
    			hasParent  = true;
    		}
		}
		if(hasParent){
			addParentPermission(allHasPermission, hasPermission);
		}
		return allHasPermission;
	}

	@Override
	public List<Permission> selectAllParents(Long userGroupId) {
		return permissionMapper.selectAllParents(userGroupId);
	}

	@Override
	public List<Permission> selectListByParentId(Long parentId) {
		return permissionMapper.selectByParentId(parentId);
	}

	@Override
	public List<String> selectAllUrls() {
		//TODO get All url
		return new ArrayList<String>();
	}

	@Override
	public List<Permission> selectAllMenu(Long userGroupId) {
		return permissionMapper.selectMenuByType(new int[]{MenuType.AJAX,MenuType.IFRAME,MenuType.LINK},userGroupId);
	}

	@Override
	public void addSubPermission(Permission permission) {
		Permission parent= permissionMapper.selectByPrimaryKey(permission.getParentId());
		permission.setMenuType(MenuType.BUTTON);
		permission.setIsMenu(false);
		permission.setUserGroupId(parent.getUserGroupId());
		permissionMapper.insert(permission);
	}

	@Override
	public void updateSubPermission(Permission model) {
		model.setMenuType(MenuType.BUTTON);
		model.setIsMenu(false);
		permissionMapper.updateByPrimaryKeySelective(model);
	}

	@Override
	public List<Permission> selectList() {
		return permissionMapper.selectList();
	}
	
	@Override
	public int delete(Long id) {
		deleteByParentId(id);
		return super.delete(id);
	}

	public void deleteByParentId(Long parentId) {
		List<Permission> permission = permissionMapper.selectByParentId(parentId);
		for (Permission children : permission) {
			this.delete(children.getId());
		}
	}

	@Override
	public List<Permission> selectList(Long userGroupId) {
		return permissionMapper.selectList(userGroupId);
	}

	@Override
	public List<Permission> selectFullStructMenu(long systemGroup) {
		List<Permission> allParents = selectAllParents(systemGroup);
		List<Permission> allPermission = selectList(systemGroup);
		for(Permission permission : allPermission){
			List<Permission> childs = permissionMapper.getChild(permission.getId());
			permission.setChildren(childs);
		}
		Map<Long,Permission> parentMap = new HashMap<Long,Permission>();
		for(Permission p : allParents){
			parentMap.put(p.getId(), p);
		}
		for(Permission children:allPermission){
			if(parentMap.containsKey(children.getParentId())){
				Permission parent = parentMap.get(children.getParentId());
				parent.getChildren().add(children);
			}
		}		
		return allParents;
	}

	@Override
	public List<Permission> selectPermissionsByRoleIdWithOutParent(Long roleId) {
    	List<Permission> permission  = permissionMapper.selectPermissionsByRoleId(roleId);
		return permission;
	}
}
