package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Permission implements Serializable{
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

    @Override
	public String toString() {
		return "Permission --- "+permissionName+":"+permissionSign;
	}
}