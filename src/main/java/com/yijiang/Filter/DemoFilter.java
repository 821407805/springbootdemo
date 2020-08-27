package com.yijiang.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yi
 * @date 2020/6/8 11:07
 */
@WebFilter( urlPatterns = "/api/*", filterName = "demoFilter")
public class DemoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse )servletResponse;
        String name = req.getParameter("name");
        if( "jasonxiao".equals( name ) ){
            filterChain.doFilter(req, resp);
        }else {
            resp.sendRedirect("/index.html");
        }

    }

    @Override
    public void destroy() {

    }
}
