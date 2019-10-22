package com.resto.pos.web.controller;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.pos.web.config.SessionKey;
import com.resto.pos.web.rpcinterceptors.DataSourceTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PageController extends GenericController {
    @Resource
    private BrandUserService brandUserService;
    @Resource
    BrandService brandService;
    @Resource
    ShopDetailService shopDetailService;

    @Resource
    BrandSettingService brandSettingService;

    @Autowired
    PosLoginConfigService posLoginConfigService;

    @Autowired
    PosUserService posUserService;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pos");
        return mv;
    }

    @RequestMapping("/pos")
    public ModelAndView pos(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView();
        mv.addObject("mode", getCurrentShop().getShopMode());
        mv.setViewName("pos");
        System.out.println("客户端地址" + request.getRemoteAddr());
        return mv;
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView login(String redirect) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        mv.addObject("redirect", redirect);
        return mv;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(String loginname, String password, String redirect, HttpSession session, HttpServletRequest request) {
        BrandUser loginUser = brandUserService.authentication(new BrandUser(loginname, ApplicationUtils.pwd(password)));
        ModelAndView mv = new ModelAndView();
        if (loginUser == null) {
            return new Result("用户名或者密码错误", false);
        } else {
            ShopDetail shop = shopDetailService.selectById(loginUser.getShopDetailId());
            Brand brand = brandService.selectById(shop.getBrandId());
            BrandSetting brandSetting = brandSettingService.selectById(brand.getBrandSettingId());
            session.setMaxInactiveInterval(36000);
            session.setAttribute(SessionKey.CURRENT_BRAND_SETTING, brandSetting);
            session.setAttribute(SessionKey.USER_INFO, loginUser);
            session.setAttribute(SessionKey.CURRENT_SHOP, shop);
            session.setAttribute(SessionKey.CURRENT_SHOP_ID, shop.getId());
            session.setAttribute(SessionKey.CURRENT_BRAND, brand);
            DataSourceTarget.setDataSourceName(brand.getId());

            PosUser posUser = posUserService.getUserByIp(getIpAddr(request));
            if (posUser == null) {

                posUser = new PosUser();
                posUser.setIp(getIpAddr(request));
                posUser.setPassWord(password);
                posUser.setUserName(loginname);
                posUserService.insertPosUser(posUser);
            }


//			mv.setViewName("redirect:"+(redirect==null?"":redirect));
            return new Result(true);
        }

    }


    /**
     * Pos端和Tv端静默登录入口
     *
     * @param loginname
     * @param password
     * @param session
     * @return
     * @author lmx
     * @version 创建时间：2016年12月12日 下午3:26:28
     */
    @RequestMapping(value = "xlogin", method = RequestMethod.POST)
    public Result xlogin(String loginname, String password, HttpSession session) {
        Result result = new Result();
        BrandUser loginUser = brandUserService.authentication(new BrandUser(loginname, ApplicationUtils.pwd(password)));
        if (loginUser == null) {
            result.setSuccess(false);
        } else {
            ShopDetail shop = shopDetailService.selectById(loginUser.getShopDetailId());
            Brand brand = brandService.selectById(shop.getBrandId());
            BrandSetting brandSetting = brandSettingService.selectById(brand.getBrandSettingId());
            session.setMaxInactiveInterval(36000);
            session.setAttribute(SessionKey.CURRENT_BRAND_SETTING, brandSetting);
            session.setAttribute(SessionKey.USER_INFO, loginUser);
            session.setAttribute(SessionKey.CURRENT_SHOP, shop);
            session.setAttribute(SessionKey.CURRENT_SHOP_ID, shop.getId());
            session.setAttribute(SessionKey.CURRENT_BRAND, brand);
            DataSourceTarget.setDataSourceName(brand.getId());
        }
        return getSuccessResult();
    }

    @RequestMapping("subpage/{subpage}")
    public ModelAndView modelAndView(@PathVariable("subpage") String subpage) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/subpage/" + subpage);
        return mv;
    }

    @RequestMapping("subpage/shopIncome")
    public String getShopIncome() {

        return "subpage/shopIncome/list";
    }

    @RequestMapping("subpage/articleSell")
    public String getArticleSell() {

        return "subpage/article/list";
    }


    @RequestMapping("tv")
    public ModelAndView tv(String name, String password, HttpSession session, HttpServletRequest request) {
        if (name != null && password != null) {
//			ModelAndView model  = new ModelAndView("login");
//			model.addObject("loginname",name);
//			model.addObject("password",password);
//			model.addObject("redirect","tv");
//			model.addObject("session",session);
//			return new ModelAndView("redirect:/login?loginname="+name+"&password="+password+"&redirect="+request.getRequestURL()+"&session="+session);
//			return model;
            tvLogin(name, password, session);
        }
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @RequestMapping("queue")
    public ModelAndView queue() {
        ModelAndView mv = new ModelAndView("queue");
        return mv;
    }

//	@RequestMapping("websocket")
//	public ModelAndView websocket(){
//		ModelAndView mv = new ModelAndView("websocket");
//        System.out.println("3");
//		return mv;
//	}


    /**
     * Pos端和Tv端用于检测，tomcat是否正常运行
     *
     * @return
     * @author lmx
     * @version 创建时间：2016年12月12日 下午3:07:25
     */
    @RequestMapping("/ping")
    @ResponseBody
    public String ping() {
        return getCurrentShopId();
    }

    public void tvLogin(String loginname, String password, HttpSession session) {
        Result result = new Result();
        BrandUser loginUser = brandUserService.authentication(new BrandUser(loginname, ApplicationUtils.pwd(password)));
        if (loginUser == null) {
            result.setSuccess(false);
        } else {
            ShopDetail shop = shopDetailService.selectById(loginUser.getShopDetailId());
            Brand brand = brandService.selectById(shop.getBrandId());
            BrandSetting brandSetting = brandSettingService.selectById(brand.getBrandSettingId());
            session.setMaxInactiveInterval(36000);
            session.setAttribute(SessionKey.CURRENT_BRAND_SETTING, brandSetting);
            session.setAttribute(SessionKey.USER_INFO, loginUser);
            session.setAttribute(SessionKey.CURRENT_SHOP, shop);
            session.setAttribute(SessionKey.CURRENT_SHOP_ID, shop.getId());
            session.setAttribute(SessionKey.CURRENT_BRAND, brand);
            DataSourceTarget.setDataSourceName(brand.getId());
        }
    }
}
