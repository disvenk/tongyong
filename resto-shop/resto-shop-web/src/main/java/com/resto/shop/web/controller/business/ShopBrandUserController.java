package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.Encrypter;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.ERole;
import com.resto.shop.web.model.ShopBrandUser;
import com.resto.shop.web.service.ERoleService;
import com.resto.shop.web.service.ShopBrandUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author carl
 * @time 2018/11/23
 */

@Controller
@RequestMapping("shopbranduser")
public class ShopBrandUserController extends GenericController {

    @Resource
    private ShopBrandUserService shopBrandUserService;

    @Resource
    private BrandService brandService;

    @Resource
    private ShopDetailService shopDetailService;

    @Resource
    private ERoleService eRoleService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<ShopBrandUser> listData(){
        List<ShopBrandUser> shopBrandUsers = shopBrandUserService.selectList();
        for(ShopBrandUser shopBrandUser : shopBrandUsers){
            shopBrandUser.setNameLeft(shopBrandUser.getUsername().substring(0, shopBrandUser.getUsername().indexOf("@")));
            shopBrandUser.setNameRight(shopBrandUser.getUsername().substring(shopBrandUser.getUsername().indexOf("@"), shopBrandUser.getUsername().length()));
            shopBrandUser.setPassword(Encrypter.decrypt(shopBrandUser.getPassword()));
        }
        return shopBrandUsers;
    }

    @RequestMapping("/brandInfo")
    @ResponseBody
    public Brand brandInfo(){
        return brandService.selectByPrimaryKey(getCurrentBrandId());
    }

    @RequestMapping("/queryShops")
    @ResponseBody
    public Result queryShops(){
        List<ShopDetail> shops = shopDetailService.selectByBrandId(getCurrentBrandId());
        return getSuccessResult(shops);
    }

    @RequestMapping("/queryRoles")
    @ResponseBody
    public Result queryRoles(){
        List<ERole> eRoles = eRoleService.selectByStauts();
        return getSuccessResult(eRoles);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid ShopBrandUser shopBrandUser){
        shopBrandUser.setId(ApplicationUtils.randomUUID());
        shopBrandUser.setBrandId(getCurrentBrandId());
        shopBrandUser.setCreateTime(new Date());
        shopBrandUser.setPassword(Encrypter.encrypt(shopBrandUser.getPassword()));
        shopBrandUser.setUsername(shopBrandUser.getNameLeft() + shopBrandUser.getNameRight());
        shopBrandUserService.insert(shopBrandUser);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid ShopBrandUser shopBrandUser){
        shopBrandUser.setPassword(Encrypter.encrypt(shopBrandUser.getPassword()));
        shopBrandUser.setUsername(shopBrandUser.getNameLeft() + shopBrandUser.getNameRight());
        shopBrandUserService.update(shopBrandUser);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id){
        shopBrandUserService.delete(id);
        return Result.getSuccess();
    }
}
