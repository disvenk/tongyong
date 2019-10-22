package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ShopBrandUser;
import org.apache.ibatis.annotations.Param;

/**
 * @author carl
 * @time 2018/11/20
 */
public interface ShopBrandUserMapper extends GenericDao<ShopBrandUser, String> {

    int deleteByPrimaryKey(String id);

    int insert(ShopBrandUser record);

    int insertSelective(ShopBrandUser record);

    ShopBrandUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShopBrandUser record);

    int updateByPrimaryKey(ShopBrandUser record);

    ShopBrandUser selectByUsername(String username);

    ShopBrandUser authentication(ShopBrandUser shopBrandUser);

    void updateUserByRoleId(Long roleId);

    ShopBrandUser selectByNamePwd(@Param("username") String username, @Param("password") String password);
}
