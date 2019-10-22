/*
package com.daishaowen.test.crossDomain;*/
package com.resto.wechat.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
*@Description:
*@Author:disvenk.dai
*@Date:19:51 2018/6/14 0014
*/
public class DomainFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest res, ServletResponse req, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) req;
        HttpServletRequest request = (HttpServletRequest)res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        //指定客户端允许发送cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
      /*  addHeader，如果同名header已存在，则追加至原同名header后面。
        setHeader，如果同名header已存在，则覆盖一个同名header*/
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials","true"); //是否支持cookie跨域
        //Access-Control-Expose-Headers
        //默认情况下我们在浏览器下面看到的首部信息是默认的，有些我们访问不到
        //我们可以使用上面的那个参数，然后在后面添加我们想让浏览看到的额外首部
        chain.doFilter(res, req);
    }

    @Override
    public void destroy() {

    }
}


