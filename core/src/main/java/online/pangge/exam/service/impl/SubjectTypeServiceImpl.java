package online.pangge.exam.service.impl;

import online.pangge.exam.domain.SubjectType;
import online.pangge.exam.mapper.SubjectTypeMapper;
import online.pangge.exam.page.PageResult;
import online.pangge.exam.query.QueryObject;
import online.pangge.exam.service.ISubjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by jie34 on 2017/6/17.
 */
@Service
public class SubjectTypeServiceImpl implements ISubjectTypeService {
    @Autowired
    private SubjectTypeMapper subjectTypeDao;

    @Override
    @CacheEvict(value="subjectall",key = "#root.methodName",allEntries=true)
    public int deleteByIds(List<Long> id) {
        return subjectTypeDao.deleteByIds(id);
    }

    @Override
    @CacheEvict(value="subjectall",key = "#root.methodName")
    public int insert(SubjectType record) {
        return subjectTypeDao.insert(record);
    }

    @Override
    @Cacheable(value="subjectall",key= "#root.methodName")
    public SubjectType selectByPrimaryKey(Long id) {
        return selectByPrimaryKey(id);
    }

    @Override
    @Cacheable(value="subjectall",key= "#root.methodName")
    public List<SubjectType> selectAll() {
        return subjectTypeDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(SubjectType record) {
        return subjectTypeDao.updateByPrimaryKey(record);
    }

    @Override
    @Cacheable(value="subjectall",key = "#methodName")
    public PageResult page(QueryObject qo) {
        Long count = subjectTypeDao.queryCount(qo);
        if (count <= 0) {
            return new PageResult(0, Collections.emptyList());
        }
        return new PageResult(count.intValue(), subjectTypeDao.queryList(qo));
    }


}
