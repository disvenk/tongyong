package com.resto.brand.web.controller.common;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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

import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.UserGroupSign;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.config.SessionKey;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.Role;
import com.resto.brand.web.model.User;
import com.resto.brand.web.service.RoleService;
import com.resto.brand.web.service.UserService;

/**
 * 用户控制器
 * 
 * @author StarZou
 * @since 2014年5月28日 下午3:54:00
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController extends GenericController{

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;
    
    /**
     * 用户登录
     * 
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
        try {
            Subject subject = SecurityUtils.getSubject(); //获取shiro管理的用户对象 主要储存了用户的角色和用户的权限
            // 已登陆则 跳到首页
            if (subject.isAuthenticated()) {
                return "redirect:/";
            }
            if (result.hasErrors()) {
                model.addAttribute("error", "参数错误！");
                return "login";
            }
            // 身份验证
            subject.login(new UsernamePasswordToken(user.getUsername(),ApplicationUtils.pwd( user.getPassword())));
            // 验证成功在Session中保存用户信息
            final User authUserInfo = userService.selectByUsername(user.getUsername());
            request.getSession().setAttribute(SessionKey.USER_INFO, authUserInfo);
        } catch (AuthenticationException e) {
            // 身份验证失败
            model.addAttribute("error", "用户名或密码错误 ！");
            return "login";
        }
        return "redirect:/";
    }

    /**
     * 用户登出
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

    @RequestMapping("list")
	public void toList(){
	}
	
	@RequestMapping("list_all")
	@ResponseBody
	public List<User> list() {
		return userService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		User user = userService.selectById(id);
		return getSuccessResult(user);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		userService.delete(id);
		return new Result(true);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(User user){
		userService.create(user);
		return new Result(true);
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(User user){
		userService.modify(user);
		return new Result(true);
	}
	
	@RequestMapping("role_all_data")
	@ResponseBody
	public Result role_all_data(){
		List<Role> role = roleService.selectList(UserGroupSign.SYSTEM_GROUP);
		return new JSONResult<List<Role>>(role);
	}
	
	@RequestMapping("role_has_data")
	@ResponseBody
	public Result role_data(Long id,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute(SessionKey.USER_INFO);
		List<Role> role = roleService.selectRolesByUserId(user.getId());
		return new JSONResult<List<Role>>(role);
	}
	
	@RequestMapping("role_data_edit")
	@ResponseBody
	public Result role_data_edit(Long id,Long roleIds[]){
		roleService.changeUserRoles(id,roleIds);
		return Result.getSuccess();
	}
}
