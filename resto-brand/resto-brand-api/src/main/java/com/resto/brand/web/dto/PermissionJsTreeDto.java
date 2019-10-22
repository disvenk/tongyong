package com.resto.brand.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.resto.brand.web.model.Permission;


/**
 * 权限的 Jstree Dto
 *
 */
public class PermissionJsTreeDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7775010614070218739L;
	private Long id;  	//权限id
	private String text; //权限名称
	private List<PermissionJsTreeDto> children;  //子权限
	
	public PermissionJsTreeDto(Permission p) {
		this.id=p.getId();
		this.text = p.getPermissionName();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<PermissionJsTreeDto> getChildren() {
		return children;
	}
	public void setChildren(List<PermissionJsTreeDto> children) {
		this.children = children;
	}
	public void addAllChildren(List<Permission> allPermission) {
		for(Iterator<Permission> it = allPermission.iterator();it.hasNext();){
			Permission p = it.next();
			if(p.getParentId()!=null&&p.getParentId().equals(this.id)){
				it.remove();
				PermissionJsTreeDto dto = new PermissionJsTreeDto(p);
				if(this.children==null){
					this.children=new ArrayList<PermissionJsTreeDto>();
				}
				this.children.add(dto);
				dto.addAllChildren(new ArrayList<Permission>(allPermission));
			}
		}
	}
}
