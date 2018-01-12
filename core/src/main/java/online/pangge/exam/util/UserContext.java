package online.pangge.exam.util;

import online.pangge.exam.domain.Admin;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserContext {
    public static final String USERINSESSION = "USER_IN_REDIS";
    public static final String PERMISSIONINSESSION = "PERMISSION_IN_REDIS";
    public static final String MENUINSESSION = "MENU_IN_REDIS";
    public static ThreadLocal<HttpServletRequest> loacl = new ThreadLocal<>();

    public static void setLoacl(HttpServletRequest request) {
        loacl.set(request);
    }

    public static HttpServletRequest getLocal() {
        return loacl.get();
    }

    private static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

    public static void setUser(Admin admin) {
        getSession().setAttribute(USERINSESSION, admin);
    }

    public static Admin getUser() {
        return (Admin) getSession().getAttribute(USERINSESSION);
    }
}