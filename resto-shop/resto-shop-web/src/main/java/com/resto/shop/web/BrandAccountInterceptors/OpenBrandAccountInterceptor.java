package com.resto.shop.web.BrandAccountInterceptors;

import com.resto.shop.web.config.SessionKey;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OpenBrandAccountInterceptor  implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
		HttpSession httpSession = httpServletRequest.getSession();
		if(!(Boolean) httpSession.getAttribute(SessionKey.OPEN_BRAND_ACCOUNT)){//如果是false则认为品牌启动了计费系统并且账户余额小于 最小设置
			//httpServletResponse.sendRedirect("/accountchargeorder/list");
			modelAndView.setViewName("accountchargeorder/list");
		}

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}
