package online.pangge.exam.service;

import online.pangge.exam.domain.SubjectType;
import online.pangge.exam.page.PageResult;
import online.pangge.exam.query.QueryObject;

import java.util.List;

/**
 * Created by jie34 on 2017/6/17.
 */
public interface ISubjectTypeService {
    public int deleteByIds(List<Long> id);

    public int insert(SubjectType record);

    public SubjectType selectByPrimaryKey(Long id);

    public List<SubjectType> selectAll();

    public int updateByPrimaryKey(SubjectType record);

    PageResult page(QueryObject qo);
}
