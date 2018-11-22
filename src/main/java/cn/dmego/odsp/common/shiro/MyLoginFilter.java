package cn.dmego.odsp.common.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * class_name: MyLoginFilter
 * package: cn.dmego.odsp.common.shiro
 * describe: 自定义 Shiro 过滤器
 * creat_user: Dmego
 * creat_date: 2018/10/25
 * creat_time: 0:14
 **/
public class MyLoginFilter extends AccessControlFilter {


    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);
        if(!subject.isAuthenticated() && !subject.isRemembered()){
            //如果是 ajax 请求
            if(isAjax((HttpServletRequest) servletRequest)){
                servletResponse.setContentType("application/json;charset=UTF-8");
                PrintWriter out = servletResponse.getWriter();
                out.write("{\"msg\":\"登录过期,请重新登录\",\"code\":401}");
                out.flush();
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是 ajax 请求
     * @param request
     * @return
     */
    public boolean isAjax(HttpServletRequest request){
        String xHeader = request.getHeader("X-Requested-With");
        if(xHeader != null && xHeader.contains("XMLHttpRequest")){
            return true;
        }
        return false;
    }
}
