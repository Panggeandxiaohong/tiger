package online.pangge.exam.service;

import online.pangge.exam.domain.Student;

import java.util.List;

/**
 * Created by jie34 on 2017/4/9.
 */
public interface IStudentService {
    public int deleteByPrimaryKey(Long id);

    public int insert(Student record);

    public Student selectByPrimaryKey(Long id);

    public List<Student> selectAll();

    public int updateByPrimaryKey(Student record);

    public Student selectByWechatName(String wechatname);

    public List<Student> selectByStunum(Long stunum);

    public boolean checkIsBandStunum(String wechatName);
}
