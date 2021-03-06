package online.pangge.exam.web.controller;

import online.pangge.exam.domain.SubjectType;
import online.pangge.exam.page.PageResult;
import online.pangge.exam.query.QueryObject;
import online.pangge.exam.service.ISubjectTypeService;
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

/**
 * Created by jie34 on 2017/6/17.
 */
@Controller
@RequestMapping("/subjectType")
public class SubjectTypeController {
    @Autowired
    private ISubjectTypeService subjectTypeService;

    @RequestMapping("/index.do")
    @RequiredPermission("管理员")
    public String subjectType(){
        return "subjectType";
    }

    @RequestMapping("/type_list.do")
    @RequiredPermission("列出管理员")
    @ResponseBody
    public List<SubjectType> getTypeList(){
        return subjectTypeService.selectAll();
    }
    @RequestMapping("/type_page.do")
    @RequiredPermission("列出管理员")
    @ResponseBody
    public PageResult getTypePage(HttpServletRequest req){
        QueryObject qo = new QueryObject();
        qo.setPage(Integer.valueOf(StringUtils.isEmpty(req.getParameter("page")) ? "5" : req.getParameter("page")));
        qo.setRows(Integer.valueOf(StringUtils.isEmpty(req.getParameter("rows")) ? "5" : req.getParameter("rows")));
        PageResult result =  subjectTypeService.page(qo);
        return result;
    }

    @RequestMapping("/update.do")
    @RequiredPermission("更改管理员")
    @ResponseBody
    public AjaxResult updateType(SubjectType subjectType){
        int updateCount = subjectTypeService.updateByPrimaryKey(subjectType);
        if(updateCount>0){
            return new AjaxResult(true,"更改成功!");
        }
        return new AjaxResult(false,"更改失败!");
    }
    @RequestMapping("/save.do")
    @RequiredPermission("新增管理员")
    @ResponseBody
    public AjaxResult saveAdmin(SubjectType type){
        int updateCount = subjectTypeService.insert(type);
        if(updateCount>0){
            return new AjaxResult(true,"新增成功!");
        }
        return new AjaxResult(false,"新增失败!");
    }
    @RequestMapping("/delete.do")
    @RequiredPermission("删除管理员")
    @ResponseBody
    public AjaxResult deleteAdmin(@RequestParam("ids[]") List<Long> ids){
        int updateCount = subjectTypeService.deleteByIds(ids);
        if(updateCount>0){
            return new AjaxResult(true,"删除成功!");
        }
        return new AjaxResult(false,"删除失败!");
    }
}
