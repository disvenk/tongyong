package com.resto.shop.web.dto;

import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.Permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author carl
 * @time 2018/11/27
 */
public class AssignShopJsTreeDto implements Serializable {

    List<ShopPermissionJsTreeDto> permissions;  //所有的权限

    List<Long> hasPermissions;				//角色已有的权限id

    public List<ShopPermissionJsTreeDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ShopPermissionJsTreeDto> permissions) {
        this.permissions = permissions;
    }

    public List<Long> getHasPermissions() {
        return hasPermissions;
    }

    public void setHasPermisssions(List<Long> hasPermissions) {
        this.hasPermissions = hasPermissions;
    }

    public static AssignShopJsTreeDto createPermissionTree(List<BrandPermissionDto> hasPermission, List<Permission> parentPermission, List<Permission> allPermission) {
        List<ShopPermissionJsTreeDto> pDtos = new ArrayList<ShopPermissionJsTreeDto>();
        AssignShopJsTreeDto dto = new AssignShopJsTreeDto();
        for (Permission parent : parentPermission) {
            ShopPermissionJsTreeDto pdto = new ShopPermissionJsTreeDto(parent);
            pdto.addAllChildren(allPermission);
            pDtos.add(pdto);
        }
        dto.setPermissions(pDtos);
        List<Long> hasPermissionIds = new ArrayList<Long>();
        try {
            hasPermissionIds = ApplicationUtils.ObjectsToFields(hasPermission,"getId",Long.class);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        dto.setHasPermisssions(hasPermissionIds);
        return dto;
    }
}
