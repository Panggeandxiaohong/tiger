package online.pangge.exam.web.interceptor;

import online.pangge.exam.domain.Admin;
import online.pangge.exam.util.PermissionUtil;
import online.pangge.exam.util.UserContext;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Admin admin = (Admin)request.getSession().getAttribute(UserContext.USERINSESSION);
        if(admin==null){
            response.sendRedirect("/login.jsp");
            return false;
        }
        UserContext.setLoacl(request);

        //past static resource
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod method = (HandlerMethod)handler;
        String exp = method.getBean().getClass().getName()+":"+method.getMethod().getName();
        Boolean hasPermission = PermissionUtil.checkPermission(exp);
        if(hasPermission){
            return true;
        }else{
            if(method.getMethod().isAnnotationPresent(ResponseBody.class)){
                response.sendRedirect("nopermission.jason");
            }else{
                response.sendRedirect("nopermission.jsp");
            }
            return false;
        }
    }
}
