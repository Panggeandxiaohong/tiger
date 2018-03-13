package online.pangge.exam.web.controller;

import online.pangge.exam.domain.Admin;
import online.pangge.exam.page.PageResult;
import online.pangge.exam.query.QueryObject;
import online.pangge.exam.service.IAdminService;
import online.pangge.exam.util.AjaxResult;
import online.pangge.exam.util.RequiredPermission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @RequestMapping("/index.do")
    @RequiredPermission("管理员")
    public String admin(){
        return "admin";
    }

    @RequestMapping("/admin_list.do")
    @RequiredPermission("列出管理员")
    @ResponseBody
    public PageResult getAdminList(HttpServletRequest req){
        QueryObject qo = new QueryObject();
        qo.setPage(Integer.valueOf(StringUtils.isEmpty(req.getParameter("page")) ? "5" : req.getParameter("page")));
        qo.setRows(Integer.valueOf(StringUtils.isEmpty(req.getParameter("rows")) ? "5" : req.getParameter("rows")));
        PageResult result =  adminService.page(qo);
        return result;
    }

    @RequestMapping("/update.do")
    @RequiredPermission("更改管理员")
    @ResponseBody
    public AjaxResult updateAdmin(Admin admin){
        int updateCount = adminService.updateByPrimaryKey(admin);
        if(updateCount>0){
            return new AjaxResult(true,"更改成功!");
        }
        return new AjaxResult(false,"更改失败!");
    }
    @RequestMapping("/save.do")
    @RequiredPermission("新增管理员")
    @ResponseBody
    public AjaxResult saveAdmin(Admin admin){
        int updateCount = adminService.insert(admin);
        if(updateCount>0){
            return new AjaxResult(true,"新增成功!");
        }
        return new AjaxResult(false,"新增失败!");
    }
    @RequestMapping("/delete.do")
    @RequiredPermission("删除管理员")
    @ResponseBody
    public AjaxResult deleteAdmin(@RequestParam("ids[]") List<Long> ids){
        int updateCount = adminService.deleteByIds(ids);
        if(updateCount>0){
            return new AjaxResult(true,"删除成功!");
        }
        return new AjaxResult(false,"删除失败!");
    }
}
