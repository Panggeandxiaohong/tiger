package online.pangge.exam.web.controller;

import online.pangge.exam.service.IPermissionService;
import online.pangge.exam.util.AjaxResult;
import online.pangge.exam.util.RequiredPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;

    @RequestMapping("/reload.do")
    @ResponseBody
    @RequiredPermission("重载权限")
    public AjaxResult reloadPermission(){
        try {
            permissionService.reload();
            AjaxResult data = new AjaxResult(true,"ok!");
            return  data;
        }catch(Exception e){
            AjaxResult data = new AjaxResult(false,"has error!");
            return  data;
        }
    }
}
