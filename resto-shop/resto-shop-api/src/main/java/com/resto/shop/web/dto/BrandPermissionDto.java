package com.resto.shop.web.dto;

import com.resto.brand.web.model.Permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author carl
 * @time 2018/11/23
 */
public class BrandPermissionDto implements Serializable {

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

    public Boolean getMenu() {
        return isMenu;
    }

    public void setMenu(Boolean menu) {
        isMenu = menu;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionSign() {
        return permissionSign;
    }

    public void setPermissionSign(String permissionSign) {
        this.permissionSign = permissionSign;
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

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getIsChildLink() {
        return isChildLink;
    }

    public void setIsChildLink(Integer isChildLink) {
        this.isChildLink = isChildLink;
    }

    public List<Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Permission> children) {
        this.children = children;
    }
}
