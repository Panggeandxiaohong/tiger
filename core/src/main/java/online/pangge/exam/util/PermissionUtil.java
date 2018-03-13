package online.pangge.exam.util;

import online.pangge.exam.domain.Admin;
import online.pangge.exam.domain.Menu;
import online.pangge.exam.domain.Permission;
import online.pangge.exam.service.IPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class PermissionUtil {

    private static IPermissionService permissionService;

    @Autowired
    public void setPermissionService(IPermissionService permissionService) {
        PermissionUtil.permissionService = permissionService;
    }

    public static boolean checkPermission(String function) {
        HttpSession session = UserContext.getLocal().getSession();
        Admin currentUser = (Admin) session.getAttribute(UserContext.USERINSESSION);
        //判断是否是超级管理员,是就放行
        if (currentUser.getIsSuperAdmin()) {
            return true;
        }
        if (CommonUtil.allPermissions.size() == 0) {
            //获取所有的权限信息
            List<Permission> allPermissions = permissionService.selectAll();
            for (Permission permission : allPermissions) {
                CommonUtil.allPermissions.add(permission.getResource());
            }
        }
        //拿到function去所有权限中查询,看该权限是否在所有权限中
        if (CommonUtil.allPermissions.contains(function)) {
            //在权限列表中,需要受权限控制
            //拿到session中的用户权限
            List<String> permissions = (List<String>) session.getAttribute(UserContext.PERMISSIONINSESSION);
            if (permissions.contains(function)) {
                //用户拥有该权限,放行
                return true;
            } else {
                String allPermission = function.split(":")[0] + ":ALL";
                //如果用户拥有ALL权限,放行
                if (permissions.contains(allPermission)) {
                    return true;
                }
                return false;
            }
        } else {
            //不在权限列表中,不需要受权限控制
            return true;
        }
    }

    /**
     * 如果用户没有的权限,就从列表中删除
     */
    public static void checkMenuPermission(List<Menu> menus) {
        //如果是超级管理员,就给他所有的权限
        Admin currentUser = (Admin) UserContext.getLocal().getSession().getAttribute(UserContext.USERINSESSION);
        if (currentUser.getIsSuperAdmin()) {
            return;
        }
        Menu menu;
        //拿到用户已有的权限
        List<String> permissions = (List<String>) UserContext.getLocal().getSession().getAttribute(UserContext.PERMISSIONINSESSION);
        //遍历第一层,看第一层哪些元素需要权限控制
        for (int i = menus.size() - 1; i >= 0; i--) {
            menu = menus.get(i);
            if (StringUtils.isEmpty(menu.getFunction())) {
                if (!permissions.contains(menu.getFunction())) {
                    //用户没有该菜单权限,删除该菜单
                    menus.remove(i);
                }
            }
            //判断是否有子节点
            if (menu.getChildren() != null && menu.getChildren().size() > 0) {
                checkMenuPermission(menu.getChildren());
            }
        }
    }
}
