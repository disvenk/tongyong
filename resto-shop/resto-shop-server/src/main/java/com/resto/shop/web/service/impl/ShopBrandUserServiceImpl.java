package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ShopBrandUserMapper;
import com.resto.shop.web.model.ShopBrandUser;
import com.resto.shop.web.service.ShopBrandUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author carl
 * @time 2018/11/20
 */
@Component
@Service
public class ShopBrandUserServiceImpl extends GenericServiceImpl<ShopBrandUser, String> implements ShopBrandUserService {

    @Resource
    private ShopBrandUserMapper shopBrandUserMapper;


    @Override
    public GenericDao<ShopBrandUser, String> getDao() {
        return shopBrandUserMapper;
    }

    @Override
    public ShopBrandUser selectByUsername(String username) {
        return shopBrandUserMapper.selectByUsername(username);
    }

    @Override
    public ShopBrandUser authentication(ShopBrandUser shopBrandUser) {
        ShopBrandUser user = shopBrandUserMapper.authentication(shopBrandUser);
        //判断是否为空	如果不为空则修改登入时间
        if(user!=null){
            user.setLastLoginTime(new Date());
            update(user);
        }
        return user;
    }

    @Override
    public void updateUserByRoleId(Long roleId) {
        shopBrandUserMapper.updateUserByRoleId(roleId);
    }

    @Override
    public ShopBrandUser selectByNamePwd(String username, String password) {
        return shopBrandUserMapper.selectByNamePwd(username, password);
    }
}
