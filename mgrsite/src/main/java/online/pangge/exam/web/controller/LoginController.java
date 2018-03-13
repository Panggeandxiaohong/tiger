package online.pangge.exam.web.controller;

import online.pangge.exam.domain.Admin;
import online.pangge.exam.domain.Menu;
import online.pangge.exam.service.IAdminService;
import online.pangge.exam.service.IMenuService;
import online.pangge.exam.service.IPermissionService;
import online.pangge.exam.util.AjaxResult;
import online.pangge.exam.util.PermissionUtil;
import online.pangge.exam.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IMenuService menuService;

    @RequestMapping("login.do")
    @ResponseBody
    public AjaxResult login(HttpServletResponse response, HttpServletRequest request, String username, String password) {
        AjaxResult result = null;
        UserContext.setLoacl(request);
        Admin s = adminService.login(username, password);
        if (s != null) {
            UserContext.setUser(s);
            List<String> permission = permissionService.queryByAdminId(s.getId());
            UserContext.getLocal().getSession().setAttribute(UserContext.PERMISSIONINSESSION, permission);
            List<Menu> menus = menuService.queryForMenu();
            PermissionUtil.checkMenuPermission(menus);
            UserContext.getLocal().getSession().setAttribute(UserContext.MENUINSESSION, menus);
            result = new AjaxResult(true, "登陆成功!");
        }else{
            result=new AjaxResult(false,"登陆失败，请检查用户名和密码！");
        }
        return result;
    }
}
