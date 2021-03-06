package online.pangge.exam.service.impl;

import online.pangge.exam.domain.Student;
import online.pangge.exam.mapper.StudentMapper;
import online.pangge.exam.service.IStudentService;
import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by jie34 on 2017/4/9.
 */
@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Override
    public int deleteByPrimaryKey(Long id) {
        return studentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Student record) {
        System.out.println("class = "+record.getClasses()+",name = "+record.getName());
        System.out.println(studentMapper==null);
        return studentMapper.insert(record);
    }

    @Override
    public Student selectByPrimaryKey(Long id) {
        System.out.println("进入select方法");
        Student s = studentMapper.selectByPrimaryKey(id);
        System.out.println("查询出来的结果是:"+s);
        return s;
    }

    @Override
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Student record) {
        return studentMapper.updateByPrimaryKey(record);
    }

    @Override
    public Student selectByWechatName(String wechatname) {
        List<Student> stus = studentMapper.selectByWechatName(wechatname);
        return CollectionUtils.isEmpty(stus)?null:stus.get(0);
    }

    @Override
    public List<Student> selectByStunum(Long stunum) {
        return studentMapper.selectByStunum(stunum);
    }

    @Override
    public boolean checkIsBandStunum(String wechatName) {
        List<Student> bandStudents = studentMapper.selectByWechatName(wechatName);
        Student s =  DataAccessUtils.singleResult(bandStudents);
        return s!=null;
    }
}
