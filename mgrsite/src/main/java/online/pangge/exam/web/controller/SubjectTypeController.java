package online.pangge.exam.web.controller;

import online.pangge.exam.domain.SubjectType;
import online.pangge.exam.service.ISubjectTypeService;
import online.pangge.exam.util.RequiredPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by jie34 on 2017/6/17.
 */
@Controller
public class SubjectTypeController {
    @Autowired
    private ISubjectTypeService subjectTypeService;

    @RequiredPermission("查看试题类型")
    @RequestMapping("/type_list.do")
    @ResponseBody
    public List<SubjectType> getSubjectList(){
        return subjectTypeService.selectAll();
    }
}
