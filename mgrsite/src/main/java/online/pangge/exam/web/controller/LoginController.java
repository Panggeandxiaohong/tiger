package online.pangge.exam.web.controller;

import online.pangge.exam.domain.Admin;
import online.pangge.exam.domain.Permission;
import online.pangge.exam.domain.Role;
import online.pangge.exam.service.IAdminService;
import online.pangge.exam.service.IPermissionService;
import online.pangge.exam.util.AjaxResult;
import online.pangge.exam.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IPermissionService permissionService;

    @RequestMapping("login.do")
    @ResponseBody
    public AjaxResult login(HttpServletResponse response, String username, String password) {
        AjaxResult result = null;
        Admin s = adminService.login(username, password);
        if (s != null) {
            UserContext.setUser(s);
            List<Role> roles = s.getRoles();
            List<String> permissions = new ArrayList<>();
            for(Role e: roles){
                List<Permission> permission = e.getPermissions();

            }
        }
        return new AjaxResult(false, "出现异常，请检查账号密码是否正确！");
    }
}
