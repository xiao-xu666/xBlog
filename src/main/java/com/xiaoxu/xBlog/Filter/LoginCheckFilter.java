package com.xiaoxu.xBlog.Filter;

import com.alibaba.fastjson.JSON;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器（可支持通配符）
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求: {}",request.getRequestURI());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许的访问方法
        response.setHeader("Access-Control-Allow-Methods","POST, GET, PUT, OPTIONS, DELETE, PATCH");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers","Access-Control-Allow-Headers, content-type,x-requested-with,Authorization, x-ui-request,lang");

        if (true){
            filterChain.doFilter(request,response);
            return;
        }


        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/user/sendMsg",
                "/user/login",
                "/backend/**",
                "/front/**"
        };
        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();

        //2.判断本次请求是否需要处理
        boolean match = check(urls,requestURI);
        //3.如果不需要处理，则直接放行
        if (match){
            filterChain.doFilter(request,response);
            return;
        }
        //4.判断登录状态，如果已经登录，则直接放行
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        if (employeeId != null){
            filterChain.doFilter(request,response);
             return;
         }

        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId != null){
            filterChain.doFilter(request,response);
            return;
        }
        //5.如果未登录则返回未登录结果
        response.getWriter().write(JSON.toJSONString(ReturnResults.error("NOTLOGIN")));
         return;
    }

    /**
     * 路径匹配，判断请求是否需要处理
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url:urls) {
            boolean match = PATH_MATCHER.match(url,requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
