package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Permission implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5733730890739103580L;
	

	private Long id;

    private Boolean isMenu;

    private String menuIcon;

    private String permissionName;

    private String permissionSign;

    private Integer sort;

    private Long parentId;

    private Integer menuType;
    
    private Long userGroupId;

    private Integer isChildLink;
    
    private List<Permission> children = new ArrayList<Permission>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(Boolean isMenu) {
        this.isMenu = isMenu;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon == null ? null : menuIcon.trim();
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    public String getPermissionSign() {
        return permissionSign;
    }

    public void setPermissionSign(String permissionSign) {
        this.permissionSign = permissionSign == null ? null : permissionSign.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

	public List<Permission> getChildren() {
		return children;
	}

	public void setChildren(List<Permission> children) {
		this.children = children;
	}
	
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroup) {
		this.userGroupId = userGroup;
	}

    public Integer getIsChildLink() {
        return isChildLink;
    }

    public void setIsChildLink(Integer isChildLink) {
        this.isChildLink = isChildLink;
    }

    @Override
	public String toString() {
		return "Permission --- "+permissionName+":"+permissionSign;
	}
}