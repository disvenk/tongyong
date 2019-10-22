package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ShopBrandUser;

/**
 * @author carl
 * @time 2018/11/20
 */
public interface ShopBrandUserService extends GenericService<ShopBrandUser, String> {

    ShopBrandUser selectByUsername(String username);

    ShopBrandUser authentication(ShopBrandUser shopBrandUser);

    /**
     * 联动操作角色用户的shopId为""
     * @param roleId
     */
    void updateUserByRoleId(Long roleId);

    ShopBrandUser selectByNamePwd(String username, String password);
}
