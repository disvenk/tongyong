package com.resto.shop.web.security;

import java.util.List;

import javax.annotation.Resource;

import com.resto.brand.web.model.*;
import com.resto.brand.web.service.BrandService;
import com.resto.shop.web.dto.BrandPermissionDto;
import com.resto.shop.web.model.ERole;
import com.resto.shop.web.model.ShopBrandUser;
import com.resto.shop.web.rpcinterceptors.DataSourceTarget;
import com.resto.shop.web.service.ERoleService;
import com.resto.shop.web.service.RolePermissionService;
import com.resto.shop.web.service.ShopBrandUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.resto.brand.web.service.RoleService;
import com.resto.brand.web.service.BrandUserService;
import org.springframework.stereotype.Component;

/**
 * 用户身份验证，以及
 * @author Diamond
 * @date 2016年3月24日 
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component(value="securityRealm")

public class BrandUserSecurityRealm extends AuthorizingRealm {

	@Resource
    private BrandUserService brandUserService;

	@Resource
    private ShopBrandUserService shopBrandUserService;

	@Resource
    private RoleService roleService;

	@Resource
    private BrandService brandService;

	@Resource
    private com.resto.brand.web.service.PermissionService  brandPermissionService;

	@Resource
    private ERoleService eRoleService;

	@Resource
    private RolePermissionService rolePermissionService;

    /**
     * 权限检查
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = String.valueOf(principals.getPrimaryPrincipal());

        //出现@的是升级的用户、权限
        if(username.indexOf("@") == -1){
            final BrandUser brandUser = brandUserService.selectByUsername(username);
            Role role= roleService.selectById(brandUser.getRoleId());
            // 添加角色
            System.err.println(role);
            authorizationInfo.addRole(role.getRoleSign());

            final List<Permission> permissions = brandPermissionService.selectPermissionsByRoleId(role.getId());
            for (Permission permission : permissions) {
                // 添加权限
                authorizationInfo.addStringPermission(permission.getPermissionSign());
            }
            return authorizationInfo;
        }else{
            String brandSign = username.substring(username.indexOf("@")+1,username.length());
            //获取到品牌信息
            Brand brand = brandService.selectBySign(brandSign);
            //切换数据源
            DataSourceTarget.setDataSourceName(brand.getId());
            if(username.indexOf("@") != 0){
                final  ShopBrandUser shopBrandUser = shopBrandUserService.selectByUsername(username);
                ERole eRole = eRoleService.selectById(shopBrandUser.getRoleId());
                // 添加角色
                authorizationInfo.addRole(eRole.getRoleSign());

                final List<BrandPermissionDto> brandPermissionDtos = rolePermissionService.selectPermissionsByRoleId(eRole.getId());

                for(BrandPermissionDto brandPermissionDto : brandPermissionDtos) {
                    // 添加权限
                    authorizationInfo.addStringPermission(brandPermissionDto.getPermissionSign());
                }
            }else{
                // 添加角色
                authorizationInfo.addRole("SUPER_ADMIN");
                // 添加权限
                final List<BrandPermissionDto> brandPermissionDtos = rolePermissionService.selectSuperAdmin();

                for(BrandPermissionDto brandPermissionDto : brandPermissionDtos) {
                    // 添加权限
                    authorizationInfo.addStringPermission(brandPermissionDto.getPermissionSign());
                }
            }

            return authorizationInfo;
        }
    }

    /**
     * 登录验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = String.valueOf(token.getPrincipal());
        String password = new String((char[]) token.getCredentials());        
    	// 通过数据库进行验证

        if(username.indexOf("@") == -1){
            final BrandUser authentication = brandUserService.authentication(new BrandUser(username, password));
            if (authentication == null) {
                throw new AuthenticationException("用户名或密码错误.");
            }
        }else{
            String brandSign = username.substring(username.indexOf("@")+1,username.length());
            //获取到品牌信息
            Brand brand = brandService.selectBySign(brandSign);
            //切换数据源
            DataSourceTarget.setDataSourceName(brand.getId());
            if(username.indexOf("@") != 0){
                final ShopBrandUser authenticationNew = shopBrandUserService.authentication(new ShopBrandUser(username, password));
                if (authenticationNew == null) {
                    throw new AuthenticationException("用户名或密码错误.");
                }
            }
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, getName());
        return authenticationInfo;
        }

    }
