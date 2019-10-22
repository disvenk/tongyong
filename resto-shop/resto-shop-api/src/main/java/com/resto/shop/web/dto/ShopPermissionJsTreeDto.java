package com.resto.shop.web.dto;

import com.resto.brand.web.model.Permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author carl
 * @time 2018/11/27
 */
public class ShopPermissionJsTreeDto implements Serializable {
    private Long id;  	//权限id

    private String text; //权限名称

    private List<ShopPermissionJsTreeDto> children;  //子权限

    public ShopPermissionJsTreeDto(Permission p) {
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

    public List<ShopPermissionJsTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<ShopPermissionJsTreeDto> children) {
        this.children = children;
    }

    public void addAllChildren(List<Permission> allPermission) {
        for(Iterator<Permission> it = allPermission.iterator(); it.hasNext();){
            Permission p = it.next();
            if(p.getParentId()!=null&&p.getParentId().equals(this.id)){
                it.remove();
                ShopPermissionJsTreeDto dto = new ShopPermissionJsTreeDto(p);
                if(this.children==null){
                    this.children=new ArrayList<ShopPermissionJsTreeDto>();
                }
                this.children.add(dto);
                dto.addAllChildren(new ArrayList<Permission>(allPermission));
            }
        }
    }

}
