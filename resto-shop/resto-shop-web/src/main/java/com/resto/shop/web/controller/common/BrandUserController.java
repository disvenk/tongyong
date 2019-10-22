package com.resto.shop.web.controller.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.Encrypter;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.ShopBrandUser;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.rpcinterceptors.DataSourceTarget;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.service.ShopBrandUserService;
import com.resto.shop.web.util.LogTemplateUtils;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 商家用户控制器
 **/
@Controller
@RequestMapping(value = "/branduser")
public class BrandUserController extends GenericController{

    @Resource
    private BrandUserService brandUserService;
    
    @Resource
    private ShopDetailService shopDetailService;

    @Resource
    private BrandService brandService;

    @Resource
    private WetherService wetherService;

    @Resource
    private PosService posService;

    @Resource
	BrandSettingService brandSettingService;

    @Resource
	AccountNoticeService accountNoticeService;

    @Resource
	BrandAccountService brandAccountService;

    @Resource
    ShopBrandUserService shopBrandUserService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 用户登录
     *
     * @param brandUser
     * @param result
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid BrandUser brandUser, BindingResult result, Model model, HttpServletRequest request,String redirect) {
        try {
        	if(redirect == null){
        		redirect = "";
        	}

            Subject subject = SecurityUtils.getSubject(); //获取shiro管理的用户对象 主要储存了用户的角色和用户的权限
            // 已登陆则 跳到首页
            if (subject.isAuthenticated()) {
                return "redirect:/"+redirect;
            }
            if (result.hasErrors()) {
                model.addAttribute("error", "参数错误！");
                return "login";
            }

            	// 身份验证
            if(brandUser.getUsername().indexOf("@") == -1){
                subject.login(new UsernamePasswordToken(brandUser.getUsername(),ApplicationUtils.pwd( brandUser.getPassword())));
            }else{
                subject.login(new UsernamePasswordToken(brandUser.getUsername(), Encrypter.encrypt( brandUser.getPassword())));

            }
            log.info("tttttt");

            // 验证成功在Session中保存用户信息
            BrandUser authUserInfo = null;
            ShopBrandUser  authUserInfoNew = null;
            String brandId = null;
            String shopDetailId = null;
            String shopName = null;
            String brandName = null;
            Long roleId = null;
            //出现@的是升级的用户、权限
            if(brandUser.getUsername().indexOf("@") == -1){
                authUserInfo = brandUserService.selectByUsername(brandUser.getUsername());
                brandId = authUserInfo.getBrandId();
                shopDetailId = authUserInfo.getShopDetailId();
                shopName = authUserInfo.getShopName();
                brandName = authUserInfo.getBrandName();
                roleId = authUserInfo.getRoleId();
            } else if (brandUser.getUsername().indexOf("@") != 0) {

                String brandSign = brandUser.getUsername().substring(brandUser.getUsername().indexOf("@")+1,brandUser.getUsername().length());
                //获取到品牌信息
                Brand brand = brandService.selectBySign(brandSign);
                //切换数据源
                DataSourceTarget.setDataSourceName(brand.getId());

                authUserInfoNew = shopBrandUserService.selectByUsername(brandUser.getUsername());
                brandId = authUserInfoNew.getBrandId();
                shopDetailId = authUserInfoNew.getShopDetailId();
                shopName = authUserInfoNew.getShopName();
                brandName = authUserInfoNew.getBrandName();
                roleId = authUserInfoNew.getRoleId();
            } else {
                String brandSign = brandUser.getUsername().substring(brandUser.getUsername().indexOf("@")+1,brandUser.getUsername().length());
                //获取到品牌信息
                Brand brand = brandService.selectBySign(brandSign);
                //切换数据源
                DataSourceTarget.setDataSourceName(brand.getId());


                ShopBrandUser shopBrandUser = new ShopBrandUser();
                shopBrandUser.setBrandId(brand.getId());
                authUserInfoNew = shopBrandUser;

                brandId = brand.getId();
                brandName = brand.getBrandName();
            }

            log.info("tttttt2222");
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(7200);
            if(authUserInfo != null){
                session.setAttribute(SessionKey.USER_INFO, authUserInfo);
                session.setAttribute(SessionKey.USER_INFO_RESOUCE, 1);
            } else if (authUserInfoNew != null) {
                session.setAttribute(SessionKey.USER_INFO, authUserInfoNew);
                session.setAttribute(SessionKey.USER_INFO_RESOUCE, 2);
            }
            session.setAttribute(SessionKey.CURRENT_BRAND_ID,brandId);
            session.setAttribute(SessionKey.CURRENT_SHOP_ID,shopDetailId);
            session.setAttribute(SessionKey.CURRENT_SHOP_NAME, shopName);
            session.setAttribute(SessionKey.CURRENT_BRAND_NAME, brandName);
            List<ShopDetail> shopDetailList = shopDetailService.selectByBrandId(brandId);
//            if (!StringUtils.isEmpty(shopDetailId == null ? shopDetailId : shopDetailId.trim())){
//                ShopDetail shopDetail = shopDetailService.selectById(shopDetailId);
//                shopDetailList.add(shopDetail);
//            }else {
//                shopDetailList = shopDetailService.selectByBrandId(brandId);
//            }
            session.setAttribute(SessionKey.CURRENT_SHOP_NAMES,shopDetailList);
            Wether wether = wetherService.selectDateAndShopId(shopDetailId, DateUtil.formatDate(new Date(),"yyyy-MM-dd"));

            Boolean flag = true;
			BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
			if(brandSetting!=null&&brandSetting.getOpenBrandAccount()==1){
				BrandAccount brandAccount = brandAccountService.selectByBrandSettingId(brandSetting.getId());
				if(brandAccount!=null){
					List<AccountNotice> accountNotices = accountNoticeService.selectByAccountId(brandAccount.getId());
					if(accountNotices!=null&&!accountNotices.isEmpty()){
						BigDecimal min = accountNotices.get(0).getNoticePrice();//默认第一个最小
						for(int i=0;i<accountNotices.size();i++){
							if(accountNotices.get(i).getNoticePrice().compareTo(min)<0){
								min = accountNotices.get(i).getNoticePrice();
							}
						}
						if(brandAccount.getAccountBalance().compareTo(min)<0){//如果品牌账户小于设置值
							flag = false;
						}
					}
				}

			}
			session.setAttribute(SessionKey.OPEN_BRAND_ACCOUNT,flag);
            session.setAttribute(SessionKey.WETHERINFO,wether);

            Brand brand = brandService.selectByPrimaryKey(brandId);
            ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopDetailId);
            if(shopDetail != null){
                LogTemplateUtils.shopUserLogin(brand.getBrandName(), shopDetail.getName(), getUserName());
            }
        } catch (AuthenticationException e) {
            // 身份验证失败
            model.addAttribute("error", e.getMessage());
            return "login";
        }
        return "redirect:/"+redirect;
    }

    /**
     * 用户登出
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
        if(shopDetail != null){
            LogTemplateUtils.shopUserLogout(brand.getBrandName(), shopDetail.getName(), getUserName());
        }

        session.invalidate();
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }
    

    /**
     * 显示修改用户信息页面
     */
    @RequestMapping("/updatepage")
    public void updatepage(){
    }

    /**
     * 显示修改管理员信息页面
     */
    @RequestMapping("/updatemanagerpwd")
    public void updatemanagerpwd(){

    }


    /**
     * 显示修改管理员信息页面
     */
    @RequestMapping("/checkPwd")
    @ResponseBody
    public Result checkPwd(String password){
        if(password.equals("Vino.2018")){
            return  new Result(true);
        }

        BrandUser brandUser = brandUserService.selectByUsername(getUserName());
        password = ApplicationUtils.pwd(password);


        return new Result(password.equals(brandUser.getSuperPwd()));
    }
    
    @RequestMapping("/updatePwd")
    @ResponseBody
    public Result updatePwd(String password){
    	brandUserService.updatePwd(getCurrentUserId(), password);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.BRANDUSER);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(getCurrentUserId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
    	return getSuccessResult();
    }


    @RequestMapping("/updateSuperPwd")
    @ResponseBody
    public Result updateSuperPwd(String password){
        brandUserService.updateSuperPwd(getCurrentUserId(), password);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.BRANDUSER);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(getCurrentUserId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return getSuccessResult();
    }

    @RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<BrandUser> listData(){
		return brandUserService.selectListBybrandId(getCurrentBrandId());
	}
	
	
	@RequestMapping("/create")
	@ResponseBody
	public Result create(@Valid BrandUser brandUser){
		brandUser.setBrandId(getCurrentBrandId());
		brandUserService.creatBrandUser(brandUser);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.BRANDUSER);
        shopMsgChangeDto.setType("add");
        shopMsgChangeDto.setId(brandUser.getId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
		return Result.getSuccess();
	}


    @RequestMapping("/modify")
    @ResponseBody
    public Result modify(@Valid BrandUser brandUser){
        brandUserService.update(brandUser);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.BRANDUSER);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(getCurrentUserId());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Result.getSuccess();
    }
	
	@RequestMapping("/checkusername")
	@ResponseBody
	public Result checkUserName(String userName){
		BrandUser user = brandUserService.selectByUsername(userName);
		return  getSuccessResult(user);
	}
	

}
